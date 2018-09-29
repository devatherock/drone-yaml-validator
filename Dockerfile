FROM groovy:2.4-alpine

MAINTAINER 'Devaprasadh.Xavier <devatherock@gmail.com>'

ENV PLUGIN_DEBUG false
ADD YamlValidator.groovy /scripts/YamlValidator.groovy

# Build the grape cache in advance
RUN grape install org.yaml snakeyaml 1.20

ADD entry-point.sh /scripts/entry-point.sh

ENTRYPOINT sh /scripts/entry-point.sh