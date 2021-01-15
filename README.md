# json-validation

[![Build Status](https://travis-ci.org/pwall567/json-validation.svg?branch=main)](https://travis-ci.org/pwall567/json-validation)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Maven Central](https://img.shields.io/maven-central/v/net.pwall.json/json-validation?label=Maven%20Central)](https://search.maven.org/search?q=g:%22net.pwall.json%22%20AND%20a:%22json-validation%22)

Validation functions for JSON Schema validation

## Background

This library performs the validations required for some of the JSON Schema `format` validations as defined in
[JSON Schema Validation](https://json-schema.org/draft/2019-09/json-schema-validation.html) Section 7.3.

The functions are provided in the form of a simple library to simplify their use in code generated from JSON Schema
specification files.

Not all of the functions defined in the specification are included in the initial version; more may be added in due
course.

## Quick Start

The validations are static functions, and they all take a single parameter, the value to be tested.
They return `true` if the value matches the requirements of the specification.

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

## Dependency Specification

The latest version of the library is 1.1, and it may be obtained from the Maven Central repository.

### Maven
```xml
    <dependency>
      <groupId>net.pwall.json</groupId>
      <artifactId>json-validation</artifactId>
      <version>1.1</version>
    </dependency>
```
### Gradle
```groovy
    testImplementation 'net.pwall.json:json-validation:1.1'
```
### Gradle (kts)
```kotlin
    testImplementation("net.pwall.json:json-validation:1.1")
```

Peter Wall

2021-01-16
