clean:
	./gradlew clean
test:
	./gradlew test --tests '*YamlValidatorSpec*'
coveralls:
	./gradlew test --tests '*YamlValidatorSpec*' coveralls
binary-test:
	./gradlew test --tests '*YamlValidatorBinarySpec*' -x jacocoTestCoverageVerification --no-daemon
functional-test:
	./gradlew test --tests '*YamlValidatorDockerSpec*' -x jacocoTestCoverageVerification --no-daemon