/*
 * @(#) JSONValidation.java
 *
 * json-validation  Validation functions for JSON Schema validation
 * Copyright (c) 2020, 2021, 2022, 2024, 2025 Peter Wall
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.jstuff.java.validation;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Static functions to perform data validations, principally for JSON Schema format validation.
 *
 * <p>The original requirements are taken from the
 * <a href="https://json-schema.org/draft/2019-09/json-schema-validation.html#rfc.section.7.3">JSON Schema
 * Validation</a> specification.</p>
 *
 * <p>There are also additional validations for two unofficial types {@code local-date-time} and {@code local-time};
 * these allow for validation of date-time or time strings that do not include time zone information.</p>
 *
 * <p>Also included are {@link #isLeapYear(int)} and {@link #monthLength(int, int)} functions; these are required for
 * date checking and it costs nothing to make them available for general use.</p>
 *
 * @author  Peter Wall
 */
public class JSONValidation {

    /** Table of implemented validations. */
    public static Map<String, Predicate<String>> validations = new HashMap<>();

    static {
        validations.put("date-time", JSONValidation::isDateTime);
        validations.put("date", JSONValidation::isDate);
        validations.put("time", JSONValidation::isTime);
        validations.put("duration", JSONValidation::isDuration);
        validations.put("uri", JSONValidation::isURI);
        validations.put("uri-reference", JSONValidation::isURIReference);
        validations.put("uri-template", JSONValidation::isURITemplate);
        validations.put("uuid", JSONValidation::isUUID);
        validations.put("hostname", JSONValidation::isHostname);
        validations.put("email", JSONValidation::isEmail);
        validations.put("ipv4", JSONValidation::isIPV4);
        validations.put("ipv6", JSONValidation::isIPV6);
        validations.put("json-pointer", JSONValidation::isJSONPointer);
        validations.put("relative-json-pointer", JSONValidation::isRelativeJSONPointer);
        validations.put("regex", JSONValidation::isRegex);
        // not yet implemented: idn-email, idn-hostname, iri, iri-reference
    }

    private static final int[] monthLengths = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    /**
     * Determine whether a given year is a leap year (only meaningful for years covered by the Gregorian calendar).
     *
     * @param   year    the year
     * @return          {@code true} if the year is a leap year
     */
    public static boolean isLeapYear(int year) {
        return (year & 3) == 0 && ((year % 100) != 0 || (year % 400) == 0);
    }

    /**
     * Calculate the length of a given month (only meaningful for years covered by the Gregorian calendar).
     *
     * @param   year    the year
     * @param   month   the month (must be in the range 1 to 12)
     * @return          the length of the month in days
     * @throws  IllegalArgumentException if the month is not in the range 1 to 12
     */
    public static int monthLength(int year, int month) {
        try {
            return month == 2 && isLeapYear(year) ? 29 : monthLengths[month - 1];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Month value incorrect - " + month);
        }
    }

    /**
     * Test for conformity to the {@code date-time} format type.  A string is valid if it conforms to the
     * {@code date-time} production in
     * <a href="https://tools.ietf.org/html/rfc3339#section-5.6">RFC 3339, section 5.6</a>.
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isDateTime(CharSequence string) {
        if (string == null)
            return false;
        if (string.length() < 19)
            return false;
        if (!checkDate(string))
            return false;
        char ch = string.charAt(10);
        if (ch != 'T' && ch != 't')
            return false;
        return checkTime(string, 11);
    }

    /**
     * Test for conformity to an unofficial {@code local-date-time} type (not part of the JSON Schema Validation
     * Specification).  A string is valid if it conforms to a new production {@code [full-date "T" partial-time]} based
     * on <a href="https://tools.ietf.org/html/rfc3339#section-5.6">RFC 3339, section 5.6</a>.
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isLocalDateTime(CharSequence string) {
        if (string == null)
            return false;
        int n = string.length();
        if (n < 19)
            return false;
        if (!checkDate(string))
            return false;
        char ch = string.charAt(10);
        if (ch != 'T' && ch != 't')
            return false;
        return checkLocalTime(string, 11) == n;
    }

    /**
     * Test for conformity to the {@code date} format type.  A string is valid if it conforms to the {@code full-date}
     * production in <a href="https://tools.ietf.org/html/rfc3339#section-5.6">RFC 3339, section 5.6</a>.
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isDate(CharSequence string) {
        if (string == null)
            return false;
        if (string.length() != 10)
            return false;
        return checkDate(string);
    }

    /**
     * Test for conformity to the {@code time} format type.  A string is valid if it conforms to the {@code full-time}
     * production in <a href="https://tools.ietf.org/html/rfc3339#section-5.6">RFC 3339, section 5.6</a>.
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isTime(CharSequence string) {
        if (string == null)
            return false;
        return checkTime(string, 0);
    }

    /**
     * Test for conformity to an unofficial {@code local-time} type (not part of the JSON Schema Validation
     * Specification).  A string is valid if it conforms to the {@code partial-time} production in
     * <a href="https://tools.ietf.org/html/rfc3339#section-5.6">RFC 3339, section 5.6</a>.
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isLocalTime(CharSequence string) {
        if (string == null)
            return false;
        return checkLocalTime(string, 0) == string.length();
    }

    private static boolean checkDate(CharSequence string) {
        int i = 0;

        // year

        char ch = string.charAt(i++);
        if (!isDigit(ch))
            return false;
        int year = (ch - '0') * 1000;
        ch = string.charAt(i++);
        if (!isDigit(ch))
            return false;
        year += (ch - '0') * 100;
        ch = string.charAt(i++);
        if (!isDigit(ch))
            return false;
        year += (ch - '0') * 10;
        ch = string.charAt(i++);
        if (!isDigit(ch))
            return false;
        year += ch - '0';
        if (string.charAt(i++) != '-')
            return false;

        // month

        ch = string.charAt(i++);
        if (!isDigit(ch))
            return false;
        int month = (ch - '0') * 10;
        ch = string.charAt(i++);
        if (!isDigit(ch))
            return false;
        month += ch - '0';
        if (string.charAt(i++) != '-')
            return false;
        if (month == 0 || month > 12)
            return false;

        // day

        ch = string.charAt(i++);
        if (!isDigit(ch))
            return false;
        int day = (ch - '0') * 10;
        ch = string.charAt(i);
        if (!isDigit(ch))
            return false;
        day += ch - '0';
        return day > 0 && day <= monthLength(year, month);
    }

    private static int checkLocalTime(CharSequence string, int start) {
        int n = string.length();
        if (start + 8 > n)
            return -1;
        int i = start;

        // hours

        char ch = string.charAt(i++);
        if (!isDigit(ch))
            return -1;
        int hour = (ch - '0') * 10;
        ch = string.charAt(i++);
        if (!isDigit(ch))
            return -1;
        hour += ch - '0';
        if (hour > 23)
            return -1;
        if (string.charAt(i++) != ':')
            return -1;

        // minutes

        ch = string.charAt(i++);
        if (!isDigit(ch))
            return -1;
        int minute = (ch - '0') * 10;
        ch = string.charAt(i++);
        if (!isDigit(ch))
            return -1;
        minute += ch - '0';
        if (minute > 59)
            return -1;
        if (string.charAt(i++) != ':')
            return -1;

        // seconds

        ch = string.charAt(i++);
        if (!isDigit(ch))
            return -1;
        int second = (ch - '0') * 10;
        ch = string.charAt(i++);
        if (!isDigit(ch))
            return -1;
        second += ch - '0';
        if (!(second <= 59 || minute == 59 && second == 60)) // leap second valid, but probably best avoided
            return -1;

        // fractional seconds

        if (i >= n)
            return i;
        ch = string.charAt(i++);
        if (ch != '.')
            return i - 1;
        if (i >= n)
            return -1;
        ch = string.charAt(i++);
        if (!isDigit(ch))
            return -1;
        do {
            if (i >= n)
                return i;
            ch = string.charAt(i++);
        } while (isDigit(ch));
        return i - 1;
    }

    private static boolean checkTime(CharSequence string, int start) {
        int n = string.length();
        int i = checkLocalTime(string, start);
        if (i < 0 || i >= n)
            return false;
        char ch = string.charAt(i++);

        // offset

        if (ch == 'Z' || ch == 'z')
            return i == n;
        if (ch != '+' && ch != '-')
            return false;
        if (i + 5 != n)
            return false;

        // offset hours

        ch = string.charAt(i++);
        if (!isDigit(ch))
            return false;
        int hour = (ch - '0') * 10;
        ch = string.charAt(i++);
        if (!isDigit(ch))
            return false;
        hour += ch - '0';
        if (hour > 23)
            return false;
        if (string.charAt(i++) != ':')
            return false;

        // offset minutes

        ch = string.charAt(i++);
        if (!isDigit(ch))
            return false;
        int minute = (ch - '0') * 10;
        ch = string.charAt(i);
        if (!isDigit(ch))
            return false;
        minute += ch - '0';
        return minute <= 59;
    }

    /**
     * Test for conformity to the {@code duration} format type.  A string is valid if it conforms to the
     * {@code duration} production in <a href="https://tools.ietf.org/html/rfc3339#appendix-A">RFC 3339, Appendix A</a>.
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isDuration(CharSequence string) {
        if (string == null)
            return false;
        int n = string.length();
        if (n == 0)
            return false;
        int i = 0;
        if (string.charAt(i++) != 'P')
            return false;
        char previous = '\0';
        while (true) {
            if (i == n)
                return previous != '\0';
            char ch = string.charAt(i++);
            if (ch == 'T')
                break;
            if (!isDigit(ch))
                return false;
            do {
                if (i == n)
                    return false;
                ch = string.charAt(i++);
            } while (isDigit(ch));
            if (previous == '\0' && ch == 'W')
                return i == n;
            if (previous == '\0' && !(ch == 'Y' || ch == 'M' || ch == 'D') ||
                    previous == 'Y' && ch != 'M' ||
                    previous == 'M' && ch != 'D')
                return false;
            previous = ch;
        }
        previous = '\0';
        while (true) {
            if (i == n)
                return previous != '\0';
            char ch = string.charAt(i++);
            if (!isDigit(ch))
                return false;
            do {
                if (i == n)
                    return false;
                ch = string.charAt(i++);
            } while (isDigit(ch));
            if (previous == '\0' && !(ch == 'H' || ch == 'M' || ch == 'S') ||
                    previous == 'H' && ch != 'M' ||
                    previous == 'M' && ch != 'S')
                return false;
            previous = ch;
        }
    }

    /**
     * Test for conformity to the {@code uri} format type.  A string is valid if it conforms to
     * <a href="https://tools.ietf.org/html/rfc3986">RFC 3986</a>.
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isURI(CharSequence string) {
        if (string == null)
            return false;
        try {
            URI uri = new URI(string.toString());
            if (uri.getScheme() == null)
                return false;
        }
        catch (URISyntaxException ignored) {
            return false;
        }
        return true;
    }

    /**
     * Test for conformity to the {@code uri-reference} format type.  A string is valid if it conforms to
     * <a href="https://tools.ietf.org/html/rfc3986">RFC 3986</a> (either a URI or a relative-reference).
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isURIReference(CharSequence string) {
        if (string == null)
            return false;
        URI baseURI = URI.create("http://pwall.net/schema/schema.json");
        try {
            //noinspection ResultOfMethodCallIgnored
            baseURI.resolve(string.toString());
        }
        catch (IllegalArgumentException ignored) {
            return false;
        }
        return true;
    }

    /**
     * Test for conformity to the {@code uri-template} format type.  A string is valid if it conforms to
     * <a href="https://www.rfc-editor.org/rfc/rfc6570.html">RFC 6570</a>.
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isURITemplate(CharSequence string) {
        if (string == null)
            return false;
        int n = string.length();
        int i = 0;
        while (i < n) {
            char ch = string.charAt(i++);
            if (ch == '%') {
                if (i + 2 > n || !isHexDigit(string.charAt(i)) || !isHexDigit(string.charAt(i + 1)))
                    return false;
                i += 2;
            }
            else if (ch == '{') {
                if (i >= n)
                    return false;
                ch = string.charAt(i++);
                if (ch == '+' || ch == '#' || ch == '.' || ch == '/' || ch == ';' || ch == '?' || ch == '&') {
                    if (i >= n)
                        return false;
                    ch = string.charAt(i++);
                }
                while (true) { // loop for each variable in a comma-separated list of variables
                    while (true) { // loop for each component of a dot-separated variable name
                        int varStart = i;
                        while (true) { // loop for each character or percent sequence in a variable name component
                            if (ch == '%') {
                                if (i + 2 > n || !isHexDigit(string.charAt(i)) || !isHexDigit(string.charAt(i + 1)))
                                    return false;
                                i += 2;
                            }
                            else if (!isValidURITemplateVariableChar(ch))
                                break;
                            if (i >= n)
                                return false;
                            ch = string.charAt(i++);
                        }
                        if (i == varStart)
                            return false;
                        if (ch != '.')
                            break;
                        if (i >= n)
                            return false;
                        ch = string.charAt(i++);
                    }
                    boolean starSeen = false;
                    if (ch == '*') {
                        if (i >= n)
                            return false;
                        ch = string.charAt(i++);
                        starSeen = true;
                    }
                    if (ch == ':') {
                        if (i >= n)
                            return false;
                        ch = string.charAt(i++);
                        int numberStart = i;
                        while (isDigit(ch)) {
                            if (i >= n)
                                return false;
                            ch = string.charAt(i++);
                        }
                        if (i == numberStart)
                            return false;
                    }
                    if (!starSeen && ch == '*') {
                        if (i >= n)
                            return false;
                        ch = string.charAt(i++);
                    }
                    if (ch != ',')
                        break;
                    if (i >= n)
                        return false;
                    ch = string.charAt(i++);
                }
                if (ch != '}')
                    return false;
            }
            else if (!isValidURITemplateTextChar(ch)) {
                return false;
            }
        }
        return true;
    }

    private static final int uuidDash1 = 8;
    private static final int uuidDash2 = uuidDash1 + 1 + 4;
    private static final int uuidDash3 = uuidDash2 + 1 + 4;
    private static final int uuidDash4 = uuidDash3 + 1 + 4;
    private static final int uuidLength = uuidDash4 + 1 + 12;

    /**
     * Test for conformity to the {@code uuid} format type.  A string is valid if it conforms to
     * <a href="https://tools.ietf.org/html/rfc4122">RFC 4122</a>.
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isUUID(CharSequence string) {
        if (string == null || string.length() != uuidLength)
            return false;
        int i = 0;
        do {
            if (!isHexDigit(string.charAt(i++)))
                return false;
        } while (i < uuidDash1);
        if (string.charAt(i++) != '-')
            return false;
        do {
            if (!isHexDigit(string.charAt(i++)))
                return false;
        } while (i < uuidDash2);
        if (string.charAt(i++) != '-')
            return false;
        do {
            if (!isHexDigit(string.charAt(i++)))
                return false;
        } while (i < uuidDash3);
        if (string.charAt(i++) != '-')
            return false;
        do {
            if (!isHexDigit(string.charAt(i++)))
                return false;
        } while (i < uuidDash4);
        if (string.charAt(i++) != '-')
            return false;
        do {
            if (!isHexDigit(string.charAt(i++)))
                return false;
        } while (i < uuidLength);
        return true;
    }

    /**
     * Test for conformity to the {@code hostname} format type.  A string is valid if it conforms to
     * <a href="https://tools.ietf.org/html/rfc1123#section-2.1">RFC 1123, section 2.1</a>.
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isHostname(CharSequence string) {
        if (string == null)
            return false;
        return isHostname(string, 0);
    }

    private static boolean isHostname(CharSequence string, int i) {
        boolean nextMayBeDashOrDot = false;
        int n = string.length();
        while (i < n) {
            char ch = string.charAt(i++);
            if (nextMayBeDashOrDot) {
                if (ch == '-' || ch == '.')
                    nextMayBeDashOrDot = false;
                else if (!isLetterOrDigit(ch))
                    return false;
            }
            else {
                if (!isLetterOrDigit(ch))
                    return false;
                nextMayBeDashOrDot = true;
            }
        }
        return nextMayBeDashOrDot;
    }

    /**
     * Test for conformity to the {@code email} format type.
     *
     * <p>Validation of email addresses is difficult, largely because the specification in
     * <a href="https://www.ietf.org/rfc/rfc5322.html">RFC 5322</a> makes reference to earlier &ldquo;obsolete&rdquo;
     * forms of email addresses that are expected to be accepted as valid.  This function does not attempt to cover the
     * entire range of obsolete addresses; instead, it implements a form of validation derived from the regular
     * expression at the web site <a href="http://emailregex.com/">{@code emailregex.com}</a> for the
     * &ldquo;local-part&rdquo; (the addressee or mailbox name), and it uses the hostname validation from
     * <a href="https://tools.ietf.org/html/rfc1123">RFC 1123</a> for the &ldquo;domain&rdquo;.</p>
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isEmail(CharSequence string) {
        if (string == null)
            return false;
        int n = string.length();
        if (n == 0)
            return false;
        char ch = string.charAt(0);
        int i = 0;
        if (ch == '"') {
            while (true) {
                if (++i >= n)
                    return false;
                ch = string.charAt(i);
                if (ch == '"')
                    break;
                if (ch == '\\') {
                    if (++i >= n)
                        return false;
                    ch = string.charAt(i);
                    if (!(inRange(ch, 1, 9) || ch == 0xB || ch == 0xC || inRange(ch, 0xE, 0x7F)))
                        return false;
                }
                else {
                    if (!(inRange(ch, 1, 8) || ch == 0xB || ch == 0xC || inRange(ch, 0xE, 0x1F) || ch == 0x21 ||
                            inRange(ch, 0x23, 0x5B) || inRange(ch, 0x5D, 0x7F)))
                        return false;
                }
            }
            if (++i >= n || string.charAt(i) != '@')
                return false;
        }
        else {
            boolean nextMayBeDot = false;
            do {
                if (nextMayBeDot) {
                    if (ch == '.')
                        nextMayBeDot = false;
                    else if (!isEmailNameChar(ch))
                        return false;
                }
                else {
                    if (!isEmailNameChar(ch))
                        return false;
                    nextMayBeDot = true;
                }
                if (++i >= n)
                    return false;
                ch = string.charAt(i);
            } while (ch != '@');
            if (!nextMayBeDot)
                return false;
        }
        return isHostname(string, i + 1);
    }

    /**
     * Test for conformity to the {@code ipv4} format type.  A string is valid if it conforms to
     * <a href="https://tools.ietf.org/html/rfc2673#section-3.2">RFC 2673, section 3.2</a>.
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isIPV4(CharSequence string) {
        if (string == null)
            return false;
        return isIPV4(string, 0);
    }

    private static boolean isIPV4(CharSequence string, int i) {
        int n = string.length();
        int j = 0;
        while (true) {
            if (i >= n)
                return false;
            char ch = string.charAt(i++);
            if (!isDigit(ch))
                return false;
            int k = 1;
            int m = ch - '0';
            while (i < n && isDigit(ch = string.charAt(i))) {
                if (m == 0)
                    return false;
                i++;
                m = m * 10 + ch - '0';
                if (m > 255)
                    return false;
                if (++k == 3)
                    break;
            }
            if (++j == 4)
                break;
            if (i >= n || string.charAt(i++) != '.')
                return false;
        }
        return i == n;
    }

    /**
     * Test for conformity to the {@code ipv6} format type.  A string is valid if it conforms to
     * <a href="https://tools.ietf.org/html/rfc4291#section-2.2">RFC 4291, section 2.2</a>.
     *
     * <p><b>NOTE:</b> The
     * <a href="https://json-schema.org/draft/2019-09/json-schema-validation.html">JSON Schema Validation</a>
     * specification says (&sect; 7.3.4) that a string conforming to the {@code ipv6} format must be an &ldquo;IPv6
     * address as defined in <a href="https://tools.ietf.org/html/rfc4291">RFC 4291, section 2.2</a>&rdquo;.  Subsequent
     * to RFC 4291, <a href="https://tools.ietf.org/html/rfc5952">RFC 5952</a> recommended tighter restrictions on the
     * representation of IPV6 addresses, including mandating the use of lower case for all alpha characters, and
     * prohibiting the use of &ldquo;{@code ::}&rdquo; to compress a single zero 16-bit field.  Because the JSON Schema
     * Validation specification refers only to RFC 4291, not RFC 5952, this function does not implement the tighter
     * restrictions of the later document.</p>
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isIPV6(CharSequence string) {
        if (string == null)
            return false;
        int n = string.length();
        if (n == 0)
            return false;
        int i = 0;
        int j = 0;
        boolean doubleColonSeen = false;
        if (string.charAt(0) == ':') {
            if (string.charAt(1) != ':')
                return false;
            if (n == 2)
                return true;
            doubleColonSeen = true;
            i = 2;
            j = 1;
        }
        while (true) {
            int k = 0;
            while (i < n && isHexDigit(string.charAt(i))) {
                i++;
                if (++k == 4)
                    break;
            }
            if (k == 0)
                return false;
            if (++j == 8)
                break;
            if (i >= n)
                return doubleColonSeen;
            char ch = string.charAt(i);
            if (ch != ':') {
                if ((j == 7 || j < 7 && doubleColonSeen) && ch == '.') {
                    do {
                        --i;
                    } while (string.charAt(i) != ':');
                    return isIPV4(string, i + 1);
                }
                return false;
            }
            i++;
            if (!doubleColonSeen && i < n && string.charAt(i) == ':') {
                if (++i == n)
                    return true;
                doubleColonSeen = true;
                j++;
            }
        }
        return i == n;
    }

    /**
     * Test for conformity to the {@code json-pointer} format type.  A string is valid if it conforms to
     * <a href="https://tools.ietf.org/html/rfc6901#section-5">RFC 6901, section 5</a>.
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isJSONPointer(CharSequence string) {
        if (string == null)
            return false;
        return isJSONPointer(string, 0);
    }

    private static boolean isJSONPointer(CharSequence string, int i) {
        int n = string.length();
        if (i >= n)
            return true;
        if (string.charAt(i++) != '/')
            return false;
        while (i < n) {
            if (string.charAt(i++) == '~') {
                if (!(i < n && inRange(string.charAt(i++), '0', '1')))
                    return false;
            }
        }
        return true;
    }

    /**
     * Test for conformity to the {@code relative-json-pointer} format type.  A string is valid if it conforms to
     * <a href="https://json-schema.org/draft/2020-12/relative-json-pointer.html">Relative JSON Pointers</a>.
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isRelativeJSONPointer(CharSequence string) {
        if (string == null)
            return false;
        int n = string.length();
        int i = 0;
        if (i == n)
            return false;
        char ch = string.charAt(i++);
        if (ch == '0') {
            if (i >= n)
                return true;
            ch = string.charAt(i++);
        }
        else {
            if (!isDigit(ch))
                return false;
            do {
                if (i >= n)
                    return true;
                ch = string.charAt(i++);
            } while (isDigit(ch));
        }
        if (ch == '+' || ch == '-') {
            if (i >= n)
                return false;
            ch = string.charAt(i++);
            if (ch == '0') {
                if (i >= n)
                    return true;
                ch = string.charAt(i++);
            }
            else {
                if (!isDigit(ch))
                    return false;
                do {
                    if (i >= n)
                        return true;
                    ch = string.charAt(i++);
                } while (isDigit(ch));
            }
        }
        return ch == '#' && i == n || isJSONPointer(string, i - 1);
    }

    /**
     * Test for conformity to the {@code regex} format type.  A string is valid if it conforms to the
     * <a href="https://www.ecma-international.org/ecma-262/11.0">ECMA 262</a> regular expression dialect.
     *
     * <p>Since the Java {@link Pattern} class used here implements a dialect very close to, but not identical to the
     * ECMA 262 variant, it is possible that in rare cases there may be subtle inconsistencies in the results of this
     * function.</p>
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isRegex(CharSequence string) {
        if (string == null)
            return false;
        try {
            Pattern.compile(string.toString());
        }
        catch (PatternSyntaxException ignore) {
            return false;
        }
        return true;
    }

    private static boolean inRange(char ch, int lo, int hi) {
        return ch >= lo && ch <= hi;
    }

    private static boolean isLetterOrDigit(char ch) {
        return inRange(ch, 'a', 'z') || inRange(ch, 'A', 'Z') || isDigit(ch);
    }

    private static boolean isHexDigit(char ch) {
        return isDigit(ch) || inRange(ch, 'a', 'f') || inRange(ch, 'A', 'F');
    }

    private static boolean isDigit(char ch) {
        return inRange(ch, '0', '9');
    }

    private static final String emailSpecialChars = "!#$%&'*+/=?^_`{|}~-";

    private static boolean isEmailNameChar(char ch) {
        return isLetterOrDigit(ch) || emailSpecialChars.indexOf(ch) >= 0;
    }

    private static final String uriTemplateIllegalChars = "\"%'<>\\^`|}";

    private static boolean isValidURITemplateTextChar(char ch) {
        return !(inRange(ch, 0, 0x20) || uriTemplateIllegalChars.indexOf(ch) >= 0 || inRange(ch, 0x7F, 0xBF));
    }

    private static boolean isValidURITemplateVariableChar(char ch) {
        return isLetterOrDigit(ch) || ch == '_';
    }

}
