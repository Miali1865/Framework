package etu1865.framework.servlet;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import javax.servlet.*;
import javax.servlet.http.*;
/**
 *
 * @author mialivola
 */
import etu1865.framework.MethodAnnotation;
import etu1865.framework.Scope;
import etu1865.framework.Mapping;
import etu1865.framework.ModelView;
import java.lang.reflect.Method;
import javax.servlet.RequestDispatcher;
import util.Util;
import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Collection;


public class FrontServlet extends HttpServlet {

    HashMap<String,Mapping> MappingUrls = new HashMap<String,Mapping>();
    HashMap<Class<?>,Object> mapSingleton = new HashMap<Class<?>,Object>();

    protected Util util;

    public void init(PrintWriter out) throws ServletException {
        try {    
            this.util = new Util();
            this.MappingUrls = new HashMap<>();
            this.mapSingleton = new HashMap<>();
            
            
            List<String> listPackage = util.fichierXML("C:/Program Files/Apache Software Foundation/Tomcat 9.0/webapps/SprintTest/WEB-INF/config.xml");  
            
            
            for(String list : listPackage ) {

                List<Class<?>> allClass = util.getClassesInPackage(list);
                Mapping mapping;
                Method[] allMethods;
                Type[] allTypes;
        
                for(Class<?> cl : allClass) {
                    allMethods = cl.getMethods();
    
                    for(Method method : allMethods) {

                        if(method.isAnnotationPresent(MethodAnnotation.class)) {
                            mapping = new Mapping();
                            mapping.setClassName(cl.getName());
                            mapping.setMethod(method.getName());
    
                            MappingUrls.put(method.getAnnotation(MethodAnnotation.class).url(), mapping);
                            
                            for(String key : MappingUrls.keySet()) {
                                Mapping a = MappingUrls.get(key);
                                out.println("Name classe = "+a.getClassName()+" et name methode = "+a.getMethod());
                                out.println("Key "+key);
                            }
                            
                        }
                    }
                    if(cl.isAnnotationPresent(Scope.class)) {
                        Scope scopeAnnotation = cl.getAnnotation(Scope.class);
                        String getTypeClasse = scopeAnnotation.value();

                        // Obtenir la classe contenant la méthode annotée
                        Class<?> containingClass = cl;
                        String className = containingClass.getName();
                        if(getTypeClasse.equalsIgnoreCase("SINGLETON")) {
                            out.println("La classe contenant l'annotation Scope est : " + className);
                            out.println("Valeur azoo  : " + getTypeClasse);
                            mapSingleton.put(cl, "SINGLETON");
                        }
                        out.println("Metyy vee "+(String) mapSingleton.get(cl));
                    }
                }
            }

        } catch (Exception e) {
            try {
                throw new Exception("Not found");
            } catch (Exception i) {
                // TODO: handle exception
            }
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getRequestURL().toString();

        PrintWriter out = response.getWriter();
        this.init(out);
        String nomurl = util.processUrl(url);

        try {
            Mapping mapping = MappingUrls.get(nomurl);
 

            if( mapping == null ) {
                out.println("Not Found");
            } 
                
            out.println("Ito zao ilay mapping : "+mapping);
            Class<?> classe = Class.forName(mapping.getClassName());
            Object o = classe.getDeclaredConstructor().newInstance();
            ModelView mview = (ModelView) o.getClass().getMethod(mapping.getMethod()).invoke(o);
            Enumeration<String> enume = request.getParameterNames();
            

            while (enume.hasMoreElements()) {
                String n = enume.nextElement();
                Field[] fields = o.getClass().getDeclaredFields();
                Field field = fields[1];

                if(field == null){
                    continue;
                }

                Object value = null;
                Class<?> parameterType = o.getClass().getDeclaredMethod("set" + n , field.getType()).getParameterTypes()[0];

                if (parameterType == String.class) {
                    value = request.getParameter(n);
                } else if (parameterType == int.class || parameterType == Integer.class) {
                    value = Integer.parseInt(request.getParameter(n));
                } else if (parameterType == double.class || parameterType == Double.class) {
                    value = Double.parseDouble(request.getParameter(n));
                } else if (parameterType == boolean.class || parameterType == Boolean.class) {
                    value = Boolean.parseBoolean(request.getParameter(n));
                } else {
                    // Autres types de données peuvent être gérés de manière similaire
                    throw new IllegalArgumentException("Type de paramètre non géré : " + parameterType.getName());
                }

                o.getClass().getDeclaredMethod("set" + n, parameterType).invoke(o, value);
            }
            out.println("Nom vaovao : "+o.getClass().getDeclaredMethod("save").invoke(o));
            out.println("View : "+mview.getView());

            HashMap<String, Object> data = new HashMap<String, Object>();
            data = mview.getData();
            
            if (data == null) {
                out.print("Votre data est null");
            }
            
            data.put("data", o.getClass().getDeclaredMethod("save").invoke(o));
            
            if (o.getClass().isAnnotationPresent(Scope.class)) {
                Scope isScope = o.getClass().getAnnotation(Scope.class);
                String ifSingleton = isScope.value();
                out.println("Valeur de scope = " + ifSingleton);
            
                if (ifSingleton.equalsIgnoreCase("SINGLETON")) {
                    // Créer une copie des données existantes de la requête
                    HashMap<String, Object> requestData = new HashMap<String, Object>();
                    Enumeration<String> attributeNames = request.getAttributeNames();
                    while (attributeNames.hasMoreElements()) {
                        String attributeName = attributeNames.nextElement();
                        Object attributeValue = request.getAttribute(attributeName);
                        requestData.put(attributeName, attributeValue);
                    }
                    
                    // Ajouter les nouvelles données à la HashMap existante
                    data.putAll(requestData);
                    out.println("Tokony tsy hiova ny ato ");
                } else {
                    // Créer une nouvelle donnée de HashMap
                    for (String key : data.keySet()) {
                        Object dataObject = data.get(key);
                        request.setAttribute(key, dataObject);
                    }
                }
            }
            
            Collection<Object> values = data.values();
            for (Object value : values) {
                out.println(value);
            }
            
            
            
            // RequestDispatcher dispatcher = request.getRequestDispatcher(mview.getView());
            // dispatcher.forward(request, response);


        } catch (Exception e) {
            try {
                throw new Exception("Not found");
            } catch (Exception i) {
                // TODO: handle exception
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response); 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }




}
