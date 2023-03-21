package util;

import java.util.List;
import java.util.ArrayList;

public class Util {

    public String processUrl(String nomurl , String context) {
        context+="/";
        int idcontext = nomurl.indexOf(context);
        String url = nomurl.substring(idcontext + context.length());
        return url;
    }
    

    public List<Class<?>> getClassesInPackage(String packageName) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        for (java.net.URL resource : java.util.Collections.list(classLoader.getResources(path))) {
            for (String file : new java.io.File(resource.toURI()).list()) {
                if (file.endsWith(".class")) {
                    String className = packageName + '.' + file.substring(0, file.length() - 6);
                    Class<?> clazz = Class.forName(className);
                    classes.add(clazz);
                }
            }
        }
        return classes;
    }
}
