import spock.lang.Specification
import spock.lang.Unroll

import java.util.logging.Handler
import java.util.logging.LogRecord
import java.util.logging.Logger
import java.util.logging.Level

/**
 * Test class to test the groovy script
 */
class YamlValidatorSpec extends Specification {
    Handler handler = Mock()

    void setup() {
        System.setProperty('java.util.logging.SimpleFormatter.format', '%5$s%6$s%n')
        Logger.getLogger('').addHandler(handler)
    }

    @Unroll
    void 'test yaml validator - valid files, debug: #debugEnabled'() {
        given:
        StringBuilder outputLogBuilder = new StringBuilder()

        when:
        new YamlValidator().main(['--debug', "${debugEnabled}", '-p',
                                  "${System.properties['user.dir']}/src/test/resources/data/valid"] as String[])

        then:
        (1.._) * handler.publish(!null as LogRecord) >> { outputLogBuilder.append(it.message) }
        String outputLog = outputLogBuilder.toString()
        outputLog.contains("/config.yml' is valid")
        outputLog.contains("/multi-doc.yaml' is valid")
        outputLog.contains('Validating files in')
        outputLog.contains("Validating '")
        !outputLog.contains('is invalid')
        levelInvocations * handler.setLevel(Level.FINE)

        where:
        debugEnabled || levelInvocations
        true         || 1
        false        || 0
    }

    @Unroll
    void 'test yaml validator - invalid files, debug: #debugEnabled'() {
        given:
        StringBuilder outputLogBuilder = new StringBuilder()

        when:
        YamlValidator.main(['--debug', "${debugEnabled}", '-t', 'true', '-p',
                            "${System.properties['user.dir']}/src/test/resources/data/invalid"] as String[])

        then:
        (1.._) * handler.publish(!null as LogRecord) >> { outputLogBuilder.append(it.message) }
        String outputLog = outputLogBuilder.toString()
        outputLog.contains("/multi-doc.yml' is invalid")
        outputLog.contains('Validating files in')
        outputLog.contains("Validating '")
        outputLog.contains('is valid')
        levelInvocations * handler.setLevel(Level.FINE)

        then:
        RuntimeException exception = thrown()
        exception.message == 'Invalid yaml files found'

        where:
        debugEnabled || levelInvocations
        true         || 1
        false        || 0
    }

    @Unroll
    void 'test yaml validator - invalid files, continueOnError: #continueOnError, debug: #debugEnabled'() {
        given:
        StringBuilder outputLogBuilder = new StringBuilder()

        when: 'debug disabled'
        YamlValidator.main(['--debug', "${debugEnabled}", '-t', 'true', '-c', "${continueOnError}", '-p',
                            "${System.properties['user.dir']}/src/test/resources"] as String[])

        then:
        (1.._) * handler.publish(!null as LogRecord) >> { outputLogBuilder.append(it.message) }
        String outputLog = outputLogBuilder.toString()
        outputLog.contains(" is invalid")
        (outputLog.contains("/multi-doc.yml' is invalid") &&
                outputLog.contains("/anchor.yml' is invalid")) == continueOnError
        outputLog.contains('Validating files in')
        outputLog.contains("Validating '")
        levelInvocations * handler.setLevel(Level.FINE)

        then:
        RuntimeException exception = thrown()
        exception.message == 'Invalid yaml files found'

        where:
        debugEnabled | continueOnError || levelInvocations
        true         | true            || 1
        false        | false           || 0
    }

    void 'test yaml validator - duplicate keys allowed'() {
        given:
        StringBuilder outputLogBuilder = new StringBuilder()

        when:
        YamlValidator.main(['-ad', 'true', '-p',
                            "${System.properties['user.dir']}/src/test/resources/data/duplicate"] as String[])

        then:
        (1.._) * handler.publish(!null as LogRecord) >> { outputLogBuilder.append(it.message) }
        String outputLog = outputLogBuilder.toString()
        outputLog.contains("/duplicate-keys.yml' is valid")
    }

    void 'test yaml validator - duplicate keys not allowed'() {
        given:
        StringBuilder outputLogBuilder = new StringBuilder()

        when:
        YamlValidator.main(['-ad', 'false', '-t', 'true', '-p',
                            "${System.properties['user.dir']}/src/test/resources/data/duplicate"] as String[])

        then:
        (1.._) * handler.publish(!null as LogRecord) >> { outputLogBuilder.append(it.message) }
        String outputLog = outputLogBuilder.toString()
        outputLog.contains("/duplicate-keys.yml' is invalid")

        then:
        RuntimeException exception = thrown()
        exception.message == 'Invalid yaml files found'
    }
}
