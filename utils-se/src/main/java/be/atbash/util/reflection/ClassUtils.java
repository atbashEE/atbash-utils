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
package be.atbash.util.reflection;

import be.atbash.util.PublicAPI;
import be.atbash.util.SecurityReview;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.*;

/**
 * Utility class used to conveniently interact with <code>Class</code>es, such as acquiring them from the
 * application <code>ClassLoader</code>s and instantiating Objects from them.
 * <p>Class original found in Apache Shiro but modified for enhanced functionality.
 */
@PublicAPI
public final class ClassUtils {

    /**
     * Private internal log instance.
     */
    private static final Logger log = LoggerFactory.getLogger(ClassUtils.class);

    /**
     * The Thread context class loader.
     */
    private static final ClassLoaderAccessor THREAD_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
        @Override
        protected ClassLoader doGetClassLoader() {
            return Thread.currentThread().getContextClassLoader();
        }
    };

    /**
     * The classloader which has loaded the ClassUtils itself.
     */
    private static final ClassLoaderAccessor CLASS_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
        @Override
        protected ClassLoader doGetClassLoader() {
            return ClassUtils.class.getClassLoader();
        }
    };

    /**
     * The System class loader.
     */
    private static final ClassLoaderAccessor SYSTEM_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
        @Override
        protected ClassLoader doGetClassLoader() {
            return ClassLoader.getSystemClassLoader();
        }
    };

    /**
     * Singleton type class.
     */
    private ClassUtils() {
    }

    /**
     * Returns the specified resource by checking the current thread's
     * {@link Thread#getContextClassLoader() context class loader}, then the
     * current ClassLoader (<code>ClassUtils.class.getClassLoader()</code>), then the system/application
     * ClassLoader (<code>ClassLoader.getSystemClassLoader()</code>, in that order, using
     * {@link ClassLoader#getResourceAsStream(String) getResourceAsStream(name)}.
     *
     * @param name the name of the resource to acquire from the classloader(s).
     * @return the InputStream of the resource found, or <code>null</code> if the resource cannot be found from any
     * of the three mentioned ClassLoaders.
     */
    public static InputStream getResourceAsStream(String name) {

        InputStream is = THREAD_CL_ACCESSOR.getResourceStream(name);

        if (is == null) {
            if (log.isTraceEnabled()) {
                log.trace("Resource [" + name + "] was not found via the thread context ClassLoader.  Trying the " +
                        "current ClassLoader...");
            }
            is = CLASS_CL_ACCESSOR.getResourceStream(name);
        }

        if (is == null) {
            if (log.isTraceEnabled()) {
                log.trace("Resource [" + name + "] was not found via the current class loader.  Trying the " +
                        "system/application ClassLoader...");
            }
            is = SYSTEM_CL_ACCESSOR.getResourceStream(name);
        }

        if (is == null && log.isTraceEnabled()) {
            log.trace("Resource [" + name + "] was not found via the thread context, current, or " +
                    "system/application ClassLoaders.  All heuristics have been exhausted.  Returning null.");
        }

        return is;
    }

    /**
     * Return the list of URLs for the resource. It tries to determine is the resource is available on the Thread Context
     * Classloader, the Classloader of the {@link ClassUtils} class or the System Classloader.
     * @param name the name of the resource to acquire from the classloader(s).
     * @return List of URLs pointing to the resource or empty list when resource is not present.
     */
    public static List<URL> getAllResources(String name) {
        Set<URL> result = new HashSet<>(THREAD_CL_ACCESSOR.getResources(name));
        result.addAll(CLASS_CL_ACCESSOR.getResources(name));
        result.addAll(SYSTEM_CL_ACCESSOR.getResources(name));
        return new ArrayList<>(result);
    }

    /**
     * Attempts to load the specified class name from the current thread's
     * {@link Thread#getContextClassLoader() context class loader}, then the
     * current ClassLoader (<code>ClassUtils.class.getClassLoader()</code>), then the system/application
     * ClassLoader (<code>ClassLoader.getSystemClassLoader()</code>, in that order.  If any of them cannot locate
     * the specified class, an <code>UnknownClassException</code> is thrown (our RuntimeException equivalent of
     * the JRE's <code>ClassNotFoundException</code>.
     *
     * @param fqcn the fully qualified class name to load
     * @return the located class
     * @throws UnknownClassException if the class cannot be found.
     */
    public static Class forName(String fqcn) {

        Class clazz = THREAD_CL_ACCESSOR.loadClass(fqcn);

        if (clazz == null) {
            if (log.isTraceEnabled()) {
                log.trace("Unable to load class named [" + fqcn +
                        "] from the thread context ClassLoader.  Trying the current ClassLoader...");
            }
            clazz = CLASS_CL_ACCESSOR.loadClass(fqcn);
        }

        if (clazz == null) {
            if (log.isTraceEnabled()) {
                log.trace("Unable to load class named [" + fqcn + "] from the current ClassLoader.  " +
                        "Trying the system/application ClassLoader...");
            }
            clazz = SYSTEM_CL_ACCESSOR.loadClass(fqcn);
        }

        if (clazz == null) {
            String msg = "Unable to load class named [" + fqcn + "] from the thread context, current, or " +
                    "system/application ClassLoaders.  All heuristics have been exhausted.  Class could not be found.";
            throw new UnknownClassException(msg);
        }

        return clazz;
    }

    /**
     * Checks if the fullyQualifiedClassName is available on any classloader.
     *
     * @param fullyQualifiedClassName The FQCN to check
     * @return Is class available or not.
     */
    public static boolean isAvailable(String fullyQualifiedClassName) {
        try {
            forName(fullyQualifiedClassName);
            return true;
        } catch (UnknownClassException e) {
            return false;
        }
    }

    /**
     * Creates an instance of the class where it is searched by the algorithm described at the
     * {@link ClassUtils#forName(String)} method using the no-arg constructor.
     *
     * @param fqcn The FQCN to instantiate.
     * @param <T>  Convenient generic so cast is not needed.
     * @return The instantiated class.
     * @throws UnknownClassException  if the class cannot be found.
     * @throws InstantiationException if the class instantiation went wrong.
     */
    public static <T> T newInstance(String fqcn) {
        return newInstance(forName(fqcn));
    }

    /**
     * Creates an instance of the class where it is searched by the algorithm described at the
     * {@link ClassUtils#forName(String)} method with a constructor matching the arguments.
     *
     * @param fqcn The FQCN to instantiate.
     * @param <T>  Convenient generic so cast is not needed.
     * @param args The arguments for the constructor
     * @return The instantiated class.
     * @throws UnknownClassException  if the class cannot be found.
     * @throws InstantiationException if the class instantiation went wrong.
     */
    public static <T> T newInstance(String fqcn, Object... args) {
        return newInstance(forName(fqcn), args);
    }

    /**
     * Creates an instance of the class using the no-arg constructor.
     *
     * @param clazz Class which needs to instantiated.
     * @param <T>   Convenient generic so cast is not needed.
     * @return The instantiated class.
     * @throws InstantiationException if the class instantiation went wrong.
     */
    public static <T> T newInstance(Class clazz) {
        if (clazz == null) {
            String msg = "Class method parameter cannot be null.";
            throw new IllegalArgumentException(msg);
        }
        try {
            return (T) clazz.newInstance();
        } catch (Exception e) {
            throw new InstantiationException("Unable to instantiate class [" + clazz.getName() + "]", e);
        }
    }

    /**
     * Creates an instance of the class with a constructor matching the arguments.
     *
     * @param clazz Class which needs to instantiated.
     * @param args  The arguments for the constructor
     * @param <T>   Convenient generic so cast is not needed.
     * @return The instantiated class.
     * @throws InstantiationException if the class instantiation went wrong.
     */
    public static <T> T newInstance(Class clazz, Object... args) {
        Class[] argTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            // Support for null argument
            if (args[i] != null) {
                argTypes[i] = args[i].getClass();
            }
        }
        Constructor ctor = getConstructor(clazz, argTypes);
        return instantiate(ctor, args);
    }

    /**
     * Match the constructor based on the argument types. But argType items can be null!
     * <p>1. First only exact matches are considered.
     * <p>2. If the first step returns no {@link Constructor}, subtypes are considered.
     * <p>3. When not exactly one {@link Constructor} is found, an {@link NoConstructorFoundException} is thrown.
     *
     * @param clazz    Class for which Constructors must be searched
     * @param argTypes List of ClassType of arguments, null values allowed.
     * @return Constructor that match the argumentTypes.
     * @throws NoConstructorFoundException When no constructor (or multiple) is found which matches the argumentTypes.
     */
    static Constructor getConstructor(Class clazz, Class... argTypes) {
        return matchConstructor(clazz, argTypes);
    }

    /**
     * Match the constructor based on the argument types. But argType items can be null!
     * <p>1. First only exact matches are considered.
     * <p>2. If the first step returns no {@link Constructor}, subtypes are considered.
     * <p>3. When not exactly one {@link Constructor} is found, an {@link NoConstructorFoundException} is thrown.
     *
     * @param clazz    Class for which Constructors must be searched
     * @param argTypes List of ClassType of arguments, null values allowed.
     * @return Constructor that match the argumentTypes.
     * @throws NoConstructorFoundException When no constructor (or multiple) is found which matches the argumentTypes.
     */
    private static Constructor matchConstructor(Class clazz, Class[] argTypes) {
        List<Constructor> constructors = matchAllConstructors(clazz, argTypes, true);
        if (constructors.isEmpty()) {
            constructors = matchAllConstructors(clazz, argTypes, false);
        }
        if (constructors.size() != 1) {
            throw new NoConstructorFoundException(clazz, argTypes);
        }
        return constructors.get(0);
    }

    /**
     * Matches all constructors having the argument types. Due to support for null values, multiple constructors can be
     * matched.
     *
     * @param clazz      Class for which Constructors must be searched
     * @param argTypes   List of ClassType of arguments, null values allowed.
     * @param exactMatch is <code>equals</code> or <code>isAssignableFrom</code> used.
     * @return List of Constructors that match the argumentTypes.
     */
    private static List<Constructor> matchAllConstructors(Class clazz, Class[] argTypes, boolean exactMatch) {
        List<Constructor> result = new ArrayList<>();
        for (Constructor constructor : clazz.getConstructors()) {
            if (constructor.getParameterTypes().length == argTypes.length && matchParameterTypes(constructor.getParameterTypes(), argTypes, exactMatch)) {
                result.add(constructor);
            }
        }
        return result;
    }

    /**
     * Method to determine if the 2 lists of class types are compatible taking into account null values. Matching
     * handles null values in the argTypes and consider them as compatible.
     * <p>exactMatch is true -> Classes must be equals
     * <p>exactMatch is false -> isAssignableFrom is used (subTypes also considered compatible)
     *
     * @param parameterTypes List of ClassType of a method. No null values allowed.
     * @param argTypes       List of ClassType of arguments, null values allowed.
     * @param exactMatch     is <code>equals</code> or <code>isAssignableFrom</code> used.
     * @return Do all arguments match the parameter types.
     */
    private static boolean matchParameterTypes(Class[] parameterTypes, Class[] argTypes, boolean exactMatch) {
        boolean result = true;
        for (int i = 0; i < parameterTypes.length; i++) {
            if (argTypes[i] != null) {
                if (exactMatch) {
                    if (!parameterTypes[i].equals(argTypes[i])) {
                        result = false;
                    }
                } else {
                    if (!parameterTypes[i].isAssignableFrom(argTypes[i])) {
                        result = false;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Instantiate the class using the supplied {@link Constructor} and arguments. Arguments are valid for the Constructor
     * due to the logic defined in {@link #matchParameterTypes(Class[], Class[], boolean)}
     *
     * @param ctor Constructor to be used for instantiating the class.
     * @param args The arguments for the constructor
     * @param <T>  Convenience generic typecasting so no cast is needed.
     * @return The new instance.
     */
    private static <T> T instantiate(Constructor ctor, Object... args) {
        try {
            return (T) ctor.newInstance(args);
        } catch (Exception e) {
            String msg = String.format("Unable to instantiate Permission instance with constructor [%s]", ctor);
            throw new InstantiationException(msg, e);
        }
    }

    /**
     * Defines the capabilities of the Class Loader accessor.
     */
    private interface ClassLoaderAccessor {
        /**
         * Tries to load the class defined by the FQCN and returns null if class is not found.
         * Security Check: Make sure that the loaded class name is controlled by developer (so no arbitrary class loaded)
         *
         * @param fqcn FQCN of the class to load.
         * @return The class corresponding ith the FQCN or null if not found.
         */
        @SecurityReview
        Class loadClass(String fqcn);

        /**
         * Tries to locate and open the resource defined by the name and returns null if not found.
         *
         * @param name The name of the resource to open.
         * @return An InputStream to the resource or null if not found.
         */
        InputStream getResourceStream(String name);

        List<URL> getResources(String name);
    }

    /**
     * An implementation that implement the {@link ClassLoaderAccessor} functionality for any classLoader but
     * converts the possible {@link Exception}s to null return values.
     */
    private abstract static class ExceptionIgnoringAccessor implements ClassLoaderAccessor {

        @Override
        public Class loadClass(String fqcn) {
            Class clazz = null;
            ClassLoader cl = getClassLoader();
            // When there was an issue retrieving the ClassLoader, the method return null;
            if (cl != null) {
                try {
                    // Security check : Dynamic loaded class can only be from classpath (so no arbitrary class loaded)
                    //
                    clazz = cl.loadClass(fqcn);
                } catch (ClassNotFoundException e) {
                    if (log.isTraceEnabled()) {
                        log.trace(String.format("Unable to load clazz named [%s] from class loader [%s]", fqcn, cl));
                    }
                }
            }
            return clazz;
        }

        @Override
        public InputStream getResourceStream(String name) {
            InputStream is = null;
            ClassLoader cl = getClassLoader();
            // When there was an issue retrieving the ClassLoader, the method return null;
            if (cl != null) {
                is = cl.getResourceAsStream(name);
            }
            return is;
        }

        /**
         * Retrieves the ClassLoader but but catches any Exception and logs them.
         *
         * @return The ClassLoader or null when there was an Exception.
         */
        final ClassLoader getClassLoader() {
            try {
                return doGetClassLoader();
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.debug("Unable to acquire ClassLoader.", e);
                }
            }
            return null;
        }

        @Override
        public List<URL> getResources(String name) {
            ClassLoader classLoader = doGetClassLoader();
            try {
                return Collections.list(classLoader.getResources(name));
            } catch (IOException e) {
                if (log.isDebugEnabled()) {
                    log.debug(String.format("Unable to read resources %s from ClassLoader %s.", name, classLoader), e);
                }

            }
            return Collections.emptyList();
        }

        /**
         * Subclasses should implement this class and return a {@link ClassLoader}.
         *
         * @return {@link ClassLoader} to be used by this class.
         */
        protected abstract ClassLoader doGetClassLoader();
    }
}
