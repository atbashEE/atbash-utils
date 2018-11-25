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

/**
 * an implementation of {@link Vfs.File} for a directory {@link java.io.File}
 */
public class SystemFile implements Vfs.File {
    private final SystemDir root;
    private final java.io.File file;

    public SystemFile(final SystemDir root, java.io.File file) {
        this.root = root;
        this.file = file;
    }

    public String getName() {
        return file.getName();
    }

    public String getRelativePath() {
        String filepath = file.getPath().replace("\\", "/");
        if (filepath.startsWith(root.getPath())) {
            return filepath.substring(root.getPath().length() + 1);
        }

        return null; //should not get here
    }

    @Override
    public String toString() {
        return file.toString();
    }
}
