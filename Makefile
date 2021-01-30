clean:
	./gradlew clean
test:
	./gradlew test --tests '*YamlValidatorSpec*'
functional-test:
	./gradlew test --tests '*YamlValidatorDockerSpec*' -x jacocoTestCoverageVerification