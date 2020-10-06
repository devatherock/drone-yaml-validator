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
    image: devatherock/vela-yaml-validator:1.1.3
    parameters:
      debug: false
      continue_on_error: false
```