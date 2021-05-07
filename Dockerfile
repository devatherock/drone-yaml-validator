FROM alpine:20210212

LABEL maintainer="devatherock@gmail.com"
LABEL io.github.devatherock.version="1.4.1"

COPY entry-point.sh /scripts/
COPY YamlValidator /scripts/yamlvalidator

ENTRYPOINT ["/bin/sh", "/scripts/entry-point.sh"]