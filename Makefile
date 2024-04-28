docker_tag=latest
java_to_native_version=3.0.0
cache_dir=~

clean:
	./gradlew clean
test:
	./gradlew spotlessApply test --tests '*YamlValidatorSpec*'
coveralls:
	./gradlew spotlessCheck test --tests '*YamlValidatorSpec*' coveralls
functional-test:
	DOCKER_TAG=$(docker_tag) ./gradlew test --tests '*YamlValidatorDockerSpec*' -x jacocoTestCoverageVerification
jar-build:
	docker run --rm \
	-v $(CURDIR):/work \
	-w=/work \
	-e PARAMETER_SCRIPT_PATH=YamlValidator.groovy \
	-e PARAMETER_OUTPUT_FILE=build/native/libs/YamlValidator.jar \
	-e PARAMETER_STATIC_COMPILE=true \
	devatherock/scriptjar:2.0.0
binary-build:
	docker run --rm \
	-v $(CURDIR):/work \
	-w=/work \
	--entrypoint='' \
	-e PLUGIN_JAR_NAME=build/native/libs/YamlValidator.jar \
	-e PLUGIN_CONFIG_FILE=config/graal.yml \
	devatherock/java-to-native:$(java_to_native_version) sh /scripts/entry-point.sh && upx -4 YamlValidator
docker-build:
	docker build -t devatherock/drone-yaml-validator:$(docker_tag) .
image-scan:
	docker run --rm \
	-v $(cache_dir):/cache \
	-v /var/run/docker.sock:/var/run/docker.sock \
	--entrypoint='' \
	aquasec/trivy:0.49.1 \
	trivy --cache-dir /cache \
		image devatherock/drone-yaml-validator:$(docker_tag) --ignore-unfixed
