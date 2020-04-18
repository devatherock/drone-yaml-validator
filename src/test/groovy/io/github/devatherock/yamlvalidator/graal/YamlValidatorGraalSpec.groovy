package io.github.devatherock.yamlvalidator.graal

import groovy.util.logging.Log
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

/**
 * Test class to test the built docker images
 *
 * @author z0033zk*
 */
@Log
class YamlValidatorGraalSpec extends Specification {

    def setupSpec() {
        System.setProperty('java.util.logging.SimpleFormatter.format', '%5$s%n')
        Files.createDirectories(Paths.get('build/native/graal-config'))
    }

    def 'test yaml validator'() {
        given:
        String classPath = "build/native/classes"
        String mainClassName = 'YamlValidator'
        String reflectConfigPath = 'build/native/graal-config/'

        and:
        def baseCommand = ['java', "-agentlib:native-image-agent=config-merge-dir=${reflectConfigPath}",
                           '-cp', classPath, '-Dgroovy.grape.enable=false', mainClassName]
        def commandWithArgs = []

        when: 'valid files - debug disabled'
        def output = executeCommand(['java', "-agentlib:native-image-agent=config-output-dir=${reflectConfigPath}",
                                     '-Dgroovy.grape.enable=false', '-cp', classPath, mainClassName,
                                     '--debug', 'false', '--search-path', 'src/test/resources/data/valid'])

        then:
        output[0] == 0
        output[1].contains("config.yml' is valid")
        !output[1].contains("Validating files in '")
        !output[1].contains("Validating '")

        when: 'invalid multi document yaml - debug disabled'
        commandWithArgs.clear()
        commandWithArgs.addAll(baseCommand)
        commandWithArgs.addAll(['--debug', 'false', '--path', 'src/test/resources/data/invalid'])
        output = executeCommand(commandWithArgs)

        then:
        output[0] == 1
        output[1].contains("multi-doc.yml' is invalid")
        !output[1].contains("Validating files in '")
        !output[1].contains("Validating '")

        when: 'invalid single document yaml - debug disabled'
        commandWithArgs.clear()
        commandWithArgs.addAll(baseCommand)
        commandWithArgs.addAll(['--debug', 'false', '--path', 'src/test/resources/data/invalid2'])
        output = executeCommand(commandWithArgs)

        then:
        output[0] == 1
        output[1].contains("anchor.yml' is invalid")
        !output[1].contains("Validating files in '")
        !output[1].contains("Validating '")

        when: 'valid files - debug enabled'
        commandWithArgs.addAll(baseCommand)
        commandWithArgs.addAll(['--debug', 'true', '--path', 'src/test/resources/data/valid'])
        output = executeCommand(commandWithArgs)

        then:
        output[0] == 0
        output[1].contains("config.yml' is valid")
        output[1].contains("Validating files in '")
        output[1].contains("Validating '")

        when: 'invalid multi document yaml - debug enabled'
        commandWithArgs.clear()
        commandWithArgs.addAll(baseCommand)
        commandWithArgs.addAll(['--debug', 'true', '--path', 'src/test/resources/data/invalid'])
        output = executeCommand(commandWithArgs)

        then:
        output[0] == 1
        output[1].contains("multi-doc.yml' is invalid")
        output[1].contains("Validating files in")
        output[1].contains("Validating '")

        when: 'invalid single document yaml - debug enabled'
        commandWithArgs.clear()
        commandWithArgs.addAll(baseCommand)
        commandWithArgs.addAll(['--debug', 'true', '--path', 'src/test/resources/data/invalid2'])
        output = executeCommand(commandWithArgs)

        then:
        output[0] == 1
        output[1].contains("anchor.yml' is invalid")
        output[1].contains("Validating files in '")
        output[1].contains("Validating '")

        when: 'no arguments specified'
        output = executeCommand(baseCommand)

        then:
        output[0] == 1
        output[1].contains(".yml' is invalid")
        !output[1].contains("Validating files in '")
        !output[1].contains("Validating '")

        when: 'no arguments specified'
        commandWithArgs.clear()
        commandWithArgs.addAll(baseCommand)
        commandWithArgs.add('--help')
        output = executeCommand(commandWithArgs)

        then:
        output[0] == 1
        !output[1].contains('valid')
        !output[1].contains("Validating files in '")
        !output[1].contains("Validating '")
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
