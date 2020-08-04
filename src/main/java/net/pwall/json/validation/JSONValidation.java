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
        if (n != 36)
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
        while (i < 36) {
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

    private static boolean isLetterOrDigit(char ch) {
        return ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch >= '0' && ch <= '9';
    }

    private static boolean isHexDigit(char ch) {
        return ch >= 'a' && ch <= 'f' || ch >= 'A' && ch <= 'F' || ch >= '0' && ch <= '9';
    }

}
