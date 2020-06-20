## Config

The following parameters can be set to configure the plugin.

* **debug** - Flag to enable debug logs. Optional, by default, debug logs are disabled

## Examples
### vela

```yaml
steps:
  - name: yaml_validator
    ruleset:
      branch: master
      event: push
    image: devatherock/vela-yaml-validator:1.1.0
    parameters:
      debug: false
```