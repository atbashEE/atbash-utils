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
package be.atbash.util.resource.internal.vfs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * an implementation of {@link Vfs.Dir} for {@link java.util.zip.ZipFile}
 */
public class ZipDir implements Vfs.Dir {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZipDir.class);

    private java.util.zip.ZipFile jarFile;

    public ZipDir(JarFile jarFile) {
        this.jarFile = jarFile;
    }

    public String getPath() {
        return jarFile.getName();
    }

    public Iterable<Vfs.File> getFiles() {
        List<Vfs.File> result = new ArrayList<>();

        Enumeration<? extends ZipEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (!entry.isDirectory()) {
                result.add(new ZipFile(ZipDir.this, entry));
            }
        }
        return result;
    }

    public void close() {
        try {
            jarFile.close();
        } catch (IOException e) {
            LOGGER.warn("Could not close JarFile", e);
        }
    }

    @Override
    public String toString() {
        return jarFile.getName();
    }
}
