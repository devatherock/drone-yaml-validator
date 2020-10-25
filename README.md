[![CircleCI](https://circleci.com/gh/devatherock/drone-yaml-validator.svg?style=svg)](https://circleci.com/gh/devatherock/drone-yaml-validator)
[![Version](https://img.shields.io/docker/v/devatherock/vela-yaml-validator?sort=date)](https://hub.docker.com/r/devatherock/vela-yaml-validator/)
[![Coverage Status](https://coveralls.io/repos/github/devatherock/drone-yaml-validator/badge.svg?branch=master)](https://coveralls.io/github/devatherock/drone-yaml-validator?branch=master)
[![Docker Pulls - Drone](https://img.shields.io/docker/pulls/devatherock/drone-yaml-validator.svg)](https://hub.docker.com/r/devatherock/drone-yaml-validator/)
[![Docker Image Size](https://img.shields.io/docker/image-size/devatherock/vela-yaml-validator.svg?sort=date)](https://hub.docker.com/r/devatherock/vela-yaml-validator/)
[![Docker Image Layers](https://img.shields.io/microbadger/layers/devatherock/vela-yaml-validator.svg)](https://microbadger.com/images/devatherock/vela-yaml-validator)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
# yaml-validator
CI plugin to validate yaml files

# Usage
## Docker

```
docker run --rm \
  -e PLUGIN_DEBUG=true \
  -v path/to/yamls:/work \
  -w=/work \
  devatherock/drone-yaml-validator:1.1.5
```

## vela/drone/CircleCI
Please refer [docs](DOCS.md)