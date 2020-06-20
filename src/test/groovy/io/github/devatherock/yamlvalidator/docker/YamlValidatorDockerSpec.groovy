package io.github.devatherock.yamlvalidator.docker

import groovy.util.logging.Log
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

/**
 * Test class to test the built docker images
 *
 * @author z0033zk
 */
@Log
class YamlValidatorDockerSpec extends Specification {
    @Shared
    @Subject
    def imagesToTest = [
            'devatherock/drone-yaml-validator:latest',
            'devatherock/vela-yaml-validator:latest'
    ]

    def setupSpec() {
        System.setProperty('java.util.logging.SimpleFormatter.format', '%5$s%n')
        imagesToTest.each { executeCommand("docker pull ${it}") }
    }

    @Unroll
    def 'test drone yaml validator - #folderName'() {
        given:
        log.info("Current folder: ${System.properties['user.dir']}")
        new File(System.properties['user.dir']).eachFile { file ->
            log.info("File name: ${file}")
        }

        when:
        def output = executeCommand(['docker', 'run', '--rm', '-v',
                                     "${System.properties['user.dir']}/src/test/resources/data/${folderName}:/work",
                                     '-w=/work', imagesToTest[0]])

        then:
        output[0] == expectedExitCode
        output[1].contains(outputText)
        !output[1].contains('Validating files in')
        !output[1].contains("Validating '")

        where:
        folderName << [
                'valid', 'invalid', 'invalid2'
        ]
        expectedExitCode << [
                0, 1, 1
        ]
        outputText << [
                "'/work/config.yml' is valid",
                "'/work/multi-doc.yml' is invalid",
                "'/work/anchor.yml' is invalid"
        ]
    }

    @Unroll
    def 'test drone yaml validator with debug enabled - #folderName'() {
        when:
        def output = executeCommand(['docker', 'run', '--rm', '-v',
                                     "${System.properties['user.dir']}/src/test/resources/data/${folderName}:/work",
                                     '-w=/work', '-e', 'PLUGIN_DEBUG=true', imagesToTest[0]])

        then:
        output[0] == expectedExitCode
        output[1].contains(outputText)
        output[1].contains('Validating files in')
        output[1].contains("Validating '")

        where:
        folderName << [
                'valid', 'invalid', 'invalid2'
        ]
        expectedExitCode << [
                0, 1, 1
        ]
        outputText << [
                "'/work/config.yml' is valid",
                "'/work/multi-doc.yml' is invalid",
                "'/work/anchor.yml' is invalid"
        ]
    }

    @Unroll
    def 'test vela yaml validator - #folderName'() {
        when:
        def output = executeCommand(['docker', 'run', '--rm', '-v',
                                     "${System.properties['user.dir']}/src/test/resources/data/${folderName}:/work",
                                     '-w=/work', imagesToTest[1]])

        then:
        output[0] == expectedExitCode
        output[1].contains(outputText)
        !output[1].contains('Validating files in')
        !output[1].contains("Validating '")

        where:
        folderName << [
                'valid', 'invalid', 'invalid2'
        ]
        expectedExitCode << [
                0, 1, 1
        ]
        outputText << [
                "'/work/config.yml' is valid",
                "'/work/multi-doc.yml' is invalid",
                "'/work/anchor.yml' is invalid"
        ]
    }

    @Unroll
    def 'test vela yaml validator with debug enabled - #folderName'() {
        when:
        def output = executeCommand(['docker', 'run', '--rm', '-v',
                                     "${System.properties['user.dir']}/src/test/resources/data/${folderName}:/work",
                                     '-w=/work', '-e', 'PARAMETER_DEBUG=true', imagesToTest[0]])

        then:
        output[0] == expectedExitCode
        output[1].contains(outputText)
        output[1].contains('Validating files in')
        output[1].contains("Validating '")

        where:
        folderName << [
                'valid', 'invalid', 'invalid2'
        ]
        expectedExitCode << [
                0, 1, 1
        ]
        outputText << [
                "'/work/config.yml' is valid",
                "'/work/multi-doc.yml' is invalid",
                "'/work/anchor.yml' is invalid"
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
