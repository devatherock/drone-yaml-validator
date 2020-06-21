## Config

The following parameters can be set to configure the plugin.

* **debug** - Flag to enable debug logs. Optional, by default, debug logs are disabled
* **search_path** - If specified, only YAMLs present in this path will be validated

## Examples
### vela

```yaml
steps:
  - name: yaml_validator
    ruleset:
      branch: master
      event: push
    image: devatherock/vela-yaml-validator:1.1.2
    parameters:
      debug: false
```