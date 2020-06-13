FROM openjdk:8-jre-alpine

LABEL maintainer="devatherock@gmail.com"
LABEL io.github.devatherock.version="1.0.2"

COPY YamlValidator.jar /scripts/
COPY entry-point.sh /scripts/

ENTRYPOINT ["sh", "/scripts/entry-point.sh"]
