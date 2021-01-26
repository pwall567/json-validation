# Change Log

The format is based on [Keep a Changelog](http://keepachangelog.com/).

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
