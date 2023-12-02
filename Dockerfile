FROM alpine:3.18.5

LABEL maintainer="devatherock@gmail.com"
LABEL io.github.devatherock.version="3.0.0"

COPY entry-point.sh /scripts/
COPY YamlValidator /scripts/yamlvalidator

ENTRYPOINT ["/bin/sh", "/scripts/entry-point.sh"]