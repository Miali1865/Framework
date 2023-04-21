package util;

import java.util.List;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.util.ArrayList;
import java.net.URLClassLoader;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.jar.JarFile;
import java.util.Enumeration;
import java.util.jar.JarEntry;



public class Util {
    
    public String processUrl(String nomurl) {
        int manisaslash = nomurl.lastIndexOf("/");
        return nomurl.substring(manisaslash);
    }

    // public List<Class<?>> getClassesInPackage(String packageName) throws Exception {
    //     List<Class<?>> classes = new ArrayList<>();
    //     ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    //     String path = packageName.replace('.', '/');
    //     for (java.net.URL resource : java.util.Collections.list(classLoader.getResources(path))) {
    //         for (String file : new java.io.File(resource.toURI()).list()) {
    //             if (file.endsWith(".class")) {
    //                 String className = packageName + '.' + file.substring(0, file.length() - 6);
    //                 Class<?> clazz = Class.forName(className);
    //                 classes.add(clazz);
    //             }
    //         }
    //     }
    //     return classes;
    // }

    public List<Class<?>> getClassesInPackage(String packageName) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if (resource.getProtocol().equals("jar")) {
                JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
                JarFile jarFile = jarURLConnection.getJarFile();
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (name.endsWith(".class") && name.startsWith(path)) {
                        String className = name.substring(0, name.length() - 6).replace('/', '.');
                        Class<?> clazz = Class.forName(className);
                        classes.add(clazz);
                    }
                }
            } else {
                File directory = new File(resource.toURI());
                File[] files = directory.listFiles();
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".class")) {
                        String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                        Class<?> clazz = Class.forName(className);
                        classes.add(clazz);
                    }
                }
            }
        }
        return classes;
    }
    


    public List<String> fichierXML( String fichier ) throws Exception {
        List<String> listPackage = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(fichier);
        doc.getDocumentElement().normalize();

        Element packagesElement = (Element) doc.getElementsByTagName("classes").item(0);
        NodeList packageNodes = packagesElement.getElementsByTagName("package");

        for(int i = 0; i < packageNodes.getLength(); i++) {
            Element packageElement = (Element) packageNodes.item(i);
            String packageName = packageElement.getTextContent();
            listPackage.add(packageName);
        }
        return listPackage;
    }
}