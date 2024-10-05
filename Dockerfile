FROM busybox:1.37.0-musl

LABEL maintainer="devatherock@gmail.com"
LABEL io.github.devatherock.version="3.3.0"

COPY entry-point.sh /scripts/
COPY YamlValidator /scripts/yamlvalidator

ENTRYPOINT ["/bin/sh", "/scripts/entry-point.sh"]