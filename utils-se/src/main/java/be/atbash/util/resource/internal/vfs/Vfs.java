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

import be.atbash.util.resource.internal.ResourceWalkerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.*;
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
 */
public abstract class Vfs {
    private static final Logger LOGGER = LoggerFactory.getLogger(Vfs.class);

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
     * a matcher and factory for a url
     */
    public interface UrlType {
        boolean matches(URL url) throws Exception;

        Dir createDir(URL url) throws Exception;
    }

    /**
     * tries to create a Dir from the given url, using the given urlTypes
     */
    public static Dir fromURL(URL url) {

        for (UrlType type : DefaultUrlTypes.values()) {
            try {
                if (type.matches(url)) {
                    Dir dir = type.createDir(url);
                    if (dir != null) return dir;
                }
            } catch (Throwable e) {
                LOGGER.warn("could not create Dir using " + type + " from url " + url.toExternalForm() + ". skipping.", e);

            }
        }

        throw new ResourceWalkerException("could not create Vfs.Dir from url, no matching UrlType was found [" + url.toExternalForm() + "]\n" +
                "either use fromURL(final URL url, final List<UrlType> urlTypes) or " +
                "use the static setDefaultURLTypes(final List<UrlType> urlTypes) or addDefaultURLTypes(UrlType urlType) " +
                "with your specialized UrlType.");
    }

    /**
     * try to get {@link java.io.File} from url
     */
    public static java.io.File getFile(URL url) {
        java.io.File file;
        String path;

        try {
            path = url.toURI().getSchemeSpecificPart();
            if ((file = new java.io.File(path)).exists()) return file;
        } catch (URISyntaxException e) {
            // TODO
        }

        try {
            path = URLDecoder.decode(url.getPath(), "UTF-8");
            if (path.contains(".jar!")) path = path.substring(0, path.lastIndexOf(".jar!") + ".jar".length());
            if ((file = new java.io.File(path)).exists()) return file;

        } catch (UnsupportedEncodingException e) {
            // TODO
        }

        try {
            path = url.toExternalForm();
            if (path.startsWith("jar:")) path = path.substring("jar:".length());
            if (path.startsWith("wsjar:")) path = path.substring("wsjar:".length());
            if (path.startsWith("file:")) path = path.substring("file:".length());
            if (path.contains(".jar!")) path = path.substring(0, path.indexOf(".jar!") + ".jar".length());
            if ((file = new java.io.File(path)).exists()) return file;

            path = path.replace("%20", " ");
            if ((file = new java.io.File(path)).exists()) return file;

        } catch (Exception e) {
            // TODO
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
    public enum DefaultUrlTypes implements Vfs.UrlType {
        // FIXME Review Usage
        jarFile {
            public boolean matches(URL url) {
                return url.getProtocol().equals("file") && hasJarFileInPath(url);
            }

            public Vfs.Dir createDir(final URL url) throws Exception {
                return new ZipDir(new JarFile(getFile(url)));
            }
        },

        jarUrl {
            public boolean matches(URL url) {
                return "jar".equals(url.getProtocol()) || "zip".equals(url.getProtocol()) || "wsjar".equals(url.getProtocol());
            }

            public Vfs.Dir createDir(URL url) throws Exception {
                try {
                    URLConnection urlConnection = url.openConnection();
                    if (urlConnection instanceof JarURLConnection) {
                        return new ZipDir(((JarURLConnection) urlConnection).getJarFile());
                    }
                } catch (Throwable e) { /*fallback*/ }
                java.io.File file = getFile(url);
                if (file != null) {
                    return new ZipDir(new JarFile(file));
                }
                return null;
            }
        },

        directory {
            public boolean matches(URL url) {
                if (url.getProtocol().equals("file") && !hasJarFileInPath(url)) {
                    java.io.File file = getFile(url);
                    return file != null && file.isDirectory();
                } else return false;
            }

            public Vfs.Dir createDir(final URL url) throws Exception {
                return new SystemDir(getFile(url));
            }
        },

        jboss_vfs {
            public boolean matches(URL url) {
                return url.getProtocol().equals("vfs");
            }

            public Vfs.Dir createDir(URL url) throws Exception {
                Object content = url.openConnection().getContent();
                Class<?> virtualFile = Thread.currentThread().getContextClassLoader().loadClass("org.jboss.vfs.VirtualFile");
                java.io.File physicalFile = (java.io.File) virtualFile.getMethod("getPhysicalFile").invoke(content);
                String name = (String) virtualFile.getMethod("getName").invoke(content);
                java.io.File file = new java.io.File(physicalFile.getParentFile(), name);
                if (!file.exists() || !file.canRead()) file = physicalFile;
                return file.isDirectory() ? new SystemDir(file) : new ZipDir(new JarFile(file));
            }
        },

        jboss_vfsfile {
            public boolean matches(URL url) throws Exception {
                return "vfszip".equals(url.getProtocol()) || "vfsfile".equals(url.getProtocol());
            }

            public Vfs.Dir createDir(URL url) throws Exception {
                return new UrlTypeVFS().createDir(url);
            }
        },

        bundle {
            public boolean matches(URL url) throws Exception {
                return url.getProtocol().startsWith("bundle");
            }

            public Vfs.Dir createDir(URL url) throws Exception {
                return fromURL((URL) Thread.currentThread().getContextClassLoader().
                        loadClass("org.eclipse.core.runtime.FileLocator").getMethod("resolve", URL.class).invoke(null, url));
            }
        },

        jarInputStream {
            public boolean matches(URL url) throws Exception {
                return url.toExternalForm().contains(".jar");
            }

            public Vfs.Dir createDir(final URL url) throws Exception {
                return new JarInputDir(url);
            }
        }
    }
}