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

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
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

    @Test
    public void resourceExists() {
        boolean exists = ResourceUtil.getInstance().resourceExists("./src/test/resources/walker/file1");
        assertThat(exists).isTrue();
    }

    @Test
    public void resourceExists_filePrefix() {
        boolean exists = ResourceUtil.getInstance().resourceExists("file:./src/test/resources/walker/file1");
        assertThat(exists).isTrue();
    }

    @Test
    public void resourceExists_nonexistent() {
        boolean exists = ResourceUtil.getInstance().resourceExists("./src/test/resources/file1");
        assertThat(exists).isFalse();
    }

    @Test
    public void resourceExists_nonexistent2() {
        boolean exists = ResourceUtil.getInstance().resourceExists("file:./src/test/resources/file1");
        assertThat(exists).isFalse();
    }

    @Test
    public void getResources() {
        List<URI> resources = ResourceUtil.getInstance().getResources("walker/file1");
        assertThat(resources).hasSize(1);
        assertThat(resources.get(0).getScheme()).isEqualTo("file");
        assertThat(resources.get(0).getPath()).endsWith("target/test-classes/walker/file1");
    }

    @Test
    public void getResources_filePrefix() {
        List<URI> resources = ResourceUtil.getInstance().getResources("file:./src/test/resources/walker/directory/file3");
        assertThat(resources).hasSize(1);
        assertThat(resources.get(0).getScheme()).isEqualTo("file");
        assertThat(resources.get(0).getPath()).endsWith("walker/directory/file3");
    }

    @Test
    public void getResources_classpath() {
        List<URI> resources = ResourceUtil.getInstance().getResources("META-INF/MANIFEST.MF");
        assertThat(resources.size()).isGreaterThan(20);
        // We can't test for specific number as it depends on the dependencies of the test run. We know it are *many*.
    }

}