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

import be.atbash.util.exception.AtbashUnexpectedException;
import be.atbash.util.exception.ResourceURLHandlingException;
import be.atbash.util.resource.UrlType;
import be.atbash.util.resource.internal.ResourceWalkerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarFile;

/**
 * a simple virtual file system bridge
 * <p>use the {@link Vfs#fromURL(java.net.URL)} to get a {@link Vfs.Dir},
 * then use {@link Vfs.Dir#getFiles()} to iterate over the {@link Vfs.File}
 * <p>for example:
 * <pre>
 *      Vfs.Dir dir = Vfs.fromURL(url);
 *      Iterable<Vfs.File> files = dir.getFiles();
 *      for (Vfs.File file : files) {
 *          InputStream is = file.openInputStream();
 *      }
 * </pre>
 * <p>{@link Vfs#fromURL(java.net.URL)} uses static {@link Vfs.DefaultUrlTypes} to resolve URLs.
 * It contains VfsTypes for handling for common resources such as local jar file, local directory, jar url, jar input stream and more.
 *
 * Additional type handling can be registered by a call to {@link Vfs#registerURLType(UrlType)}
 */
public abstract class Vfs {
    private static final Logger LOGGER = LoggerFactory.getLogger(Vfs.class);
    private static final String JAR_MARKER = ".jar!";

    private static final List<UrlType> defaultUrlTypes;

    static {
        defaultUrlTypes = new ArrayList<>();
        Collections.addAll(defaultUrlTypes, DefaultUrlTypes.values());
    }

    /**
     * an abstract vfs dir
     */
    public interface Dir {
        String getPath();

        Iterable<File> getFiles();

        void close();
    }

    /**
     * an abstract vfs file
     */
    public interface File {
        String getName();

        String getRelativePath();
    }

    /**
     * add a static default url types to the beginning of the default url types list. can be used to statically plug in urlTypes
     */
    public static void registerURLType(UrlType urlType) {
        defaultUrlTypes.add(0, urlType);
    }

    /**
     * tries to create a Dir from the given url, using the given urlTypes.
     * @return null when URL doesn't need to be scanned, a non null value is the directory to scan.
     */
    public static Dir fromURL(URL url) {

        for (UrlType type : defaultUrlTypes) {
            try {
                if (type.matches(url) ) {
                    if (type.noScanningNeeded(url)) {
                        return null;
                    } else {
                        Dir dir = type.createDir(url);
                        if (dir != null) {
                            return dir;
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.warn("could not create Dir using " + type + " from url " + url.toExternalForm() + ". skipping.", e);

            }
        }

        throw new ResourceWalkerException("could not create Vfs.Dir from url, no matching UrlType was found [" + url.toExternalForm() + "]\n" +
                "either use fromURL(URL url, List<UrlType> urlTypes) or " +
                "use the static setDefaultURLTypes(List<UrlType> urlTypes) or addDefaultURLTypes(UrlType urlType) " +
                "with your specialized UrlType.");
    }

    /**
     * try to get {@link java.io.File} from url.
     */
    public static java.io.File getFile(URL url) {
        java.io.File file;
        String path;

        try {
            path = url.toURI().getSchemeSpecificPart();
            file = new java.io.File(path);
            if (file.exists()) {
                return file;
            }
        } catch (URISyntaxException e) {
            throw new AtbashUnexpectedException(e);
        }

        try {
            path = URLDecoder.decode(url.getPath(), "UTF-8");
            if (path.contains(JAR_MARKER)) {
                path = path.substring(0, path.lastIndexOf(JAR_MARKER) + ".jar".length());
            }
            file = new java.io.File(path);
            if (file.exists()) {
                return file;
            }

        } catch (UnsupportedEncodingException e) {
            throw new AtbashUnexpectedException(e);
        }

        try {
            path = url.toExternalForm();
            if (path.startsWith("jar:")) {
                path = path.substring("jar:".length());
            }
            if (path.startsWith("wsjar:")) {
                path = path.substring("wsjar:".length());
            }
            if (path.startsWith("file:")) {
                path = path.substring("file:".length());
            }
            if (path.contains(JAR_MARKER)) {
                path = path.substring(0, path.indexOf(JAR_MARKER) + ".jar".length());
            }
            file = new java.io.File(path);
            if (file.exists()) {
                return file;
            }

            path = path.replace("%20", " ");
            file = new java.io.File(path);
            if (file.exists()) {
                return file;
            }

        } catch (Exception e) {
            throw new AtbashUnexpectedException(e);
        }

        return null;
    }

    private static boolean hasJarFileInPath(URL url) {
        return url.toExternalForm().matches(".*\\.jar(\\!.*|$)");
    }

    /**
     * default url types used by {@link Vfs#fromURL(java.net.URL)}
     * <p>
     * <p>jarFile - creates a {@link ZipDir} over jar file
     * <p>jarUrl - creates a {@link ZipDir} over a jar url (contains ".jar!/" in it's name), using Java's {@link JarURLConnection}
     * <p>directory - creates a {@link SystemDir} over a file system directory
     * <p>jboss vfs - for protocols vfs, using jboss vfs (should be provided in classpath)
     * <p>jboss vfsfile - creates a {@link UrlTypeVFS} for protocols vfszip and vfsfile.
     * <p>bundle - for bundle protocol, using eclipse FileLocator (should be provided in classpath)
     * <p>jarInputStream - creates a {@link JarInputDir} over jar files, using Java's JarInputStream
     */
    @SuppressWarnings("squid:S115")
    public enum DefaultUrlTypes implements UrlType {

        jarFile {
            @Override
            public boolean matches(URL url) {
                return url.getProtocol().equals("file") && hasJarFileInPath(url);
            }

            @Override
            public Vfs.Dir createDir(URL url) throws Exception {
                java.io.File file = getFile(url);
                if (file == null) {
                    throw new ResourceURLHandlingException(String.format("Unable to locate the Directory within the JAR File '%s'", url.toExternalForm()));
                }
                return new ZipDir(new JarFile(file));
            }

            @Override
            public boolean noScanningNeeded(URL url) {
                return false;
            }
        },

        jarUrl {
            @Override
            public boolean matches(URL url) {
                return "jar".equals(url.getProtocol()) || "zip".equals(url.getProtocol()) || "wsjar".equals(url.getProtocol());
            }

            @Override
            public boolean noScanningNeeded(URL url) {
                return false;
            }

            @Override
            public Vfs.Dir createDir(URL url) throws Exception {
                try {
                    URLConnection urlConnection = url.openConnection();
                    if (urlConnection instanceof JarURLConnection) {
                        return new ZipDir(((JarURLConnection) urlConnection).getJarFile());
                    }
                } catch (Exception e) { /*fallback*/ }
                java.io.File file = getFile(url);
                if (file != null) {
                    return new ZipDir(new JarFile(file));
                }
                return null;
            }
        },

        directory {
            @Override
            public boolean matches(URL url) {
                if (url.getProtocol().equals("file") && !hasJarFileInPath(url)) {
                    java.io.File file = getFile(url);
                    return file != null && file.isDirectory();
                } else return false;
            }

            @Override
            public boolean noScanningNeeded(URL url) {
                return false;
            }

            @Override
            public Vfs.Dir createDir(URL url) throws Exception {
                return new SystemDir(getFile(url));
            }
        },

        jboss_vfs {
            @Override
            public boolean matches(URL url) {
                return url.getProtocol().equals("vfs");
            }

            @Override
            public boolean noScanningNeeded(URL url) {
                return url.getPath().endsWith("META-INF/MANIFEST.MF");  // Using the META-INF/MANIFEST.MF resource for finding JAR entries on Java SE
                // Gives a problem when using on JBoss (with VFS) as it adds an additional URL which can't be processed.
            }

            @Override
            public Vfs.Dir createDir(URL url) throws Exception {
                Object content = url.openConnection().getContent();
                Class<?> virtualFile = Thread.currentThread().getContextClassLoader().loadClass("org.jboss.vfs.VirtualFile");
                java.io.File physicalFile = (java.io.File) virtualFile.getMethod("getPhysicalFile").invoke(content);
                String name = (String) virtualFile.getMethod("getName").invoke(content);
                java.io.File file = new java.io.File(physicalFile.getParentFile(), name);  // Name points to /content dir -> jar file is in the parent
                if (!file.exists() || !file.canRead()) {
                    file = physicalFile;
                }
                return file.isDirectory() ? new SystemDir(file) : new ZipDir(new JarFile(file));
            }
        },

        jboss_vfsfile {
            // TODO Do we need to support this?
            @Override
            public boolean matches(URL url) {
                return "vfszip".equals(url.getProtocol()) || "vfsfile".equals(url.getProtocol());
            }

            @Override
            public boolean noScanningNeeded(URL url) {
                return false;
            }

            @Override
            public Vfs.Dir createDir(URL url) throws Exception {
                return new UrlTypeVFS().createDir(url);
            }
        },

        bundle {
            // TODO Do we need to support this?
            @Override
            public boolean matches(URL url) {
                return url.getProtocol().startsWith("bundle");
            }

            @Override
            public boolean noScanningNeeded(URL url) {
                return false;
            }

            @Override
            public Vfs.Dir createDir(URL url) throws Exception {
                return fromURL((URL) Thread.currentThread().getContextClassLoader().
                        loadClass("org.eclipse.core.runtime.FileLocator").getMethod("resolve", URL.class).invoke(null, url));
            }
        },

        jarInputStream {
            @Override
            public boolean matches(URL url) {
                return url.toExternalForm().contains(".jar");
            }

            @Override
            public boolean noScanningNeeded(URL url) {
                return false;
            }

            @Override
            public Vfs.Dir createDir(URL url) throws Exception {
                return new JarInputDir(url);
            }
        }
    }
}