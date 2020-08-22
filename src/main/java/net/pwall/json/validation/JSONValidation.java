/*
 * @(#) JSONValidation.java
 *
 * json-validation  Validation functions for JSON Schema validation
 * Copyright (c) 2020 Peter Wall
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

package net.pwall.json.validation;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

/**
 * Static functions to perform validations for JSON Schema format types.
 *
 * @author  Peter Wall
 */
public class JSONValidation {

    /**
     * Test for conformity to the {@code date-time} format type.
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isDateTime(String string) {
        try {
            DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(string);
        }
        catch (Exception ignored) {
            return false;
        }
        return true;
    }

    /**
     * Test for conformity to the {@code date} format type.
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isDate(String string) {
        try {
            DateTimeFormatter.ISO_LOCAL_DATE.parse(string);
        }
        catch (Exception ignored) {
            return false;
        }
        return true;
    }

    /**
     * Test for conformity to the {@code time} format type.
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isTime(String string) {
        try {
            DateTimeFormatter.ISO_OFFSET_TIME.parse(string);
        }
        catch (Exception ignored) {
            return false;
        }
        return true;
    }

    /**
     * Test for conformity to the {@code duration} format type.
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isDuration(String string) {
        try {
            Duration.parse(string);
        }
        catch (Exception ignored) {
            return false;
        }
        return true;
    }

    private static final int uuidLength = 36;
    private static final int[] uuidDashLocations = { 8, 13, 18, 23 };

    /**
     * Test for conformity to the {@code uuid} format type.
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isUUID(String string) {
        if (string == null)
            return false;
        int i = 0;
        int n = string.length();
        if (n != uuidLength)
            return false;
        for (int j = 0; j < 4; j++) {
            int dashLocation = uuidDashLocations[j];
            while (i < dashLocation) {
                if (!isHexDigit(string.charAt(i++)))
                    return false;
            }
            if (string.charAt(i++) != '-')
                return false;
        }
        while (i < uuidLength) {
            if (!isHexDigit(string.charAt(i++)))
                return false;
        }
        return true;
    }

    /**
     * Test for conformity to the {@code hostname} format type.
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isHostname(String string) {
        if (string == null)
            return false;
        return isHostname(string, 0);
    }

    private static boolean isHostname(String string, int i) {
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
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isEmail(String string) {
        if (string == null)
            return false;
        for (int i = 0, n = string.length(); i < n; ) {
            char ch = string.charAt(i++);
            if (ch == '@')
                return i > 0 && isHostname(string, i);
            if (!(isLetterOrDigit(ch) || ch == '-' || ch == '+' || ch == '.' || ch == '_' || ch == '%'))
                return false;
        }
        return false;
    }

    /**
     * Test for conformity to the {@code ipv4} format type.
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isIPV4(String string) {
        if (string == null)
            return false;
        return isIPV4(string, 0);
    }

    private static boolean isIPV4(String string, int i) {
        int n = string.length();
        int j = 0;
        while (true) {
            int k = 0;
            int m = 0;
            char ch;
            while (i < n && isDigit(ch = string.charAt(i))) {
                i++;
                m = m * 10 + ch - '0';
                if (m > 255)
                    return false;
                if (++k == 3)
                    break;
            }
            if (k == 0)
                return false;
            if (++j == 4)
                break;
            if (i >= n || string.charAt(i++) != '.')
                return false;
        }
        return i == n;
    }

    /**
     * Test for conformity to the {@code ipv6} format type.
     *
     * <p><b>NOTE:</b> The
     * <a href="https://json-schema.org/draft/2019-09/json-schema-validation.html">JSON Schema Validation</a>
     * specification says (&sect; 7.3.4) that a string conforming to the {@code ipv6} format must be an "IPv6 address
     * as defined in <a href="https://tools.ietf.org/html/rfc4291">RFC 4291, section 2.2</a>".  Subsequent to RFC 4291,
     * <a href="https://tools.ietf.org/html/rfc5952">RFC 5952</a> recommended stricter restrictions on the
     * representation of IPV6 addresses, including mandating the use of lower case for all alpha characters, and
     * prohibiting the use of "{@code ::}" to compress a single zero 16-bit field.  Because the JSON Schema Validation
     * specification refers only to RFC 4291, not RFC 5952, this function does not implement the stricter restrictions
     * of the later document.</p>
     *
     * @param   string  the string to be tested
     * @return          {@code true} if the string is correct
     */
    public static boolean isIPV6(String string) {
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

    private static boolean isLetterOrDigit(char ch) {
        return ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || isDigit(ch);
    }

    private static boolean isHexDigit(char ch) {
        return isDigit(ch) || ch >= 'a' && ch <= 'f' || ch >= 'A' && ch <= 'F';
    }

    private static boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

}
