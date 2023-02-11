FROM alpine:20230208

LABEL maintainer="devatherock@gmail.com"
LABEL io.github.devatherock.version="2.1.0"

COPY entry-point.sh /scripts/
COPY YamlValidator /scripts/yamlvalidator

ENTRYPOINT ["/bin/sh", "/scripts/entry-point.sh"]