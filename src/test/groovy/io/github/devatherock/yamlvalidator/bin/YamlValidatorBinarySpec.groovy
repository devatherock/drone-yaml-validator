package io.github.devatherock.yamlvalidator.bin

import groovy.util.logging.Log
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Test class to test the built native binary
 *
 * @author z0033zk
 */
@Log
class YamlValidatorBinarySpec extends Specification {
    def setupSpec() {
        System.setProperty('java.util.logging.SimpleFormatter.format', '%5$s%n')
    }

    def 'test yaml validator - entire workspace provided'() {
        when:
        def output = executeCommand(['./YamlValidator', '--debug', 'false'])

        then:
        output[0] == 1
        output[1].contains('is invalid')
        !output[1].contains('Validating files in')
        !output[1].contains("Validating '")
    }

    def 'test yaml validator - entire workspace provided, debug enabled'() {
        when:
        def output = executeCommand(['./YamlValidator', '--debug', 'true'])

        then:
        output[0] == 1
        output[1].contains('is invalid')
        output[1].contains('Validating files in')
        output[1].contains("Validating '")
    }

    @Unroll
    def 'test yaml validator - continue: #continueOnError'() {
        when:
        def output = executeCommand(['./YamlValidator', '-c', continueOnError, '-p',
                                     "${System.properties['user.dir']}/src/test/resources"])

        then:
        output[0] == 1
        output[1].count('is invalid') == invalidCount

        where:
        continueOnError || invalidCount
        'true'          || 2
        'false'         || 1
    }

    @Unroll
    def 'test yaml validator - #folderName'() {
        when:
        def output = executeCommand(['./YamlValidator', '--debug', 'false', '-p',
                                     "${System.properties['user.dir']}/src/test/resources/data/${folderName}"])

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
                "/config.yml' is valid",
                "/multi-doc.yml' is invalid",
                "/anchor.yml' is invalid"
        ]
    }

    @Unroll
    def 'test yaml validator with debug enabled - #folderName'() {
        when:
        def output = executeCommand(['./YamlValidator', '--debug', 'true', '-p',
                                     "${System.properties['user.dir']}/src/test/resources/data/${folderName}"])

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
                "/config.yml' is valid",
                "/multi-doc.yml' is invalid",
                "/anchor.yml' is invalid"
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
