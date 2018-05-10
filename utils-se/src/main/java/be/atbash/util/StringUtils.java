/*
 * Copyright 2014-2018 Rudy De Busscher (https://www.atbash.be)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.atbash.util;

import java.util.*;

/**
 * Simple utility class for String operations useful across the framework. Taken from Apache Shiro.
 */
@PublicAPI
public final class StringUtils {

    /**
     * Constant representing the empty string, equal to &quot;&quot;
     */
    public static final String EMPTY_STRING = "";

    /**
     * Constant representing the default delimiter character (comma), equal to <code>','</code>
     */
    public static final char DEFAULT_DELIMITER_CHAR = ',';

    /**
     * Constant representing the default quote character (double quote), equal to '&quot;'</code>
     */
    public static final char DEFAULT_QUOTE_CHAR = '"';

    /**
     * Singleton pattern.
     */
    private StringUtils() {
    }

    /**
     * Check whether the given String has actual text.
     * More specifically, returns <code>true</code> if the string not <code>null</code>,
     * its length is greater than 0, and it contains at least one non-whitespace character.
     * <p/>
     * <code>StringUtils.hasText(null) == false<br/>
     * StringUtils.hasText("") == false<br/>
     * StringUtils.hasText(" ") == false<br/>
     * StringUtils.hasText("12345") == true<br/>
     * StringUtils.hasText(" 12345 ") == true</code>
     * <p/>
     *
     * @param str the String to check (may be <code>null</code>)
     * @return <code>true</code> if the String is not <code>null</code>, its length is
     * greater than 0, and it does not contain whitespace only
     * @see Character#isWhitespace
     */
    public static boolean hasText(String str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the given String has actual text.
     * More specifically, returns <code>true</code> if the string is <code>null</code> or
     * its length is equal to 0, and it contains no non-whitespace character.
     * <p/>
     * <code>StringUtils.hasText(null) == true<br/>
     * StringUtils.hasText("") == true<br/>
     * StringUtils.hasText(" ") == true<br/>
     * StringUtils.hasText("12345") == false<br/>
     * StringUtils.hasText(" 12345 ") == false</code>
     *
     * @param value the String to check (may be <code>null</code>)
     * @return <code>true</code> if the String is <code>null</code> or its length is
     * equal to 0, or it does contain whitespace only
     * @see Character#isWhitespace
     */

    public static boolean isEmpty(String value) {
        return !hasText(value);
    }

    /**
     * Check whether the given char array has actual text.
     * More specifically, returns <code>true</code> if the array is <code>null</code> or
     * its length is equal to 0, and it contains no non-whitespace character.
     * <p/>
     *
     *
     * @param value the String to check (may be <code>null</code>)
     * @return <code>true</code> if the Array is <code>null</code> or its length is
     * equal to 0, or it does contain whitespace only
     * @see Character#isWhitespace
     */
    public static boolean isEmpty(char[] value) {
        if (value == null) {
            return true;
        }
        return !hasText(new String(value));
    }

    /**
     * Check that the given String is neither <code>null</code> nor of length 0.
     * Note: Will return <code>true</code> for a String that purely consists of whitespace.
     * <p/>
     * <code>StringUtils.hasLength(null) == false<br/>
     * StringUtils.hasLength("") == false<br/>
     * StringUtils.hasLength(" ") == true<br/>
     * StringUtils.hasLength("Hello") == true</code>
     * <p/>
     *
     * @param str the String to check (may be <code>null</code>)
     * @return <code>true</code> if the String is not null and has length
     * @see #hasText(String)
     */
    public static boolean hasLength(String str) {
        return (str != null && str.length() > 0);
    }

    /**
     * Test if the given String starts with the specified prefix,
     * ignoring upper/lower case.
     * <p/>
     *
     * @param str    the String to check
     * @param prefix the prefix to look for
     * @return <code>true</code> starts with the specified prefix (ignoring case), <code>false</code> if it does not.
     * @see String#startsWith
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        if (str == null || prefix == null) {
            return false;
        }
        if (str.startsWith(prefix)) {
            return true;
        }
        if (str.length() < prefix.length()) {
            return false;
        }
        String lcStr = str.substring(0, prefix.length()).toLowerCase();
        String lcPrefix = prefix.toLowerCase();
        return lcStr.equals(lcPrefix);
    }

    /**
     * Returns a 'cleaned' representation of the specified argument.  'Cleaned' is defined as the following:
     * <p/>
     * <ol>
     * <li>If the specified <code>String</code> is <code>null</code>, return <code>null</code></li>
     * <li>If not <code>null</code>, {@link String#trim() trim()} it.</li>
     * <li>If the trimmed string is equal to the empty String (i.e. &quot;&quot;), return <code>null</code></li>
     * <li>If the trimmed string is not the empty string, return the trimmed version</li>.
     * </ol>
     * <p/>
     * Therefore this method always ensures that any given string has trimmed text, and if it doesn't, <code>null</code>
     * is returned.
     *
     * @param in the input String to clean.
     * @return a populated-but-trimmed String or <code>null</code> otherwise
     */
    public static String clean(String in) {
        String out = in;

        if (in != null) {
            out = in.trim();
            if (out.equals(EMPTY_STRING)) {
                out = null;
            }
        }

        return out;
    }

    /**
     * Returns the specified array as a comma-delimited (',') string.
     *
     * @param array the array whose contents will be converted to a string.
     * @return the array's contents as a comma-delimited (',') string.
     */
    public static String toDelimitedString(Object[] array) {
        return toDelimitedString(",", array);
    }

    /**
     * Returns the array's contents as a string, with each element delimited by the specified
     * {@code delimiter} argument.  Useful for {@code toString()} implementations and log messages.
     *
     * @param delimiter the delimiter to use between each element
     * @param array     the array whose contents will be converted to a string
     * @return a single string, delimited by the specified {@code delimiter}.
     */
    public static String toDelimitedString(String delimiter, Object... array) {
        if (array == null || array.length == 0) {
            return EMPTY_STRING;
        }
        return toDelimitedString(delimiter, Arrays.asList(array));
    }

    /**
     * Returns the collection's contents as a string, with each element delimited by the specified
     * {@code delimiter} argument.  Useful for {@code toString()} implementations and log messages.
     *
     * @param delimiter  the delimiter to use between each element
     * @param collection the collection whose contents will be converted to a string
     * @return a single string, delimited by the specified {@code delimiter}.
     */
    public static String toDelimitedString(String delimiter, Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            return EMPTY_STRING;
        }
        return toDelimitedString(delimiter, collection.iterator());
    }

    /**
     * Joins the elements of the provided {@code Iterator} into
     * a single String containing the provided elements.</p>
     * <p/>
     * No delimiter is added before or after the list.
     * A {@code null} separator is the same as an empty String ("").</p>
     * <p/>
     * Copied from Commons Lang, version 3 (r1138702).</p>
     *
     * @param iterator  the {@code Iterator} of values to join together, may be null
     * @param separator the separator character to use, null treated as ""
     * @return the joined String, {@code null} if null iterator input
     */
    public static String toDelimitedString(String separator, Iterator<?> iterator) {
        String empty = "";

        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return empty;
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return first == null ? empty : first.toString();
        }

        // two or more elements
        StringBuilder buf = new StringBuilder(256); // Java default is 16, probably too small
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }

    /**
     * Tokenize the given String into a String array via a StringTokenizer.
     * Trims tokens and omits empty tokens.
     * <p>The given delimiters string is supposed to consist of any number of
     * delimiter characters. Each of those characters can be used to separate
     * tokens. A delimiter is always a single character;
     * <p/>
     *
     * @param str        the String to tokenize
     * @param delimiters the delimiter characters, assembled as String
     *                   (each of those characters is individually considered as delimiter).
     * @return an array of the tokens
     * @see StringTokenizer
     * @see String#trim()
     */
    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    /**
     * Tokenize the given String into a String array via a StringTokenizer.
     * <p>The given delimiters string is supposed to consist of any number of
     * delimiter characters. Each of those characters can be used to separate
     * tokens. A delimiter is always a single character;
     * <p/>
     *
     * @param str               the String to tokenize
     * @param delimiters        the delimiter characters, assembled as String
     *                          (each of those characters is individually considered as delimiter)
     * @param trimTokens        trim the tokens via String's <code>trim</code>
     * @param ignoreEmptyTokens omit empty tokens from the result array
     *                          (only applies to tokens that are empty after trimming; StringTokenizer
     *                          will not consider subsequent delimiters as token in the first place).
     * @return an array of the tokens (<code>null</code> if the input String
     * was <code>null</code>)
     * @see StringTokenizer
     * @see String#trim()
     */
    @SuppressWarnings({"unchecked"})
    public static String[] tokenizeToStringArray(
            String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

        if (str == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }

    /**
     * Copy the given Collection into a String array.
     * <p/>
     *
     * @param collection the Collection to copy
     * @return the String array (<code>null</code> if the passed-in
     * Collection was <code>null</code>)
     */
    @SuppressWarnings({"unchecked"})
    public static String[] toStringArray(Collection<String> collection) {
        if (collection == null) {
            return null;
        }
        return collection.toArray(new String[collection.size()]);
    }

    /**
     * Splits the specified delimited String into tokens, supporting quoted tokens so that quoted strings themselves
     * won't be tokenized. This method uses , as delimiter , " as quote, removes the quotes from the result and trims the values.
     *
     * @param aLine the String to parse
     * @return the tokens discovered from parsing the given delimited <tt>aline</tt>.
     */
    public static String[] split(String aLine) {
        return split(aLine, DEFAULT_DELIMITER_CHAR);
    }

    /**
     * Splits the specified delimited String into tokens, supporting quoted tokens so that quoted strings themselves
     * won't be tokenized. This method uses " as quote, removes the quotes from the result and trims the values.
     *
     * @param aLine     the String to parse
     * @param delimiter the delimiter by which the <tt>line</tt> argument is to be split
     * @return the tokens discovered from parsing the given delimited <tt>aline</tt>.
     */
    public static String[] split(String aLine, char delimiter) {
        return split(aLine, delimiter, DEFAULT_QUOTE_CHAR);
    }

    /**
     * Splits the specified delimited String into tokens, supporting quoted tokens so that quoted strings themselves
     * won't be tokenized. This method uses the same start as end quote, removes the quotes from the result and trims the values.
     *
     * @param aLine     the String to parse
     * @param delimiter the delimiter by which the <tt>line</tt> argument is to be split
     * @param quoteChar the character signifying the quoted text (start and end) (so the quoted text will not be split)
     * @return the tokens discovered from parsing the given delimited <tt>aline</tt>.
     */
    public static String[] split(String aLine, char delimiter, char quoteChar) {
        return split(aLine, delimiter, quoteChar, quoteChar);
    }

    /**
     * Splits the specified delimited String into tokens, supporting quoted tokens so that quoted strings themselves
     * won't be tokenized. This method removes the quotes from the result and trims the values.
     *
     * @param aLine          the String to parse
     * @param delimiter      the delimiter by which the <tt>line</tt> argument is to be split
     * @param beginQuoteChar the character signifying the start of quoted text (so the quoted text will not be split)
     * @param endQuoteChar   the character signifying the end of quoted text
     * @return the tokens discovered from parsing the given delimited <tt>aline</tt>.
     */
    public static String[] split(String aLine, char delimiter, char beginQuoteChar, char endQuoteChar) {
        return split(aLine, delimiter, beginQuoteChar, endQuoteChar, false, true);
    }

    /**
     * Splits the specified delimited String into tokens, supporting quoted tokens so that quoted strings themselves
     * won't be tokenized.
     * <p/>
     * This method's implementation is very loosely based (with significant modifications) on
     * <a href="http://blogs.bytecode.com.au/glen">Glen Smith</a>'s open-source
     * <a href="http://opencsv.svn.sourceforge.net/viewvc/opencsv/trunk/src/au/com/bytecode/opencsv/CSVReader.java?&view=markup">CSVReader.java</a>
     * file.
     * <p/>
     * That file is Apache 2.0 licensed as well, making Glen's code a great starting point for us to modify to
     * our needs.
     *
     * @param aLine          the String to parse
     * @param delimiter      the delimiter by which the <tt>line</tt> argument is to be split
     * @param beginQuoteChar the character signifying the start of quoted text (so the quoted text will not be split)
     * @param endQuoteChar   the character signifying the end of quoted text
     * @param retainQuotes   if the quotes themselves should be retained when constructing the corresponding token
     * @param trimTokens     if leading and trailing whitespace should be trimmed from discovered tokens.
     * @return the tokens discovered from parsing the given delimited <tt>aline</tt>.
     */
    public static String[] split(String aLine, char delimiter, char beginQuoteChar, char endQuoteChar,
                                 boolean retainQuotes, boolean trimTokens) {
        String line = clean(aLine);
        if (line == null) {
            return null;
        }

        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {

            char c = line.charAt(i);
            if (c == beginQuoteChar) {
                // this gets complex... the quote may end a quoted block, or escape another quote.
                // do a 1-char lookahead:
                if (inQuotes  // we are in quotes, therefore there can be escaped quotes in here.
                        && line.length() > (i + 1)  // there is indeed another character to check.
                        && line.charAt(i + 1) == beginQuoteChar) { // ..and that char. is a quote also.
                    // we have two quote chars in a row == one quote char, so consume them both and
                    // put one on the token. we do *not* exit the quoted text.
                    sb.append(line.charAt(i + 1));
                    i++;
                } else {
                    inQuotes = !inQuotes;
                    if (retainQuotes) {
                        sb.append(c);
                    }
                }
            } else if (c == endQuoteChar) {
                inQuotes = !inQuotes;
                if (retainQuotes) {
                    sb.append(c);
                }
            } else if (c == delimiter && !inQuotes) {
                String s = sb.toString();
                if (trimTokens) {
                    s = s.trim();
                }
                tokens.add(s);
                sb = new StringBuilder(); // start work on next token
            } else {
                sb.append(c);
            }
        }
        String s = sb.toString();
        if (trimTokens) {
            s = s.trim();
        }
        tokens.add(s);
        return tokens.toArray(new String[tokens.size()]);
    }

    /**
     * Count the number of occurrences of a char in the String. When null is passed to String to search, it returns 0.
     *
     * @param haystack The String we like to search
     * @param needle   the character to search for.
     * @return The number of occurrences found.
     */
    public static int countOccurrences(String haystack, char needle) {
        int count = 0;
        if (haystack == null) {
            return count;
        }
        for (int i = 0; i < haystack.length(); i++) {
            if (haystack.charAt(i) == needle) {
                count++;
            }
        }
        return count;
    }

}
