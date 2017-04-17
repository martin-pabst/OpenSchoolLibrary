package de.sp.tools.file;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class JarFileIncluder {


public static void addJar(File file) throws Exception {
    Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
    method.setAccessible(true);
    method.invoke(ClassLoader.getSystemClassLoader(), new Object[]{file.toURI().toURL()});
}

}