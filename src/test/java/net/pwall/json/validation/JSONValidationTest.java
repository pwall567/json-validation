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
    public void shouldAcceptValidURI() {
        assertTrue(JSONValidation.isURI("http://pwall.net/schema/schema.json"));
        assertTrue(JSONValidation.isURI("urn:uuid:123e4567-e89b-12d3-a456-426655440000"));
    }

    @Test
    public void shouldRejectInvalidURI() {
        assertFalse(JSONValidation.isURI("xxx"));
        assertFalse(JSONValidation.isURI(":xxx"));
        assertFalse(JSONValidation.isURI("http:"));
        assertFalse(JSONValidation.isURI(null));
    }

    @Test
    public void shouldAcceptValidURIReference() {
        assertTrue(JSONValidation.isURIReference("http://pwall.net/schema/schema.json"));
        assertTrue(JSONValidation.isURIReference("urn:uuid:123e4567-e89b-12d3-a456-426655440000"));
        assertTrue(JSONValidation.isURIReference("schema-2.json"));
        assertTrue(JSONValidation.isURIReference("/xxx"));
        assertTrue(JSONValidation.isURIReference("#abcd"));
    }

    @Test
    public void shouldRejectInvalidURIReference() {
        assertFalse(JSONValidation.isURIReference(":xxx"));
        assertFalse(JSONValidation.isURIReference("http:"));
        assertFalse(JSONValidation.isURIReference(null));
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
        assertFalse(JSONValidation.isUUID(""));
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

    @Test
    public void shouldAcceptValidIPV4() {
        assertTrue(JSONValidation.isIPV4("1.2.3.4"));
        assertTrue(JSONValidation.isIPV4("127.0.0.1"));
        assertTrue(JSONValidation.isIPV4("192.168.1.100"));
        assertTrue(JSONValidation.isIPV4("10.20.1.255"));
    }

    @Test
    public void shouldRejectInvalidIPV4() {
        assertFalse(JSONValidation.isIPV4("1.2.3"));
        assertFalse(JSONValidation.isIPV4("127.0.0.999"));
        assertFalse(JSONValidation.isIPV4("0001.1.1.1"));
        assertFalse(JSONValidation.isIPV4("1.2.3.256"));
        assertFalse(JSONValidation.isIPV4("1.2.3.4.5"));
        assertFalse(JSONValidation.isIPV4("localhost"));
        assertFalse(JSONValidation.isIPV4("a.1.2.4"));
        assertFalse(JSONValidation.isIPV4("192.168.1.abc"));
        assertFalse(JSONValidation.isIPV4("256.1.1.1"));
        assertFalse(JSONValidation.isIPV4("127:0:0:1"));
        assertFalse(JSONValidation.isIPV4(""));
        assertFalse(JSONValidation.isIPV4(null));
    }

    @Test
    public void shouldAcceptValidIPV6() {
        assertTrue(JSONValidation.isIPV6("2001:DB8:0:0:8:800:200C:417A"));
        assertTrue(JSONValidation.isIPV6("2001:db8:0:0:8:800:200c:417a"));
        assertTrue(JSONValidation.isIPV6("2001:DB8::8:800:200C:417A"));
        assertTrue(JSONValidation.isIPV6("FF01:0:0:0:0:0:0:101"));
        assertTrue(JSONValidation.isIPV6("FF01::101"));
        assertTrue(JSONValidation.isIPV6("FF01:0:0:0:0:0:0:0"));
        assertTrue(JSONValidation.isIPV6("FF01::"));
        assertTrue(JSONValidation.isIPV6("ff01::"));
        assertTrue(JSONValidation.isIPV6("0:0:0:0:0:0:0:1"));
        assertTrue(JSONValidation.isIPV6("::1"));
        assertTrue(JSONValidation.isIPV6("0:0:0:0:0:0:0:0"));
        assertTrue(JSONValidation.isIPV6("::"));
        assertTrue(JSONValidation.isIPV6("::1234:1"));
        assertTrue(JSONValidation.isIPV6("::1234:1:2"));
        assertTrue(JSONValidation.isIPV6("::1234:1:2:3"));
        assertTrue(JSONValidation.isIPV6("::1234:1:2:3:4"));
        assertTrue(JSONValidation.isIPV6("::1234:1:2:3:4:5"));
        assertTrue(JSONValidation.isIPV6("::1234:1:2:3:4:5:6"));
        assertTrue(JSONValidation.isIPV6("1234::1:2:3:4"));
        assertTrue(JSONValidation.isIPV6("1234::1:2:3:4:5"));
        assertTrue(JSONValidation.isIPV6("1234::1:2:3:4:5:6"));
        assertTrue(JSONValidation.isIPV6("1234:1:2:3:4::"));
        assertTrue(JSONValidation.isIPV6("1234:1:2:3:4:5::"));
        assertTrue(JSONValidation.isIPV6("1234:1:2:3:4:5:6::"));
        assertTrue(JSONValidation.isIPV6("0:0:0:0:0:0:13.1.68.3"));
        assertTrue(JSONValidation.isIPV6("::13.1.68.3"));
        assertTrue(JSONValidation.isIPV6("0:0:0:0:0:FFFF:129.144.52.38"));
        assertTrue(JSONValidation.isIPV6("::FFFF:129.144.52.38"));
        assertTrue(JSONValidation.isIPV6("::FFFF:1.2.3.4"));
        assertTrue(JSONValidation.isIPV6("::1234:1.2.3.4"));
        assertTrue(JSONValidation.isIPV6("::1234:1:1.2.3.4"));
        assertTrue(JSONValidation.isIPV6("::1234:1:2:1.2.3.4"));
        assertTrue(JSONValidation.isIPV6("::1234:1:2:3:1.2.3.4"));
        assertTrue(JSONValidation.isIPV6("::1234:1:2:3:4:1.2.3.4"));
        assertTrue(JSONValidation.isIPV6("1234::1:2:3:1.2.3.4"));
        assertTrue(JSONValidation.isIPV6("1234::1:2:3:4:1.2.3.4"));
        assertTrue(JSONValidation.isIPV6("1234:1:2:3::1.2.3.4"));
        assertTrue(JSONValidation.isIPV6("1234:1:2:3:4::1.2.3.4"));
    }

    @Test
    public void shouldRejectInvalidIPV6() {
        assertFalse(JSONValidation.isIPV6("127.0.0.1"));
        assertFalse(JSONValidation.isIPV6("2001:DB8:0:0:0:8:800:200C:417A"));
        assertFalse(JSONValidation.isIPV6("2001:DB8::8:800::200C:417A"));
        assertFalse(JSONValidation.isIPV6("2001:DB8::0:0:8:800:200C:417A"));
        assertFalse(JSONValidation.isIPV6("2001:DB8:0:8:800:200C:417A"));
        assertFalse(JSONValidation.isIPV6(":2001:DB8:0:8:800:200C:417A"));
        assertFalse(JSONValidation.isIPV6("2001:DB8:0:8:800:200C:417A:"));
        assertFalse(JSONValidation.isIPV6("2001:DB8::8:800:200G:417A"));
        assertFalse(JSONValidation.isIPV6("::1234:1:2:3:4:5:6:7"));
        assertFalse(JSONValidation.isIPV6("1234::1:2:3:4:5:6:7"));
        assertFalse(JSONValidation.isIPV6("1234:1:2:3:4:5:6:7::"));
        assertFalse(JSONValidation.isIPV6("0:0:0:0:0:FFFF:129.144.52.380"));
        assertFalse(JSONValidation.isIPV6("0:0:0:0:0:FFFF:.144.52.38"));
        assertFalse(JSONValidation.isIPV6("::1234:1:2:3:4:5:1.2.3.4"));
        assertFalse(JSONValidation.isIPV6("1234::1:2:3:4:5:1.2.3.4"));
        assertFalse(JSONValidation.isIPV6("1234:1:2:3:4:5::1.2.3.4"));
        assertFalse(JSONValidation.isIPV6("localhost"));
        assertFalse(JSONValidation.isIPV6(""));
        assertFalse(JSONValidation.isIPV6(null));
    }

}
