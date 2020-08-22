# json-validation

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

## Dependency Specification

The latest version of the library is 0.2, and it may be obtained from the Maven Central repository.

### Maven
```xml
    <dependency>
      <groupId>net.pwall.json</groupId>
      <artifactId>json-validation</artifactId>
      <version>0.2</version>
    </dependency>
```
### Gradle
```groovy
    testImplementation 'net.pwall.json:json-validation:0.2'
```
### Gradle (kts)
```kotlin
    testImplementation("net.pwall.json:json-validation:0.2")
```

Peter Wall

2020-08-22
