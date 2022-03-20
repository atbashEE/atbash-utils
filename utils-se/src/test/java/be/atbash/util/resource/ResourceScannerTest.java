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

import be.atbash.util.TestReflectionUtils;
import be.atbash.util.resource.internal.ResourceWalker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

import java.util.Set;
import java.util.regex.Pattern;

class ResourceScannerTest {

    private TestLogger logger;

    @BeforeEach
    void setup() throws NoSuchFieldException {
        TestReflectionUtils.resetOf(ResourceScanner.class, "instance");  // Reset singleton
        System.setProperty("useExecutorService", ""); // No executorService Active
        // See TestExecutorServiceProvider

        logger = TestLoggerFactory.getTestLogger(ResourceWalker.class);
    }

    @AfterEach
    void reset() {
        TestLoggerFactory.clear();
    }

    @Test
    void getResources() {

        Pattern pattern = Pattern.compile("walker/directory" + ".*");
        Set<String> resources = ResourceScanner.getInstance().getResources(pattern);
        Assertions.assertThat(resources).contains("walker/directory/file2.txt", "walker/directory/file3", "walker/directory/fileInJar");

        Assertions.assertThat(getLogMessage()).doesNotContain("[using executorService]");
    }

    @Test
    void getResources_MultiThreaded() {
        System.setProperty("useExecutorService", "multi"); // Any value wil do
        Pattern pattern = Pattern.compile("walker/directory" + ".*");
        Set<String> resources = ResourceScanner.getInstance().getResources(pattern);
        Assertions.assertThat(resources).contains("walker/directory/file2.txt", "walker/directory/file3", "walker/directory/fileInJar");
        Assertions.assertThat(getLogMessage()).contains("[using executorService]");
    }

    @Test
    void existsResource_Local() {
        boolean found = ResourceScanner.getInstance().existsResource("walker/file1");
        Assertions.assertThat(found).isTrue();
    }

    @Test
    void existsResource_Jar() {
        boolean found = ResourceScanner.getInstance().existsResource("walker/fromJar.txt");
        Assertions.assertThat(found).isTrue();
    }

    @Test
    void existsResource_Local_NotFound() {
        boolean found = ResourceScanner.getInstance().existsResource("walker/file3");
        Assertions.assertThat(found).isFalse();
    }

    @Test
    void isUniqueResource_true() {

        boolean unique = ResourceScanner.getInstance().isUniqueResource("walker/directory/file2.txt");
        Assertions.assertThat(unique).isTrue();
    }

    @Test
    void isUniqueResource_false() {

        boolean unique = ResourceScanner.getInstance().isUniqueResource("walker/directory/file3");
        Assertions.assertThat(unique).isFalse();
    }

    @Test
    void isUniqueResource_nonExistent() {

        boolean unique = ResourceScanner.getInstance().isUniqueResource("walker/file3");
        Assertions.assertThat(unique).isFalse();
    }

    @Test
    void getResourcePaths() {
        // Does not run from IDE but works fine from Maven
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
        Assertions.assertThat(cnt).isEqualTo(2);
    }

    private String getLogMessage() {
        String result = "";
        for (LoggingEvent loggingEvent : logger.getLoggingEvents()) {
            if (Level.INFO.equals(loggingEvent.getLevel())) {
                result = loggingEvent.getMessage();
            }
        }
        return result;
    }

}