package etu1865.framework.servlet;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;

import javax.management.MXBean;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.swing.text.View;

/**
 *
 * @author mialivola
 */
import etu1865.framework.MethodAnnotation;
import etu1865.framework.AuthProfile;
import etu1865.framework.FileUpload;
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
                                // out.println("Name classe = "+a.getClassName()+" et name methode = "+a.getMethod());
                                // out.println("Key "+key);
                            }
                            
                        }
                    }
                    // if(cl.isAnnotationPresent(Scope.class)) {
                    //     Scope scopeAnnotation = cl.getAnnotation(Scope.class);
                    //     String getTypeClasse = scopeAnnotation.value();

                    //     // Obtenir la classe contenant la méthode annotée
                    //     Class<?> containingClass = cl;
                    //     String className = containingClass.getName();
                    //     if(getTypeClasse.equalsIgnoreCase("SINGLETON")) {
                    //         out.println("La classe contenant l'annotation Scope est : " + className);
                    //         out.println("Valeur azoo  : " + getTypeClasse);
                    //         mapSingleton.put(cl, "SINGLETON");
                    //     }
                    //     out.println("Metyy vee "+(String) mapSingleton.get(cl));
                    // }
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
        String profileConnecte = request.getParameter("profile");

        try {
            Mapping mapping = MappingUrls.get(nomurl);
            // Sprint 11
            HttpSession session = request.getSession();
            String listProfil = (String) session.getAttribute("isConnected");
            if (listProfil == null) {
                listProfil = profileConnecte;
                // out.println("Manambotra session vaovao ndray izy");
            } else {
                // out.println("Le taloha no nalaina");
            }
            session.setAttribute("isConnected", listProfil);
            String testlist = (String) session.getAttribute("isConnected");
            out.println(testlist);
            out.println("Profil connecté : " + testlist);
            

            if( mapping == null ) {
                out.println("Not Found");
            } 
              

            // out.println("Ito zao ilay mapping : "+mapping);
            Class<?> classe = Class.forName(mapping.getClassName());
            Object o = classe.getDeclaredConstructor().newInstance();
            boolean isConnectedFind = false;

            // Vérification si le profil peux se connecter Sprint 10
            String methodSprint8 = mapping.getMethod();
            String stringClasse = mapping.getClassName();
            Method methods = util.getMethodByClassName(stringClasse, methodSprint8);


            // // Sprint 11
            if(methods.isAnnotationPresent(AuthProfile.class)) {
                AuthProfile autorisationAcces = methods.getAnnotation(AuthProfile.class);
                String getProfile = autorisationAcces.profile();
                String[] profilAcces = getProfile.split(",");
                for(int i = 0; i < profilAcces.length; i++) {
                    if(profilAcces[i].equalsIgnoreCase(testlist)) {
                        isConnectedFind = true;
                    }
                } 
            }
            
            // // Sprint 8
            if(methods.isAnnotationPresent(MethodAnnotation.class)) {
                MethodAnnotation annotation = methods.getAnnotation(MethodAnnotation.class);
                String paramName = annotation.paramName();
                String[] getGroupParamName = paramName.split(",");
                Class<?>[] paramTypes = methods.getParameterTypes();
                Object[] objectTypes = new Object[paramTypes.length];

                

                if( getGroupParamName.length == paramTypes.length ) {

                    for(int i = 0; i < getGroupParamName.length; i++) {
                        String paramNom = getGroupParamName[i].trim();
                        // out.println("Ty mintsy no olana "+paramNom);
                        Class<?> paramType = paramTypes[i];
                        String modifiedParamNom = paramNom.substring(0, 1).toUpperCase() + paramNom.substring(1);


                        if (paramType == String.class) {
                            objectTypes[i] = request.getParameter(paramNom);
                            out.println(objectTypes[i]);
                        } else if (paramType == int.class || paramType == Integer.class) {
                            objectTypes[i] = Integer.parseInt(request.getParameter(paramNom));
                        } else if (paramType == double.class || paramType == Double.class) {
                            objectTypes[i] = Double.parseDouble(request.getParameter(paramNom));
                        } else if (paramType == boolean.class || paramType == Boolean.class) {
                            objectTypes[i] = Boolean.parseBoolean(request.getParameter(paramNom));
                        } else if (paramType == FileUpload.class ) {
                            objectTypes[i] = util.getUploadFile(request, paramNom);
                        } else {
                            // Autres types de données peuvent être gérés de manière similaire
                            throw new IllegalArgumentException("Type de paramètre non géré : " + paramType.getName());
                        }
                        o.getClass().getDeclaredMethod("set" + modifiedParamNom, paramType).invoke(o, objectTypes[i]);
                    }
                } else {
                    // ModelView sprint 11
                    Enumeration<String> enume = request.getParameterNames();
                    // Sprint 7
                    while (enume.hasMoreElements()) {

                        String n = enume.nextElement();
                        Field[] fields = o.getClass().getDeclaredFields();
                        Field field = fields[1];

                        if(field == null){
                            continue;
                        }
                        if( n.equalsIgnoreCase("profile")) {
                            out.println("Tsy mety");
                        } else {
                            Object value = null;
                            Class<?> parameterType = o.getClass().getDeclaredMethod("set" + n , field.getType()).getParameterTypes()[0];
                            out.println("Ito no tena tiako jerena "+parameterType);

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
                        // ModelView sprint 7
                        ModelView mview = (ModelView) o.getClass().getMethod(mapping.getMethod()).invoke(o);
                        out.println("Nom vaovao : "+o.getClass().getDeclaredMethod("save").invoke(o));
                        out.println("View : "+mview.getView());

                        HashMap<String,Object> data = new HashMap<String,Object>();
                        data = mview.getData();
                        // out.print(data);

                        if(data == null) {
                            out.print("Votre data est null");
                        }

                        data.put("data", o.getClass().getDeclaredMethod("save").invoke(o));

                        if (o.getClass().isAnnotationPresent(Scope.class)) {
                            Scope isScope = o.getClass().getAnnotation(Scope.class);
                            out.println("ito ilay classe " + o.getClass().getSimpleName());
                            String ifSingleton = isScope.value();
                            out.println("Valeur de scope = " + ifSingleton);
                        
                            if (ifSingleton.equalsIgnoreCase("SINGLETON")) {
                                // Ceci est un singleton
                                if (!data.containsKey("data")) {
                                    // La première fois qu'on récupère des données, les stocker dans le HashMap
                                    Object newData = o.getClass().getDeclaredMethod("save").invoke(o);
                                    data.put("data", newData);
                                }
                                Object firstData = data.get("data");
                                request.setAttribute("data", firstData);
                            } else {
                                // Créer une nouvelle donnée dans le HashMap
                                String key = o.getClass().getSimpleName();
                                if (!data.containsKey(key)) {
                                    Object newData = o.getClass().getDeclaredMethod("save").invoke(o);
                                    data.put(key, newData);
                                }
                                Object dataObject = data.get(key);
                                request.setAttribute(key, dataObject);
                            }
                        }                                               
                        out.println(request.getAttribute("data"));
                        RequestDispatcher dispatcher = request.getRequestDispatcher(mview.getView());
                        dispatcher.forward(request, response);
                    }

                }
                if( isConnectedFind ) {
                    ModelView mviewSpring8 = (ModelView) o.getClass().getMethod(mapping.getMethod(), paramTypes ).invoke(o,objectTypes);
                    HashMap<String, Object> dataSpring8 = new HashMap<String, Object>();
                    dataSpring8 = mviewSpring8.getData();
                    if(dataSpring8 == null) {
                        out.println("data null");
                    }
                    if(dataSpring8 != null) {
                        for(String cle : dataSpring8.keySet()) {
                            Object dataoObject = dataSpring8.get(cle);
                            request.setAttribute(cle,dataoObject);
                        }
                        RequestDispatcher dispatch = request.getRequestDispatcher(mviewSpring8.getView());
                        dispatch.forward(request, response);
                    }
                } else {
                    out.println("Permission denied");
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
