## Config

The following parameters can be set to configure the plugin.

* **debug** - Flag to enable debug logs. Optional, by default, debug logs are disabled
* **continue_on_error** - Flag to indicate if processing should continue when an invalid file is encountered. Optional, 
defaults to true
* **search_path** - If specified, only YAMLs present in this path will be validated

## Examples
### vela

```yaml
steps:
  - name: yaml_validator
    ruleset:
      branch: master
      event: push
    image: devatherock/vela-yaml-validator:1.2.0
    parameters:
      debug: false
      continue_on_error: false
```

### CircleCI

```yaml
version: 2.1
jobs:
  validate_yamls:
    docker:
      - image: devatherock/vela-yaml-validator:1.2.0
    working_directory: ~/my-repo
    environment:
      PARAMETER_DEBUG: false
      PARAMETER_CONTINUE_ON_ERROR: false
    steps:
      - checkout
      - run: sh /scripts/entry-point.sh
```