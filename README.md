[![CircleCI](https://circleci.com/gh/devatherock/drone-yaml-validator.svg?style=svg)](https://circleci.com/gh/devatherock/drone-yaml-validator)
[![Docker Pulls - Vela](https://img.shields.io/docker/pulls/devatherock/vela-yaml-validator.svg)](https://hub.docker.com/r/devatherock/vela-yaml-validator/)
[![Docker Image Size](https://img.shields.io/docker/image-size/devatherock/vela-yaml-validator.svg?sort=date)](https://hub.docker.com/r/devatherock/vela-yaml-validator/)
[![Docker Image Layers](https://img.shields.io/microbadger/layers/devatherock/vela-yaml-validator.svg)](https://microbadger.com/images/devatherock/vela-yaml-validator)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
# drone-yaml-validator
CI plugin to validate yaml files

# Usage
## Docker

```
docker run --rm \
  -e PARAMETER_DEBUG=true \
  -v path/to/yamls:/work \
  -w=/work \
  devatherock/vela-yaml-validator:1.1.2
```

## CircleCI
### On push

```yaml
version: 2.1
jobs:
  validate_yamls:
    docker:
      - image: devatherock/vela-yaml-validator:1.1.2
    working_directory: ~/my-repo
    environment:
      PARAMETER_DEBUG: false                                      # Flag to enable debug logs. Optional, by default, debug logs are disabled
    steps:
      - checkout
      - run: sh /scripts/entry-point.sh

workflows:
  version: 2.1
  yaml_validator:
    jobs:
      - validate_yamls:
          filters:
            branches:
              only: master                                       # Source branch
```

### On tag
Change `filters` section to below:

```yaml
tags:
  only: /^v[0-9\.]+$/       # Regex to match tag pattern
```

## vela
Please refer [docs](DOCS.md)