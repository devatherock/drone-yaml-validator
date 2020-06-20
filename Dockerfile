FROM alpine

LABEL maintainer="devatherock@gmail.com"
LABEL io.github.devatherock.version="1.1.0"

COPY YamlValidator /bin/yamlvalidator
COPY entry-point.sh /scripts/

ENTRYPOINT ["sh", "/scripts/entry-point.sh"]