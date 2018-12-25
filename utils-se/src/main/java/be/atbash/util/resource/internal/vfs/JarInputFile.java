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

import java.util.zip.ZipEntry;

/**
 *
 */
public class JarInputFile implements Vfs.File {
    private ZipEntry entry;

    public JarInputFile(ZipEntry entry) {
        this.entry = entry;
    }

    public String getName() {
        String name = entry.getName();
        return name.substring(name.lastIndexOf('/') + 1);
    }

    public String getRelativePath() {
        return entry.getName();
    }
}
