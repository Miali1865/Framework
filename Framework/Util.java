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
