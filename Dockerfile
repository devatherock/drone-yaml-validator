FROM alpine

LABEL maintainer="devatherock@gmail.com"
LABEL io.github.devatherock.version="1.1.0"

COPY entry-point.sh /scripts/
COPY YamlValidator /scripts/yamlvalidator
RUN chmod 755 /scripts/yamlvalidator

ENTRYPOINT ["sh", "/scripts/entry-point.sh"]