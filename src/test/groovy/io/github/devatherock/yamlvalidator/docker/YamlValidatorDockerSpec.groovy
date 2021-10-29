package io.github.devatherock.yamlvalidator.docker

import groovy.util.logging.Log
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Test class to test the built docker images
 */
@Log
class YamlValidatorDockerSpec extends Specification {

    @Shared
    def config = [
            'drone': [
                    'envPrefix': 'PLUGIN_'
            ],
            'vela' : [
                    'envPrefix': 'PARAMETER_'
            ]
    ]

    @Shared
    String dockerImage = 'devatherock/drone-yaml-validator:latest'

    def setupSpec() {
        System.setProperty('java.util.logging.SimpleFormatter.format', '%5$s%n')
        executeCommand("docker pull ${dockerImage}")
    }

    @Unroll
    def 'test yaml validator - debug enabled: #debugEnabled, folder: #folderName, image: #ci'() {
        when:
        def output = executeCommand(['docker', 'run', '--rm',
                                     '-v', "${System.properties['user.dir']}:/work",
                                     '-w=/work',
                                     '-e', "${config[ci].envPrefix}DEBUG=${debugEnabled}",
                                     '-e', "${config[ci].envPrefix}SEARCH_PATH=/work/src/test/resources/data/${folderName}",
                                     dockerImage])

        then:
        output[0] == expectedExitCode
        output[1].contains(outputText)
        output[1].contains('Validating files in') == debugEnabled
        output[1].contains("Validating '") == debugEnabled

        where:
        folderName << [
                'valid', 'invalid', 'invalid2', 'duplicate', 'valid', 'invalid', 'invalid2', 'duplicate',
                'valid', 'invalid', 'invalid2', 'duplicate', 'valid', 'invalid', 'invalid2', 'duplicate'
        ]
        expectedExitCode << [
                0, 1, 1, 1, 0, 1, 1, 1,
                0, 1, 1, 1, 0, 1, 1, 1
        ]
        outputText << [
                "/config.yml' is valid",
                "/multi-doc.yml' is invalid",
                "/anchor.yml' is invalid",
                "/duplicate-keys.yml' is invalid",
                "/config.yml' is valid",
                "/multi-doc.yml' is invalid",
                "/anchor.yml' is invalid",
                "/duplicate-keys.yml' is invalid",
                "/config.yml' is valid",
                "/multi-doc.yml' is invalid",
                "/anchor.yml' is invalid",
                "/duplicate-keys.yml' is invalid",
                "/config.yml' is valid",
                "/multi-doc.yml' is invalid",
                "/anchor.yml' is invalid",
                "/duplicate-keys.yml' is invalid",
        ]
        ci << [
                'drone', 'drone', 'drone', 'drone', 'drone', 'drone', 'drone', 'drone',
                'vela', 'vela', 'vela', 'vela', 'vela', 'vela', 'vela', 'vela'
        ]
        debugEnabled << [
                false, false, false, false, true, true, true, true,
                false, false, false, false, true, true, true, true
        ]
    }

    @Unroll
    void 'test yaml validator - continue: #continueOnError, image: #ci'() {
        when:
        def output = executeCommand(['docker', 'run', '--rm',
                                     '-v', "${System.properties['user.dir']}/src/test/resources/data:/work",
                                     '-w=/work',
                                     '-e', "${config[ci].envPrefix}CONTINUE_ON_ERROR=${continueOnError}",
                                     dockerImage])

        then:
        output[0] == 1
        output[1].count('is invalid') == invalidCount

        where:
        continueOnError << [
                true, false,
                true, false
        ]
        invalidCount << [
                3, 1,
                3, 1
        ]
        ci << [
                'drone', 'drone',
                'vela', 'vela'
        ]
    }

    @Unroll
    void 'test yaml validator - allowDuplicateKeys: #allowDuplicateKeys, image: #ci'() {
        when:
        def output = executeCommand(['docker', 'run', '--rm',
                                     '-v', "${System.properties['user.dir']}/src/test/resources/data/duplicate:/work",
                                     '-w=/work',
                                     '-e', "${config[ci].envPrefix}ALLOW_DUPLICATE_KEYS=${allowDuplicateKeys}",
                                     dockerImage])

        then:
        output[0] == expectedExitCode
        output[1].contains(outputText)

        where:
        allowDuplicateKeys << [
                true, false,
                true, false
        ]
        expectedExitCode << [
                0, 1,
                0, 1
        ]
        outputText << [
                "/duplicate-keys.yml' is valid",
                "/duplicate-keys.yml' is invalid",
                "/duplicate-keys.yml' is valid",
                "/duplicate-keys.yml' is invalid"
        ]
        ci << [
                'drone', 'drone',
                'vela', 'vela'
        ]
    }

    /**
     * Executes a command and returns the exit code and output
     *
     * @param command
     * @return exit code and output
     */
    def executeCommand(def command) {
        Process process = command.execute()
        StringBuilder out = new StringBuilder()
        StringBuilder err = new StringBuilder()
        process.consumeProcessOutput(out, err)
        int exitCode = process.waitFor()

        if (out.length() > 0) {
            log.info(out.toString())
        }
        if (err.length() > 0) {
            log.severe(err.toString())
        }

        return [exitCode, "${out}${System.lineSeparator()}${err}"]
    }
}
