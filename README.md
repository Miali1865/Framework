# Condition :
* Vous devez avoir un tomcat 9.0 
* Vous ne pouvez pas utiliser neatbeans


# Installation : 
- créer un dossier WEB-INF
- y créer un sous dossier lib
- copier framework.jar dans WEB-INF/lib et si nécessaire rajouter servlet-api.jar
- toujours dans WEB-INF , créer deux sous-dossiers : web.xml et config.xml ( ils seront configurer un peut plus tard )
- mettre au même répertoire que WEB-INF les fichiers .jsp dont vous avez besoin


# Utilisation : 

* Configurer web.xml comme suite : 


        <?xml version="1.0" encoding="UTF-8"?>
        <web-app version="3.1" xmlns="http://xmlns.jcp.org/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd">

            <servlet>
                <servlet-name>FrontServlet</servlet-name>
                <servlet-class>etu1865.framework.servlet.FrontServlet</servlet-class>
            </servlet>
            <servlet-mapping>
                <servlet-name>FrontServlet</servlet-name>
                <url-pattern>/</url-pattern>
            </servlet-mapping>
            <session-config>
                <session-timeout>30</session-timeout>
            </session-config>
        </web-app>


FrontServlet contient toutes les fonctions importantes pour notre framework ( contenu du servlet-name )

servlet-class définie le package de notre classe


* Configurer config.xml en mettant dans la balise "package" tous les packages que vous juger nécessaire pour votre projet 

Exemple : 

    <?xml version="1.0" encoding="UTF-8"?>
    <classes>s
        <package>model</package>
        <package>etu1865</package>
    </classes>


Dans cet exemple , "model" et "etu1865" sont les packages nécessaires

* Afin d'utliser plusieurs fonctionnalités veuillez utiliser le profil : "admin" ou "mialivola"

* Lors de la connexion , on enregistre les profils dans une session

* Veuillez configurer tous vos attributs paraillement

* Si vous souhaitez annoter votre classe en Singleton , rajouter @Scope( "SINGLETON" ) comme la classe Emp

    @Scope( "SINGLETON" )
    public class Emp {

* Configurer votre fichier .jsp configurer l'action de votre formulaire comme suite `"http://localhost:8080/testFramework/EmpAll"`

Exemple : 

        <form action="http://localhost:8080/testFramework/EmpAll" method="post">

            <input type="text" name="Name">

            <input type="submit" value="Valider">

        </form>