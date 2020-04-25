[![CircleCI](https://circleci.com/gh/devatherock/drone-yaml-validator.svg?style=svg)](https://circleci.com/gh/devatherock/drone-yaml-validator)
[![Docker Pulls - Drone](https://img.shields.io/docker/pulls/devatherock/drone-yaml-validator.svg)](https://hub.docker.com/r/devatherock/drone-yaml-validator/)
[![Docker Pulls - Vela](https://img.shields.io/docker/pulls/devatherock/vela-yaml-validator.svg)](https://hub.docker.com/r/devatherock/vela-yaml-validator/)
[![Docker Image Size](https://img.shields.io/docker/image-size/devatherock/vela-yaml-validator.svg?sort=date)](https://hub.docker.com/r/devatherock/vela-yaml-validator/)
[![Docker Image Layers](https://img.shields.io/microbadger/layers/devatherock/vela-yaml-validator.svg)](https://microbadger.com/images/devatherock/vela-yaml-validator)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
# drone-yaml-validator
drone.io/CircleCI/vela plugin to validate yaml files

# Usage
## CircleCI
### On push

```yaml
version: 2
jobs:
  validate_yamls:
    docker:
      - image: devatherock/drone-yaml-validator:1.0.1
    working_directory: ~/my-repo
    environment:
      PLUGIN_DEBUG: false                                      # Flag to enable debug logs. Optional, by default, debug logs are disabled
    steps:
      - checkout
      - run: sh /scripts/entry-point.sh

workflows:
  version: 2
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

## drone.io or vela
Please refer [docs](DOCS.md)
