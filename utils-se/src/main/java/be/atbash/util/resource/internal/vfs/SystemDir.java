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


import java.io.File;
import java.util.*;

/**
 * An implementation of {@link Vfs.Dir} for directory {@link java.io.File}.
 */
public class SystemDir implements Vfs.Dir {
    private final File file;

    public SystemDir(File file) {
        if (file != null && (!file.isDirectory() || !file.canRead())) {
            throw new RuntimeException("cannot use dir " + file);
        }

        this.file = file;
    }

    public String getPath() {
        if (file == null) {
            return "/NO-SUCH-DIRECTORY/";
        }
        return file.getPath().replace("\\", "/");
    }

    public Iterable<Vfs.File> getFiles() {
        if (file == null || !file.exists()) {
            return Collections.emptyList();
        }

        return new ArrayList<>(defineFiles(file));
    }

    private Collection<? extends Vfs.File> defineFiles(File parentFile) {
        List<Vfs.File> result = new ArrayList<>();
        for (File item : listFiles(parentFile)) {
            if (item.isDirectory()) {
                result.addAll(defineFiles(item));
            } else {
                result.add(new SystemFile(SystemDir.this, item));
            }

        }
        return result;
    }

    private static List<File> listFiles(File file) {
        File[] files = file.listFiles();

        if (files != null) {
            return Arrays.asList(files);

        } else {
            return new ArrayList<>();
        }
    }

    public void close() {
        //No Need to close Directory
    }

    @Override
    public String toString() {
        return getPath();
    }
}
