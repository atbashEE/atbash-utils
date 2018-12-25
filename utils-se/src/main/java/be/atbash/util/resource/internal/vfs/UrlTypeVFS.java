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

import be.atbash.util.resource.UrlType;
import be.atbash.util.resource.internal.ResourceWalkerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UrlType to be used by Reflections library.
 * This class handles the vfszip and vfsfile protocol of JBOSS files.
 * <p>
 *
 */
public class UrlTypeVFS implements UrlType {
    private static final Logger LOGGER = LoggerFactory.getLogger(JarInputDir.class);

    private static final String[] REPLACE_EXTENSION = new String[]{".ear/", ".jar/", ".war/", ".sar/", ".har/", ".par/"};

    private static final String VFSZIP = "vfszip";
    private static final String VFSFILE = "vfsfile";

    public boolean matches(URL url) {
        return VFSZIP.equals(url.getProtocol()) || VFSFILE.equals(url.getProtocol());
    }

    @Override
    public boolean noScanningNeeded(URL url) {
        return false;
    }

    public Vfs.Dir createDir(final URL url) {
        try {
            URL adaptedUrl = adaptURL(url);
            return new ZipDir(new JarFile(adaptedUrl.getFile()));
        } catch (Exception e) {
            try {
                return new ZipDir(new JarFile(url.getFile()));
            } catch (IOException e1) {
                LOGGER.warn("Could not get URL", e);
                LOGGER.warn("Could not get URL", e1);

            }
        }
        return null;
    }

    private URL adaptURL(URL url) throws MalformedURLException {
        if (VFSZIP.equals(url.getProtocol())) {
            return replaceZipSeparators(url.getPath());
        } else if (VFSFILE.equals(url.getProtocol())) {
            return new URL(url.toString().replace(VFSFILE, "file"));
        } else {
            return url;
        }
    }

    private URL replaceZipSeparators(String path) throws MalformedURLException {
        int pos = 0;
        while (pos != -1) {
            pos = findFirstMatchOfDeployableExtention(path, pos);

            if (pos > 0) {
                File file = new File(path.substring(0, pos - 1));
                if (acceptFile(file)) {
                    return replaceZipSeparatorStartingFrom(path, pos);
                }
            }
        }

        throw new ResourceWalkerException("Unable to identify the real zip file in path '" + path + "'.");
    }

    private boolean acceptFile(File file) {
        return file.exists() && file.isFile();
    }

    private int findFirstMatchOfDeployableExtention(String path, int pos) {
        Pattern p = Pattern.compile("\\.[ejprw]ar/");
        Matcher m = p.matcher(path);
        if (m.find(pos)) {
            return m.end();
        } else {
            return -1;
        }
    }


    private URL replaceZipSeparatorStartingFrom(String path, int pos) throws MalformedURLException {
        String zipFile = path.substring(0, pos - 1);
        String zipPath = path.substring(pos);

        int numSubs = 1;
        for (String ext : REPLACE_EXTENSION) {
            while (zipPath.contains(ext)) {
                zipPath = zipPath.replace(ext, ext.substring(0, 4) + "!");
                numSubs++;
            }
        }

        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < numSubs; i++) {
            prefix.append("zip:");
        }

        if (zipPath.trim().length() == 0) {
            return new URL(prefix.toString() + "/" + zipFile);
        } else {
            return new URL(prefix.toString() + "/" + zipFile + "!" + zipPath);
        }
    }
}
