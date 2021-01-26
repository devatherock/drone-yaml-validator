## Config

The following parameters can be set to configure the plugin.

*   **debug** - Flag to enable debug logs. Optional, by default, debug logs are disabled

*   **continue_on_error** - Flag to indicate if processing should continue when an invalid file is encountered. Optional, 
defaults to true

*   **search_path** - If specified, only YAMLs present in this path will be validated

*   **allow_duplicate_keys** - Flag to indicate if YAML files with duplicate keys should be considered valid. Optional, 
    defaults to false

## Examples
### vela

```yaml
steps:
  - name: yaml_validator
    ruleset:
      branch: master
      event: push
    image: devatherock/vela-yaml-validator:latest
    parameters:
      debug: false
      continue_on_error: false
      allow_duplicate_keys: true
```

### drone

```yaml
steps:
  - name: yaml_validator
    image: devatherock/drone-yaml-validator:latest
    settings:
      debug: false
      continue_on_error: false
      allow_duplicate_keys: true
```

### CircleCI

```yaml
version: 2.1
jobs:
  validate_yamls:
    docker:
      - image: devatherock/vela-yaml-validator:latest
    working_directory: ~/my-repo
    environment:
      PARAMETER_DEBUG: false
      PARAMETER_CONTINUE_ON_ERROR: false
      PARAMETER_ALLOW_DUPLICATE_KEYS: true
    steps:
      - checkout
      - run: sh /scripts/entry-point.sh
```