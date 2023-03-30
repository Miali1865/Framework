import util.Util;

public class Main {
    public static void main(String[] args) {
        try {
            Util util = new Util();
            System.out.println(util.fichierXML("C:/Program Files/Apache Software Foundation/Tomcat 9.0/webapps/Framework/testFramework/src/config.xml").size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }    
}
