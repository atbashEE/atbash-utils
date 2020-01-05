/*
 * Copyright 2014-2020 Rudy De Busscher (https://www.atbash.be)
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

import java.util.Set;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceScannerTest {

    private TestLogger logger;

    @BeforeEach
    public void setup() throws NoSuchFieldException {
        TestReflectionUtils.resetOf(ResourceScanner.class, "INSTANCE");  // Reset singleton
        System.setProperty("useExecutorService",""); // No executorService Active
        // See TestExecutorServiceProvider

        logger = TestLoggerFactory.getTestLogger(ResourceWalker.class);
    }

    @AfterEach
    public void reset() {
        TestLoggerFactory.clear();
    }
    @Test
    public void getResources() {

        Pattern pattern = Pattern.compile("walker/directory" + ".*");
        Set<String> resources = ResourceScanner.getInstance().getResources(pattern);
        assertThat(resources).contains("walker/directory/file2.txt", "walker/directory/file3", "walker/directory/fileInJar");

        assertThat(getLogMessage()).doesNotContain("[using executorService]");
    }

    @Test
    public void getResources_MultiThreaded() {
        System.setProperty("useExecutorService","multi"); // Any value wil do
        Pattern pattern = Pattern.compile("walker/directory" + ".*");
        Set<String> resources = ResourceScanner.getInstance().getResources(pattern);
        assertThat(resources).contains("walker/directory/file2.txt", "walker/directory/file3", "walker/directory/fileInJar");
        assertThat(getLogMessage()).contains("[using executorService]");
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
        assertThat(cnt).isEqualTo(2);
    }

    private String getLogMessage() {
        String result = "";
        for (LoggingEvent loggingEvent : logger.getLoggingEvents()) {
            if (Level.INFO.equals(loggingEvent.getLevel())) {
                result = loggingEvent.getMessage();
            }
        }
        System.out.println(result);
        return result;
    }

}