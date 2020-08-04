/*
 * @(#) JSONValidationTest.java
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

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JSONValidationTest {

    @Test
    public void shouldAcceptValidDateTime() {
        assertTrue(JSONValidation.isDateTime("2020-08-04T20:32:17+10:00"));
        assertTrue(JSONValidation.isDateTime("2020-08-04T10:32:17Z"));
        assertTrue(JSONValidation.isDateTime("2020-08-04T10:32:17.123Z"));
    }

    @Test
    public void shouldRejectInvalidDateTime() {
        assertFalse(JSONValidation.isDateTime("2020-08-34T20:32:17+10:00"));
        assertFalse(JSONValidation.isDateTime("2020-08-04T10:32:17X"));
        assertFalse(JSONValidation.isDateTime("2020-08-04"));
        assertFalse(JSONValidation.isDateTime(""));
        assertFalse(JSONValidation.isDateTime(null));
    }

    @Test
    public void shouldAcceptValidDate() {
        assertTrue(JSONValidation.isDate("2020-08-04"));
        assertTrue(JSONValidation.isDate("1999-12-31"));
        assertTrue(JSONValidation.isDate("2000-01-01"));
        assertTrue(JSONValidation.isDate("2000-02-29"));
    }

    @Test
    public void shouldRejectInvalidDate() {
        assertFalse(JSONValidation.isDate("2020-08-00"));
        assertFalse(JSONValidation.isDate("1999-12-32"));
        assertFalse(JSONValidation.isDate("rubbish"));
        assertFalse(JSONValidation.isDate(""));
        assertFalse(JSONValidation.isDate(null));
    }

    @Test
    public void shouldAcceptValidTime() {
        assertTrue(JSONValidation.isTime("20:04:37+10:00"));
        assertTrue(JSONValidation.isTime("10:04:37Z"));
        assertTrue(JSONValidation.isTime("20:04:37.123+10:00"));
    }

    @Test
    public void shouldRejectInvalidTime() {
        assertFalse(JSONValidation.isTime("20:04:37"));
        assertFalse(JSONValidation.isTime("10:04:66Z"));
        assertFalse(JSONValidation.isTime("rubbish"));
        assertFalse(JSONValidation.isTime(""));
        assertFalse(JSONValidation.isTime(null));
    }

    @Test
    public void shouldAcceptValidDuration() {
        assertTrue(JSONValidation.isDuration("P1D"));
        assertTrue(JSONValidation.isDuration("PT20S"));
        assertTrue(JSONValidation.isDuration("PT2H30M"));
    }

    @Test
    public void shouldRejectInvalidDuration() {
        assertFalse(JSONValidation.isDuration("1D"));
        assertFalse(JSONValidation.isDuration("P20S"));
        assertFalse(JSONValidation.isDuration("rubbish"));
        assertFalse(JSONValidation.isDuration(null));
    }

    @Test
    public void shouldAcceptValidUUID() {
        assertTrue(JSONValidation.isUUID("632132e4-d62e-11ea-87e9-8f96286622c4"));
        assertTrue(JSONValidation.isUUID("6afc2014-d62e-11ea-9122-673293b85d12"));
        assertTrue(JSONValidation.isUUID("7233014a-d62e-11ea-911b-a335d7bd6aba"));
    }

    @Test
    public void shouldRejectInvalidUUID() {
        assertFalse(JSONValidation.isUUID("632132e4-d62e-11ea-87e9-8f96286622cz"));
        assertFalse(JSONValidation.isUUID("6afc2014d62e11ea9122673293b85d12"));
        assertFalse(JSONValidation.isUUID("7233-014ad62e-11ea-911b-a335d7bd6aba"));
        assertFalse(JSONValidation.isUUID("rubbish"));
        assertFalse(JSONValidation.isUUID(null));
    }

    @Test
    public void shouldAcceptValidHostname() {
        assertTrue(JSONValidation.isHostname("valid"));
        assertTrue(JSONValidation.isHostname("valid-12345"));
        assertTrue(JSONValidation.isHostname("1valid"));
        assertTrue(JSONValidation.isHostname("valid.com"));
        assertTrue(JSONValidation.isHostname("valid.abc.def.com"));
    }

    @Test
    public void shouldRejectInvalidHostname() {
        assertFalse(JSONValidation.isHostname("@invalid"));
        assertFalse(JSONValidation.isHostname("invalid-"));
        assertFalse(JSONValidation.isHostname("invalid-.com"));
        assertFalse(JSONValidation.isHostname("-valid"));
        assertFalse(JSONValidation.isHostname("not--valid"));
        assertFalse(JSONValidation.isHostname("invalid.com."));
        assertFalse(JSONValidation.isHostname("invalid..com"));
        assertFalse(JSONValidation.isHostname(""));
        assertFalse(JSONValidation.isHostname(null));
    }

    @Test
    public void shouldAcceptValidEmail() {
        assertTrue(JSONValidation.isEmail("me@valid"));
        assertTrue(JSONValidation.isEmail("a.b.c@valid-12345"));
        assertTrue(JSONValidation.isEmail("very-long-name@1valid"));
        assertTrue(JSONValidation.isEmail("1@valid.com"));
        assertTrue(JSONValidation.isEmail("a+b@valid.abc.def.com"));
    }

    @Test
    public void shouldRejectInvalidEmail() {
        assertFalse(JSONValidation.isEmail("alpha@@invalid"));
        assertFalse(JSONValidation.isEmail("xyz@invalid-"));
        assertFalse(JSONValidation.isEmail("hello@invalid-.com"));
        assertFalse(JSONValidation.isEmail("a@-valid"));
        assertFalse(JSONValidation.isEmail("b@not--valid"));
        assertFalse(JSONValidation.isEmail("c@invalid.com."));
        assertFalse(JSONValidation.isEmail("d@invalid..com"));
        assertFalse(JSONValidation.isEmail("(xyz)@valid"));
        assertFalse(JSONValidation.isEmail("abc?@valid.com"));
        assertFalse(JSONValidation.isEmail(""));
        assertFalse(JSONValidation.isEmail(null));
    }

}
