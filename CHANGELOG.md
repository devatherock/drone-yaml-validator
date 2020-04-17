# Changelog
## 1.0.0
### 2018-09-23
- Initial version

### 2018-09-25
- Optimized log lines and log format

### 2018-09-28
- Build the grape cache in advance using `grape install`

### 2018-09-29
- Packaged the script into an executable jar using `drone-groovy-script-to-jar` plugin

### 2018-10-04
- Step to check docker build during PR
- Upgraded docker image version to `17.12.1-ce`

## 1.0.1
### 2020-04-11
- Made the plugin compatible with vela
- Upgraded docker image version to `19.03.6`

### 2020-04-16
- Fixed the `ClassNotFoundException` when running the docker image
- Modified the script to be graal native friendly
- Added unit tests to test the docker image
- [Issue 2](https://github.com/devatherock/drone-yaml-validator/issues/2): Added `latest` tag
- [Issue 6](https://github.com/devatherock/drone-yaml-validator/issues/6): Added `docker` badge for vela image