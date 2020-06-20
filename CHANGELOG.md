# Changelog

## [1.1.0] - 2020-06-20
### Added
- [Issue 7](https://github.com/devatherock/drone-yaml-validator/issues/7): Unit tests to test the docker image

### Changed
- [Issue 3](https://github.com/devatherock/drone-yaml-validator/issues/3): Converted the jar into a graal native binary

## [1.0.2] - 2020-04-25
### Added
- Separate copy steps again as the image size reduction was only in local

### Changed
- Reduced a layer in the image and hence the image size

## [1.0.1] - 2020-04-16
### Added
- Made the plugin compatible with vela
- [Issue 2](https://github.com/devatherock/drone-yaml-validator/issues/2): Added `latest` tag
- [Issue 6](https://github.com/devatherock/drone-yaml-validator/issues/6): Added `docker` badge for vela image

### Changed
- Upgraded docker image version to `19.03.6`
- Fixed the `ClassNotFoundException` when running the docker image
- Modified the script to be graal native friendly

## [1.0.0] - 2018-10-04
### Added
- Initial version
- Built the grape cache in advance using `grape install`
- Packaged the script into an executable jar using `drone-groovy-script-to-jar` plugin
- Step to check docker build during PR

### Changed
- Optimized log lines and log format
- Upgraded docker image version to `17.12.1-ce`