## Config

The following parameters can be set to configure the plugin.

* **debug** - Flag to enable debug logs. Optional, by default, debug logs are disabled

## Examples
### On push

```yaml
pipeline:
  yaml-validator:
    when:
      branch: master
      event: push
    image: devatherock/drone-yaml-validator:1.0.0
    debug: true
```

### On tag

```yaml
pipeline:
  yaml-validator:
    when:
      ref: refs/tags/v*
      event: tag
    image: devatherock/drone-yaml-validator:1.0.0
```