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
package be.atbash.util.resource;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceUtilTest {

    private static final String VALUE = "Value of Key2";

    @Test
    public void getStream() throws IOException {
        // Just to test a few things (serviceLoader, sorting, and context)

        Map<String, String> data = new HashMap<>();
        data.put("key1", "Value of Key1");
        data.put("key2", VALUE);

        InputStream is = ResourceUtil.getInstance().getStream("key2", data);
        assertThat(convertStreamToString(is)).isEqualTo(VALUE);
        is.close();
    }

    private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}