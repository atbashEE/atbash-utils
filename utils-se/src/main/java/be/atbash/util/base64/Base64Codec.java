/*
 * nimbus-jose-jwt
 *
 * Copyright 2012-2016, Connect2id Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package be.atbash.util.base64;

import be.atbash.util.PublicAPI;
import be.atbash.util.StringUtils;
import be.atbash.util.codec.CodecSupport;

import java.util.Arrays;

/**
 * Base64 encode and decode because only available in Java 8. Code copied from nimbus-jose-jwt with some modifications.
 */
@PublicAPI
public final class Base64Codec {

    /**
     * The base 64 characters.
     */
    private static final char[] CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    /**
     * The base 64 URL-safe characters.
     */
    private static final char[] CA_URL_SAFE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".toCharArray();

    /**
     * List of characters which are allowed but not real BASE64 characters (newline and padding)
     */
    private static final String VALID_NON_CA = "\n\r=";

    /**
     * Maps base 64 characters to their respective byte values.
     */
    private static final int[] IA = new int[256];

    /**
     * Maps base 64 URL-safe characters to their respective byte values.
     */
    private static final int[] IA_URL_SAFE = new int[256];

    static {
        // Regular map
        Arrays.fill(IA, -1);
        for (int i = 0, iS = CA.length; i < iS; i++) {
            IA[CA[i]] = i;
        }
        IA['='] = 0;

        // URL-safe map
        Arrays.fill(IA_URL_SAFE, -1);
        for (int i = 0, iS = CA_URL_SAFE.length; i < iS; i++) {
            IA_URL_SAFE[CA_URL_SAFE[i]] = i;
        }
        IA_URL_SAFE['='] = 0;
    }

    /**
     * Singleton style class.
     */
    private Base64Codec() {
    }

    /**
     * Test to see if the String contains a valid Base64 value. Separators and padding (=) is considered as valid
     * characters.
     *
     * @param value The value to test
     * @return true or false depending on the validity.
     */
    public static boolean isBase64Encoded(String value) {
        // Number of separator and illegal characters
        int separatorCount = 0;

        boolean allValidChars = true;

        for (int i = 0; i < value.length() && allValidChars; i++) {

            char c = value.charAt(i);
            int b = (int) c;
            if (b < 256) { // Only ASCII characters are valid base64 characters
                if (IA[c] == -1 && IA_URL_SAFE[c] == -1) {
                    separatorCount++;
                    if (!VALID_NON_CA.contains(String.valueOf(c))) {
                        allValidChars = false;
                    }
                }
            } else {
                allValidChars = false;
            }
        }

        if (!allValidChars) {
            return false;
        }

        // Ensure the legal chars (including '=' padding) divide by 4 (RFC 2045)
        return (value.length() - separatorCount) % 4 == 0;
    }

    /**
     * Test to see if the byte array contains a valid Base64 value. Separators and padding (=) are considered as valid
     * characters.
     *
     * @param value The value to test
     * @return true or false depending on the validity.
     */
    public static boolean isBase64Encoded(byte[] value) {
        return isBase64Encoded(CodecSupport.toString(value));
    }

    /**
     * Computes the base64 encoded character length for the specified input byte length.
     *
     * @param inputLength The input byte length.
     * @param urlSafe     {@code true} for URL-safe encoding.
     * @return The base 64 encoded character length.
     */
    public static int computeEncodedLength(int inputLength, boolean urlSafe) {

        if (inputLength == 0) {
            return 0;
        }

        if (urlSafe) {

            // Compute the number of complete quads (4-char blocks)
            int fullQuadLength = (inputLength / 3) << 2;

            // Compute the remaining bytes at the end
            int remainder = inputLength % 3;

            // Compute the total
            return remainder == 0 ? fullQuadLength : fullQuadLength + remainder + 1;
        } else {
            // Original Mig code
            return ((inputLength - 1) / 3 + 1) << 2;
        }
    }

    /**
     * Normalises a base64 encoded string by ensuring any URL-safe
     * characters are replaced with their regular base64 representation and
     * any truncated '=' padding is restored.
     *
     * @param value The base 64 or base 64 URL-safe encoded string.
     *              Must not be {@code null}.
     * @return The normalised base 64 encoded string.
     */
    public static String normalizeEncodedString(String value) {

        int inputLen = value.length();

        // Compute missing padding, taking illegal chars into account
        int legalLen = inputLen - countIllegalChars(value);
        int padLength = legalLen % 4 == 0 ? 0 : 4 - (legalLen % 4);

        // Create output array
        char[] chars = new char[inputLen + padLength];

        // Copy chars into output array
        value.getChars(0, inputLen, chars, 0);

        // Append padding chars if required
        for (int i = 0; i < padLength; i++) {
            chars[inputLen + i] = '=';
        }

        // Replace URL-safe chars
        for (int i = 0; i < inputLen; i++) {
            if (chars[i] == '_') {
                chars[i] = '/';
            } else if (chars[i] == '-') {
                chars[i] = '+';
            }
        }

        return new String(chars);
    }

    /**
     * Counts the illegal / separator characters in the specified base64
     * or base64 URL-safe encoded string.
     *
     * @param b64String The base 64 or base 64 URL-safe encoded string.
     *                  Must not be {@code null}.
     * @return The illegal character count, zero if none.
     */
    private static int countIllegalChars(String b64String) {

        // Number of separator and illegal characters
        int illegalCharCount = 0;

        for (int i = 0; i < b64String.length(); i++) {

            char c = b64String.charAt(i);

            if (IA[c] == -1 && IA_URL_SAFE[c] == -1) {
                illegalCharCount++;
            }
        }

        return illegalCharCount;
    }

    /**
     * Encodes a byte array into a base64 encoded character array.
     *
     * @param byteArray The bytes to convert. If {@code null} or length 0
     *                  an empty array will be returned.
     * @param urlSafe   If {@code true} to apply URL-safe encoding (padding
     *                  still included and not to spec).
     * @return The base 64 encoded character array. Never {@code null}.
     */
    public static char[] encodeToChar(byte[] byteArray, boolean urlSafe) {

        // Check special case
        int sLen = byteArray != null ? byteArray.length : 0;

        if (sLen == 0) {
            return new char[0];
        }

        int eLen = (sLen / 3) * 3;                      // Length of even 24-bits.
        int dLen = computeEncodedLength(sLen, urlSafe); // Returned character count
        char[] out = new char[dLen];

        // Encode even 24-bits
        for (int s = 0, d = 0; s < eLen; ) {

            // Copy next three bytes into lower 24 bits of int, paying attention to sign
            int i = (byteArray[s++] & 0xff) << 16 | (byteArray[s++] & 0xff) << 8 | (byteArray[s++] & 0xff);

            // Encode the int into four chars
            if (urlSafe) {
                out[d++] = CA_URL_SAFE[(i >>> 18) & 0x3f];
                out[d++] = CA_URL_SAFE[(i >>> 12) & 0x3f];
                out[d++] = CA_URL_SAFE[(i >>> 6) & 0x3f];
                out[d++] = CA_URL_SAFE[i & 0x3f];
            } else {
                out[d++] = CA[(i >>> 18) & 0x3f];
                out[d++] = CA[(i >>> 12) & 0x3f];
                out[d++] = CA[(i >>> 6) & 0x3f];
                out[d++] = CA[i & 0x3f];
            }
        }

        // Pad and encode last bits if source isn't even 24 bits
        // according to URL-safe switch
        int left = sLen - eLen; // 0 - 2.
        if (left > 0) {
            // Prepare the int
            int i = ((byteArray[eLen] & 0xff) << 10) | (left == 2 ? ((byteArray[sLen - 1] & 0xff) << 2) : 0);

            // Set last four chars
            if (urlSafe) {

                if (left == 2) {
                    out[dLen - 3] = CA_URL_SAFE[i >> 12];
                    out[dLen - 2] = CA_URL_SAFE[(i >>> 6) & 0x3f];
                    out[dLen - 1] = CA_URL_SAFE[i & 0x3f];
                } else {
                    out[dLen - 2] = CA_URL_SAFE[i >> 12];
                    out[dLen - 1] = CA_URL_SAFE[(i >>> 6) & 0x3f];
                }
            } else {
                // Original Mig code with padding
                out[dLen - 4] = CA[i >> 12];
                out[dLen - 3] = CA[(i >>> 6) & 0x3f];
                out[dLen - 2] = left == 2 ? CA[i & 0x3f] : '=';
                out[dLen - 1] = '=';
            }
        }

        return out;
    }

    /**
     * Encodes a byte array into a base64 encoded string.
     *
     * @param byteArray The bytes to convert. If {@code null} or length 0
     *                  an empty array will be returned.
     * @param urlSafe   If {@code true} to apply URL-safe encoding (padding
     *                  still included and not to spec).
     * @return The base 64 encoded string. Never {@code null}.
     */
    public static String encodeToString(byte[] byteArray, boolean urlSafe) {

        // Reuse char[] since we can't create a String incrementally
        // and StringBuffer/Builder would be slower
        return new String(encodeToChar(byteArray, urlSafe));
    }

    /**
     * Decodes a base64 or base64 URL-safe encoded string. May contain
     * line separators. Any illegal characters are ignored.
     *
     * @param value The base 64 or base 64 URL-safe encoded string. May
     *              be empty or {@code null}.
     * @return The decoded byte array, empty if the input base 64 encoded
     * string is empty, {@code null} or corrupted.
     */
    public static byte[] decode(String value) {

        // Check special case
        if (StringUtils.isEmpty(value)) {
            return new byte[0];
        }

        String nStr = normalizeEncodedString(value);

        int sLen = nStr.length();

        // Count illegal characters (including '\r', '\n') to determine
        // the size of the byte array to return
        int sepCnt = countIllegalChars(nStr);

        // Ensure the legal chars (including '=' padding) divide by 4 (RFC 2045)
        if ((sLen - sepCnt) % 4 != 0) {
            // The string is corrupted
            return new byte[0];
        }

        // Count '=' at end
        int pad = 0;

        for (int i = sLen; i > 1 && IA[nStr.charAt(--i)] <= 0; ) {
            if (nStr.charAt(i) == '=') {
                pad++;
            }
        }

        int len = ((sLen - sepCnt) * 6 >> 3) - pad;

        // Preallocate byte[] of final length
        byte[] dArr = new byte[len];

        for (int s = 0, d = 0; d < len; ) {
            // Assemble three bytes into an int from four base 64
            // characters
            int i = 0;

            for (int j = 0; j < 4; j++) {
                // j only increased if a valid char was found
                int c = IA[nStr.charAt(s++)];
                if (c >= 0) {
                    i |= c << (18 - j * 6);
                } else {
                    j--;
                }
            }
            // Add the bytes
            dArr[d++] = (byte) (i >> 16);
            if (d < len) {
                dArr[d++] = (byte) (i >> 8);
                if (d < len) {
                    dArr[d++] = (byte) i;
                }
            }
        }

        return dArr;
    }

    /**
     * Decodes a base64 or base64 URL-safe encoded string represented as byte array using UTF-8 encoding.
     * May contain line separators. Any illegal characters are ignored.
     *
     * @param value The base 64 or base 64 URL-safe encoded byte array. May
     *              be empty or {@code null}.
     * @return The decoded byte array, empty if the input base 64 encoded
     * string is empty, {@code null} or corrupted.
     */
    public static byte[] decode(byte[] value) {

        return decode(CodecSupport.toString(value));
    }
}