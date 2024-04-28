FROM busybox:1.36.1-musl

LABEL maintainer="devatherock@gmail.com"
LABEL io.github.devatherock.version="3.2.0"

COPY entry-point.sh /scripts/
COPY YamlValidator /scripts/yamlvalidator

ENTRYPOINT ["/bin/sh", "/scripts/entry-point.sh"]