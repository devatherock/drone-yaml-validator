FROM groovy:2.4-alpine

MAINTAINER 'Devaprasadh.Xavier <devatherock@gmail.com>'

ENV PLUGIN_DEBUG false
ADD YamlValidator.groovy /scripts/YamlValidator.groovy

# Command to build the grape cache in advance
RUN groovy /scripts/YamlValidator.groovy --test

ADD entry-point.sh /scripts/entry-point.sh

ENTRYPOINT sh /scripts/entry-point.sh