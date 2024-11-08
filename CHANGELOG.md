# Change Log

The format is based on [Keep a Changelog](http://keepachangelog.com/).

## [2.0] - 2024-11-02
### Added
- `build.yml`, `deploy.yml`: converted project to GitHub Actions
### Changed
- updated all functions to take `CharSequence` instead of `String`
- added `validations` table
- reorganised date/time validations, added `isLocalDateTime()`, `isLocalTime()`
- added `isURITemplate()`
- `pom.xml`, tests: reverted from Junit 5 to Junit 4 (more stable)
### Removed
- `.travis.yml`

## [1.5] - 2022-11-06
### Changed
- simplified and improved efficiency (by loop unrolling) of `isUUID`
- `pom.xml`: updated test dependency versions
- corrected reference in JavaDoc
- minor changes to README

## [1.4] - 2021-05-22
### Changed
- fixed bug in `isIPV4` - no longer allows leading zeros
- improved implementation of `isJSONPointer` and `isRelativeJSONPointer`
- added `isRegex`

## [1.3] - 2021-02-25
### Changed
- modified `isEmail` to use validation derived from [emailregex.com](http://emailregex.com/)
- added JavaDoc notes on specifications implemented

## [1.2] - 2021-01-26
### Changed
- added hard-coded validations for `isDateTime`, `isDate` and `isTime`, instead of relying on
`java.time.DateTimeFormatter`
- added `isLeapYear` and `monthLength`
- switched tests to JUnit 5

## [1.1] - 2021-01-16
### Changed
- added hard-coded validation for `isDuration`, instead of relying on `java.time.Duration`
- added `isJSONPointer` and `isRelativeJSONPointer`

## [1.0] - 2020-11-15
### Changed
- `README.md`: added badges
- `pom.xml`: promoted to version 1.0

## [0.2] - 2020-08-22
### Added
- added `isIPV4` and `isIPV6`
- added `uri` and `uri-reference`

## [0.1] - 2020-08-04
### Added
- all files: initial versions
