import spock.lang.Specification

/**
 * Test class to test the groovy script
 *
 * @author z0033zk
 */
class YamlValidatorSpec extends Specification {

    def 'test yaml validator'() {
        setup:
        def stdout = System.out
        def stderr = System.err

        and:
        PrintStream testStdout = Mock()
        System.setOut(testStdout)
        System.setErr(testStdout)

        and:
        StringBuilder outputLogBuilder = new StringBuilder()
        StringBuilder outputLogBuilderTwo = new StringBuilder()

        when: 'debug disabled'
        YamlValidator.main(['--debug', 'false', '-p',
                            "${System.properties['user.dir']}/src/test/resources/data/valid"] as String[])

        then:
        (1.._) * testStdout.write(_, _, _) >> { outputLogBuilderTwo.append(new String(it[0])) }
        String outputLogTwo = outputLogBuilderTwo.toString()
        outputLogTwo.contains("/config.yml' is valid")
        outputLogTwo.contains("/multi-doc.yaml' is valid")
        !outputLogTwo.contains('Validating files in')
        !outputLogTwo.contains("Validating '")
        !outputLogTwo.contains('is invalid')

        when: 'debug enabled'
        YamlValidator.main(['--debug', 'true', '-p',
                   "${System.properties['user.dir']}/src/test/resources/data/valid"] as String[])

        then:
        (1.._) * testStdout.write(_, _, _) >> { outputLogBuilder.append(new String(it[0])) }
        String outputLog = outputLogBuilder.toString()
        outputLog.contains("/config.yml' is valid")
        outputLog.contains("/multi-doc.yaml' is valid")
        outputLog.contains('Validating files in')
        outputLog.contains("Validating '")
        !outputLog.contains('is invalid')

        cleanup:
        System.setOut(stdout)
        System.setErr(stderr)
    }
}
