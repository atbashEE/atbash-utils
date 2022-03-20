/*
 * Copyright 2014-2022 Rudy De Busscher (https://www.atbash.be)
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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ResourceUtilTest {

    private static final String VALUE = "Value of Key2";

    @Test
    void getStream() throws IOException {
        // Just to test a few things (serviceLoader, sorting, and context)

        Map<String, String> data = new HashMap<>();
        data.put("key1", "Value of Key1");
        data.put("key2", VALUE);

        InputStream is = ResourceUtil.getInstance().getStream("key2", data);
        Assertions.assertThat(convertStreamToString(is)).isEqualTo(VALUE);
        is.close();
    }

    private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @Test
    void resourceExists() {
        boolean exists = ResourceUtil.getInstance().resourceExists("./src/test/resources/walker/file1");
        Assertions.assertThat(exists).isTrue();
    }

    @Test
    void resourceExists_filePrefix() {
        boolean exists = ResourceUtil.getInstance().resourceExists("file:./src/test/resources/walker/file1");
        Assertions.assertThat(exists).isTrue();
    }

    @Test
    void resourceExists_nonexistent() {
        boolean exists = ResourceUtil.getInstance().resourceExists("./src/test/resources/file1");
        Assertions.assertThat(exists).isFalse();
    }

    @Test
    void resourceExists_nonexistent2() {
        boolean exists = ResourceUtil.getInstance().resourceExists("file:./src/test/resources/file1");
        Assertions.assertThat(exists).isFalse();
    }

    @Test
    void getResources() {
        List<URI> resources = ResourceUtil.getInstance().getResources("walker/file1");
        Assertions.assertThat(resources).hasSize(1);
        Assertions.assertThat(resources.get(0).getScheme()).isEqualTo("file");
        Assertions.assertThat(resources.get(0).getPath()).endsWith("target/test-classes/walker/file1");
    }

    @Test
    void getResources_filePrefix() {
        List<URI> resources = ResourceUtil.getInstance().getResources("file:./src/test/resources/walker/directory/file3");
        Assertions.assertThat(resources).hasSize(1);
        Assertions.assertThat(resources.get(0).getScheme()).isEqualTo("file");
        Assertions.assertThat(resources.get(0).getPath()).endsWith("walker/directory/file3");
    }

    @Test
    void getResources_classpath() {
        List<URI> resources = ResourceUtil.getInstance().getResources("META-INF/MANIFEST.MF");
        Assertions.assertThat(resources).hasSizeGreaterThan(20);
        // We can't test for specific number as it depends on the dependencies of the test run. We know it are *many*.
    }

}