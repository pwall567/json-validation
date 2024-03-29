# json-validation

[![Build Status](https://travis-ci.com/pwall567/json-validation.svg?branch=main)](https://app.travis-ci.com/github/pwall567/json-validation)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Maven Central](https://img.shields.io/maven-central/v/net.pwall.json/json-validation?label=Maven%20Central)](https://search.maven.org/search?q=g:%22net.pwall.json%22%20AND%20a:%22json-validation%22)

Validation functions for JSON Schema validation

## Background

This library performs the validations required for some of the JSON Schema `format` validations as defined in
[JSON Schema Validation](https://json-schema.org/draft/2020-12/json-schema-validation.html)
[Section 7.3](https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.7.3).

The functions are provided in the form of a standalone library to simplify their use in code generated from JSON Schema
specification files.

Most but not all of the functions defined in the specification are included in the initial version; the remaining few
may be added in due course.

## Quick Start

The validations are static functions, and they all take a single parameter, the string value to be tested.
They return `true` if the value matches the requirements of the specification (they all return `false` if the string is
`null`).

### `date-time` validation

```java
        boolean valid = JSONValidation.isDateTime(str);
```

### `date` validation

```java
        boolean valid = JSONValidation.isDate(str);
```

### `time` validation

```java
        boolean valid = JSONValidation.isTime(str);
```

### `duration` validation

```java
        boolean valid = JSONValidation.isDuration(str);
```

### `email` validation

```java
        boolean valid = JSONValidation.isEmail(str);
```

### `hostname` validation

```java
        boolean valid = JSONValidation.isHostname(str);
```

### `uri` validation

```java
        boolean valid = JSONValidation.isURI(str);
```

### `uri-reference` validation

```java
        boolean valid = JSONValidation.isURIReference(str);
```

### `uuid` validation

```java
        boolean valid = JSONValidation.isUUID(str);
```

### `ipv4` validation

```java
        boolean valid = JSONValidation.isIPV4(str);
```

### `ipv6` validation

```java
        boolean valid = JSONValidation.isIPV6(str);
```

### `json-pointer` validation

```java
        boolean valid = JSONValidation.isJSONPointer(str);
```

### `relative-json-pointer` validation

```java
        boolean valid = JSONValidation.isRelativeJSONPointer(str);
```

### `regex` validation

```java
        boolean valid = JSONValidation.isRegex(str);
```

## Additional Functions

In order to perform date validations, the library contains additional static functions related to dates.
They are made public because there is little reason not to do so, and they may be useful in other contexts.

### `isLeapYear`

This function returns `true` if the given year is a leap year.
It takes a single integer parameter, the year to be checked.
The function uses the rules of the Gregorian calendar, and while there is no limit on the input year value, the result
will be meaningful only in cases where the year is subject to that calendar.
```java
        boolean leap = JSONValidation.isLeapYear(year);
```

### `monthLength`

This function returns the length of a specified month.
It takes two integer parameters, the year and the month (in the range 1 to 12).
As with `isLeapYear`, the function uses the rules of the Gregorian calendar, and the same caveat applies.
```java
        int length = JSONValidation.isLeapYear(year, month);
```

## No Transitive Dependencies

Importantly, the library is entirely self-contained, and the inclusion of this library in a project will not bring in a
long list of transitive dependencies.

## Dependency Specification

The latest version of the library is 1.5, and it may be obtained from the Maven Central repository.

### Maven
```xml
    <dependency>
      <groupId>net.pwall.json</groupId>
      <artifactId>json-validation</artifactId>
      <version>1.5</version>
    </dependency>
```
### Gradle
```groovy
    implementation 'net.pwall.json:json-validation:1.5'
```
### Gradle (kts)
```kotlin
    implementation("net.pwall.json:json-validation:1.5")
```

Peter Wall

2022-11-06
