# json-validation

[![Build Status](https://github.com/pwall567/json-validation/actions/workflows/build.yml/badge.svg)](https://github.com/pwall567/json-validation/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Maven Central](https://img.shields.io/maven-central/v/io.jstuff/json-validation?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.jstuff%22%20AND%20a:%22json-validation%22)

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
Test for conformity to the `date-time` format type.
A string is valid if it conforms to the `date-time` production in
[RFC 3339, section 5.6](https://tools.ietf.org/html/rfc3339#section-5.6).

### `local-date-time` validation

```java
        boolean valid = JSONValidation.isLocalDateTime(str);
```
Test for conformity to an unofficial `local-date-time` type (not part of the JSON Schema Validation Specification).
A string is valid if it conforms to a new production `[full-date "T" partial-time]` based on
[RFC 3339, section 5.6](https://tools.ietf.org/html/rfc3339#section-5.6).

### `date` validation

```java
        boolean valid = JSONValidation.isDate(str);
```

Test for conformity to the `date` format type.
A string is valid if it conforms to the `full-date` production in
[RFC 3339, section 5.6](https://tools.ietf.org/html/rfc3339#section-5.6).

### `time` validation

```java
        boolean valid = JSONValidation.isTime(str);
```

Test for conformity to the `time` format type.
A string is valid if it conforms to the `full-time` production in
[RFC 3339, section 5.6](https://tools.ietf.org/html/rfc3339#section-5.6).

### `local-time` validation

```java
        boolean valid = JSONValidation.isLocalTime(str);
```

Test for conformity to an unofficial `local-time` type (not part of the JSON Schema Validation Specification).
A string is valid if it conforms to the `partial-time` production in
[RFC 3339, section 5.6](https://tools.ietf.org/html/rfc3339#section-5.6).

### `duration` validation

```java
        boolean valid = JSONValidation.isDuration(str);
```

Test for conformity to the `duration` format type.
A string is valid if it conforms to the `duration` production in
[RFC 3339, Appendix A](https://tools.ietf.org/html/rfc3339#appendix-A).

### `uri` validation

```java
        boolean valid = JSONValidation.isURI(str);
```

Test for conformity to the `uri` format type.
A string is valid if it conforms to [RFC 3986](https://tools.ietf.org/html/rfc3986).

### `uri-reference` validation

```java
        boolean valid = JSONValidation.isURIReference(str);
```

Test for conformity to the `uri-reference` format type.
A string is valid if it conforms to [RFC 3986](https://tools.ietf.org/html/rfc3986) (either a URI or a
relative-reference).

### `uri-template` validation

```java
        boolean valid = JSONValidation.isURI(str);
```

Test for conformity to the `uri-template` format type.
A string is valid if it conforms to [RFC 6570](https://www.rfc-editor.org/rfc/rfc6570.html).

### `uuid` validation

```java
        boolean valid = JSONValidation.isUUID(str);
```

Test for conformity to the `uuid` format type.
A string is valid if it conforms to [RFC 4122](https://tools.ietf.org/html/rfc4122).

### `hostname` validation

```java
        boolean valid = JSONValidation.isHostname(str);
```

Test for conformity to the `hostname` format type.
A string is valid if it conforms to [RFC 1123, section 2.1](https://tools.ietf.org/html/rfc1123#section-2.1).

### `email` validation

```java
        boolean valid = JSONValidation.isEmail(str);
```

Test for conformity to the `email` format type.

Validation of email addresses is difficult, largely because the specification in
[RFC 5322](https://www.ietf.org/rfc/rfc5322.html) makes reference to earlier &ldquo;obsolete&rdquo; forms of email
addresses that are expected to be accepted as valid.
This function does not attempt to cover the entire range of obsolete addresses; instead, it implements a form of
validation derived from the regular expression at the web site [`emailregex.com`](http://emailregex.com/) for the
&ldquo;local-part&rdquo; (the addressee or mailbox name), and it uses the `hostname` validation from
[RFC 1123](https://tools.ietf.org/html/rfc1123) for the &ldquo;domain&rdquo;.

### `ipv4` validation

```java
        boolean valid = JSONValidation.isIPV4(str);
```

Test for conformity to the `ipv4` format type.
A string is valid if it conforms to [RFC 2673, section 3.2](https://tools.ietf.org/html/rfc2673#section-3.2).

### `ipv6` validation

```java
        boolean valid = JSONValidation.isIPV6(str);
```

Test for conformity to the `ipv6` format type.
A string is valid if it conforms to [RFC 4291, section 2.2](https://tools.ietf.org/html/rfc4291#section-2.2).

**NOTE:** The [JSON Schema Validation](https://json-schema.org/draft/2019-09/json-schema-validation.html) specification
says (&sect; 7.3.4) that a string conforming to the `ipv6` format must be an &ldquo;IPv6 address as defined in
[RFC 4291, section 2.2](https://tools.ietf.org/html/rfc4291)&rdquo;.
Subsequent to RFC 4291, [RFC 5952](https://tools.ietf.org/html/rfc5952) recommended tighter restrictions on the
representation of IPV6 addresses, including mandating the use of lower case for all alpha characters, and prohibiting
the use of &ldquo;`::`&rdquo; to compress a single zero 16-bit field.
Because the JSON Schema Validation specification refers only to RFC 4291, not RFC 5952, this function does not implement
the tighter restrictions of the later document.

### `json-pointer` validation

```java
        boolean valid = JSONValidation.isJSONPointer(str);
```

Test for conformity to the `json-pointer` format type.
A string is valid if it conforms to [RFC 6901, section 5](https://tools.ietf.org/html/rfc6901#section-5).

### `relative-json-pointer` validation

```java
        boolean valid = JSONValidation.isRelativeJSONPointer(str);
```

Test for conformity to the `relative-json-pointer` format type.
A string is valid if it conforms to
[Relative JSON Pointers](https://json-schema.org/draft/2020-12/relative-json-pointer.html).

### `regex` validation

```java
        boolean valid = JSONValidation.isRegex(str);
```

Test for conformity to the `regex` format type.
A string is valid if it conforms to the [ECMA 262](https://www.ecma-international.org/ecma-262/11.0) regular expression
dialect.

Since the Java `Pattern` class used here implements a dialect very close to, but not identical to the ECMA 262 variant,
it is possible that in rare cases there may be subtle inconsistencies in the results of this function.

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

The latest version of the library is 3.0, and it may be obtained from the Maven Central repository.

### Maven
```xml
    <dependency>
      <groupId>io.jstuff</groupId>
      <artifactId>json-validation</artifactId>
      <version>3.0</version>
    </dependency>
```
### Gradle
```groovy
    implementation 'io.jstuff:json-validation:3.0'
```
### Gradle (kts)
```kotlin
    implementation("io.jstuff:json-validation:3.0")
```

Peter Wall

2025-01-31
