# Changelog

## [Unreleased]
### Changed
- chore(deps): update plugin com.diffplug.spotless to v6.20.0
- chore(deps): update plugin org.owasp.dependencycheck to v8.4.0
- fix(deps): update dependency org.codehaus.groovy:groovy to v3.0.19

## [3.0.0] - 2023-07-07
### Added
- [#91](https://github.com/devatherock/drone-yaml-validator/issues/91): Allowed yamls with custom tags
- Dependency check plugin, to scan for vulnerable dependencies

### Changed
- fix(deps): update dependency org.objenesis:objenesis to v3.3
- feat: Updated changelog updater user
- chore(deps): update dependency gradle to v7
- chore(deps): update plugin com.github.kt3k.coveralls to v2.12.2
- fix(deps): update dependency org.yaml:snakeyaml to v2
- chore(deps): update alpine docker tag to v20230329
- Updated dockerhub readme in CI pipeline
- [#102](https://github.com/devatherock/drone-yaml-validator/issues/102): Built a multi-arch docker image
- Cached build dependencies separately
- Integration tested the arm image
- [#106](https://github.com/devatherock/drone-yaml-validator/issues/106): Built the arm binary in an arm machine
- Upgraded `java-to-native` to `3.0.0`
- [#103](https://github.com/devatherock/drone-yaml-validator/issues/103): Upgraded to `scriptjar` with groovy 3
- chore(deps): update devatherock/simple-slack docker tag to v1
- Made only HIGH bolt vulnerabilities create issues
- chore(deps): update plugin org.owasp.dependencycheck to v8.3.1
- chore(deps): update dependency gradle to v7.6.2

## [2.1.0] - 2022-05-18
### Added
- [#69](https://github.com/devatherock/drone-yaml-validator/issues/69): UPX compression of generated binary

### Changed
- chore: Added changelog-updater for creating missed changelog entries
- chore(deps): update dependency org.codehaus.groovy:groovy to v2.5.16
- chore(deps): update dependency alpine to v20220328
- chore: Used custom ssh key to push to github
- Used docker image with upx in CI pipeline

## [2.0.0] - 2021-10-29
### Added
- Functional tests that can be manually run to verify the dev images before releasing
- test: Used `system-rules` library to test `System.exit` calls
- chore: `Makefile` for easier testing
- test: Added functional tests to CI pipeline. The `machine` executor allows executing docker commands
- chore: Upgraded libraries to fix security vulnerabilities

### Changed
- chore(deps): update dependency org.codehaus.groovy:groovy to v2.5.15

### Removed
- [#58](https://github.com/devatherock/drone-yaml-validator/issues/58): `vela-yaml-validator` publish, so that one image `drone-yaml-validator`, that supports both vela and drone, can be used

## [1.5.0] - 2021-01-25
### Added
- [#28](https://github.com/devatherock/drone-yaml-validator/issues/28): Flag to treat YAML files with duplicate keys as valid.
Defaults to treating such files as invalid

## [1.4.1] - 2020-11-14
### Changed
-   Fixed bug introduced when fixing codacy violations

## [1.4.0] - 2020-11-14
### Changed
-   Fixed `Codacy` violations

## [1.3.0] - 2020-10-25
### Changed
-   Rewrote the code a little for better code coverage

## [1.2.0] - 2020-10-25
### Changed
-   Introduced `test` flag for better unit testing, without killing the java process

## [1.1.5] - 2020-10-24
### Added
-   Coveralls for code coverage
-   Version badge

## [1.1.4] - 2020-10-24
### Added
-   Unit tests

## [1.1.3] - 2020-10-05
### Changed
-   [Issue 9](https://github.com/devatherock/drone-yaml-validator/issues/9) - Parsed all files even after a file fails validation
-   Logged error reason in case of invalid YAML files
-   Enabled auth when pulling from docker registry to avoid docker's rate limiting

## [1.1.2] - 2020-06-21
### Changed
-   Stopped using environment variable `VELA`

## [1.1.1] - 2020-06-20
### Added
-   Exposed a parameter to accept path from which to validate yamls

## [1.1.0] - 2020-06-20
### Added
-   [Issue 7](https://github.com/devatherock/drone-yaml-validator/issues/7): Unit tests to test the docker image

### Changed
-   [Issue 3](https://github.com/devatherock/drone-yaml-validator/issues/3): Converted the jar into a graal native binary

## [1.0.2] - 2020-04-25
### Added
-   Separate copy steps again as the image size reduction was only in local

### Changed
-   Reduced a layer in the image and hence the image size

## [1.0.1] - 2020-04-16
### Added
-   Made the plugin compatible with vela
-   [Issue 2](https://github.com/devatherock/drone-yaml-validator/issues/2): Added `latest` tag
-   [Issue 6](https://github.com/devatherock/drone-yaml-validator/issues/6): Added `docker` badge for vela image

### Changed
-   Upgraded docker image version to `19.03.6`
-   Fixed the `ClassNotFoundException` when running the docker image
-   Modified the script to be graal native friendly

## [1.0.0] - 2018-10-04
### Added
-   Initial version
-   Built the grape cache in advance using `grape install`
-   Packaged the script into an executable jar using `drone-groovy-script-to-jar` plugin
-   Step to check docker build during PR

### Changed
-   Optimized log lines and log format
-   Upgraded docker image version to `17.12.1-ce`