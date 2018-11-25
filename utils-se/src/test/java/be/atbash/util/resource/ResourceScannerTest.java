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

import java.util.Set;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceScannerTest {

    @Test
    public void getResources() {

        Pattern pattern = Pattern.compile("walker/directory" + ".*");
        Set<String> resources = ResourceScanner.getInstance().getResources(pattern);
        assertThat(resources).contains("walker/directory/file2.txt", "walker/directory/file3", "walker/directory/fileInJar");
    }

    @Test
    public void existsResource_Local() {
        boolean found = ResourceScanner.getInstance().existsResource("walker/file1");
        assertThat(found).isTrue();
    }

    @Test
    public void existsResource_Jar() {
        boolean found = ResourceScanner.getInstance().existsResource("walker/fromJar.txt");
        assertThat(found).isTrue();
    }

    @Test
    public void existsResource_Local_NotFound() {
        boolean found = ResourceScanner.getInstance().existsResource("walker/file3");
        assertThat(found).isFalse();
    }

    @Test
    public void isUniqueResource_true() {

        boolean unique = ResourceScanner.getInstance().isUniqueResource("walker/directory/file2.txt");
        assertThat(unique).isTrue();
    }

    @Test
    public void isUniqueResource_false() {

        boolean unique = ResourceScanner.getInstance().isUniqueResource("walker/directory/file3");
        assertThat(unique).isFalse();
    }

    @Test
    public void isUniqueResource_nonExistent() {

        boolean unique = ResourceScanner.getInstance().isUniqueResource("walker/file3");
        assertThat(unique).isFalse();
    }

    @Test
    public void getResourcePaths() {
        Set<String> paths = ResourceScanner.getInstance().getResourcePaths("walker/directory/file3");
        int cnt = 0;
        for (String path : paths) {
            if (path.startsWith("jar:file:")) {
                if (path.endsWith(".jar!/META-INF/MANIFEST.MF")) {
                    cnt++; // The resource within the jar is found
                }
            }
            if (path.startsWith("file:")) {
                if (path.endsWith("utils-se/target/test-classes/")) {
                    cnt++; // The resource within directory of project found
                }
            }
        }
        assertThat(cnt).isEqualTo(2);
    }
}