package io.github.devatherock.yamlvalidator

import groovy.util.logging.Log
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

/**
 * Test class to test the built docker images
 * 
 * @author z0033zk
 *
 */
@Log
class YamlValidatorSpec extends Specification {
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
    def 'test yaml validator - #folderName'() {
        when: 'drone yaml validator'
        def output = executeCommand(['docker', 'run', '--rm', '-v',
                                     "${System.properties['user.dir']}/src/test/resources/data/${folderName}:/work",
                                     '-w=/work', imagesToTest[0]])

        then:
        output[0] == expectedExitCode
        output[1].contains(outputText)

        when: 'vela yaml validator'
        executeCommand(['docker', 'run', '--rm', '-v',
                        "${System.properties['user.dir']}/src/test/resources/data/${folderName}:/work",
                        '-w=/work', imagesToTest[1]])

        then:
        output[0] == expectedExitCode
        output[1].contains(outputText)

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
                "'/work/config.yml' is invalid"
        ]
    }

    /**
     * Executes a command and returns the exit code
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
