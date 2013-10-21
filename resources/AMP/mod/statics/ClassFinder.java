package AMP.mod.statics;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class ClassFinder {

    public static Set<Class<?>> getClasses(String packageName) throws Exception {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return getClasses(loader, packageName);
    }

    static Set<Class<?>> getClasses(ClassLoader loader, String packageName){
        Set<Class<?>> classes = new HashSet<Class<?>>();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources;
        try {
            resources = loader.getResources(path);

            if (resources != null) {
                while (resources.hasMoreElements()) {
                    String filePath = resources.nextElement().getFile();
                    // WINDOWS HACK
                    if(filePath.indexOf("%20") > 0)
                        filePath = filePath.replaceAll("%20", " ");
                    if (filePath != null) {
                        if ((filePath.indexOf("!") > 0) & (filePath.indexOf(".jar") > 0)) {
                            String jarPath = filePath.substring(0, filePath.indexOf("!"))
                                    .substring(filePath.indexOf(":") + 1);
                            // WINDOWS HACK
                            if (jarPath.indexOf(":") >= 0) jarPath = jarPath.substring(1);
                            classes.addAll(getFromJARFile(jarPath, path));
                        } else {
                            classes.addAll(
                                    getFromDirectory(new File(filePath), packageName));
                        }
                    }
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return classes;
    }
    static Set<Class<?>> getFromDirectory(File directory, String packageName){
        Set<Class<?>> classes = new HashSet<Class<?>>();
        if (directory.exists()) {
            for (String file : directory.list()) {
                if (file.endsWith(".class")) {
                    String name = packageName + '.' + stripFilenameExtension(file);
                    Class<?> clazz;
                    try {
                        clazz = Class.forName(name);

                        classes.add(clazz);
                    } catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return classes;
    }
    static Set<Class<?>> getFromJARFile(String jar, String packageName){
        Set<Class<?>> classes = new HashSet<Class<?>>();
        JarInputStream jarFile;
        try {
            jarFile = new JarInputStream(new FileInputStream(jar));

            JarEntry jarEntry;
            do {

                jarEntry = jarFile.getNextJarEntry();

                if (jarEntry != null) {
                    String className = jarEntry.getName();
                    if (className.endsWith(".class")) {
                        className = stripFilenameExtension(className);
                        if (className.startsWith(packageName))
                            classes.add(Class.forName(className.replace('/', '.')));
                    }
                }
                jarFile.closeEntry();
            } while (jarEntry != null);
            jarFile.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return classes;
    }

    private static String stripFilenameExtension(String className) {
        System.out.println(className);
        String[] parts = className.split("\\.");
        if(parts.length > 0)
        {
            String stripped = parts[0];
            for(int i = 1; i < parts.length-1; i++)
                stripped += "."+parts[i];
            return stripped;
        }
        return className;
    }
}