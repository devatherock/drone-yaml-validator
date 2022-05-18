DOCKER_TAG=latest

clean:
	./gradlew clean
test:
	./gradlew test --tests '*YamlValidatorSpec*'
coveralls:
	./gradlew test --tests '*YamlValidatorSpec*' coveralls
functional-test:
	./gradlew test --tests '*YamlValidatorDockerSpec*' -x jacocoTestCoverageVerification --no-daemon
jar-build:
	docker run --rm \
	-v $(CURDIR):/work \
	-w=/work \
	-e PARAMETER_SCRIPT_PATH=YamlValidator.groovy \
	-e PARAMETER_OUTPUT_FILE=build/native/libs/YamlValidator.jar \
	-e PARAMETER_STATIC_COMPILE=true \
	devatherock/vela-groovy-script-to-jar:0.7.0
binary-build:
	docker run --rm \
	-v $(CURDIR):/work \
	-w=/work \
	-it --entrypoint='' \
	-e PLUGIN_JAR_NAME=build/native/libs/YamlValidator.jar \
	-e PLUGIN_CONFIG_FILE=config/graal.yml \
	devatherock/java-to-native:1.0.0 sh /scripts/entry-point.sh && upx -4 YamlValidator
docker-build:
	docker build -t devatherock/drone-yaml-validator:$(DOCKER_TAG) .