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
package be.atbash.util.resource.internal.vfs;

import be.atbash.util.resource.internal.ResourceWalkerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

/**
 *
 */
public class JarInputDir implements Vfs.Dir {
    private static final Logger LOGGER = LoggerFactory.getLogger(JarInputDir.class);

    private final URL url;
    private JarInputStream jarInputStream;

    public JarInputDir(URL url) {
        this.url = url;
    }

    public String getPath() {
        return url.getPath();
    }

    public Iterable<Vfs.File> getFiles() {
        List<Vfs.File> result = new ArrayList<>();

        try {
            jarInputStream = new JarInputStream(url.openConnection().getInputStream());
        } catch (Exception e) {
            throw new ResourceWalkerException("Could not open url connection", e);
        }
        try {
            ZipEntry entry = jarInputStream.getNextJarEntry();
            while (entry != null) {

                if (!entry.isDirectory()) {
                    result.add(new JarInputFile(entry));
                }

                entry = jarInputStream.getNextJarEntry();
            }
        } catch (IOException e) {
            throw new ResourceWalkerException("Error in processing content of " + url.getPath(), e);
        }

        return result;

    }

    public void close() {

        try {
            if (jarInputStream != null) {
                jarInputStream.close();
            }
        } catch (IOException e) {
            LOGGER.warn("Could not close InputStream", e);

        }
    }

}
