## Config

The following parameters can be set to configure the plugin.

* **debug** - Flag to enable debug logs. Optional, by default, debug logs are disabled

## Examples
### drone

```yaml
pipeline:
  yaml_validator:
    when:
      branch: master
      event: push
    image: devatherock/drone-yaml-validator:1.0.1
    debug: true
```

### vela

```yaml
steps:
  - name: yaml_validator
    ruleset:
      branch: master
      event: push
    image: devatherock/vela-yaml-validator:1.0.1
    parameters:
      debug: true
```