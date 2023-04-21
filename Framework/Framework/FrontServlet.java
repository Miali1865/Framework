package etu1865.framework.servlet;

import java.io.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import javax.servlet.*;
import javax.servlet.http.*;
/**
 *
 * @author mialivola
 */
import etu1865.framework.MethodAnnotation;
import etu1865.framework.Mapping;
import etu1865.framework.ModelView;
import java.lang.reflect.Method;
import javax.servlet.RequestDispatcher;
import util.Util;
import java.util.List;
import java.util.ArrayList;

public class FrontServlet extends HttpServlet {

    HashMap<String,Mapping> MappingUrls = new HashMap<String,Mapping>();
    protected Util util;

    public void init(PrintWriter out) throws ServletException {
        try {    
            this.util = new Util();
            this.MappingUrls = new HashMap<>();
            
            
            List<String> listPackage = util.fichierXML("C:/Program Files/Apache Software Foundation/Tomcat 9.0/webapps/SprintTest/WEB-INF/config.xml");  
            
            
            for(String list : listPackage ) {

                List<Class<?>> allClass = util.getClassesInPackage(list);
                Mapping mapping;
                Method[] allMethods;
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
                }
            }

        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getRequestURL().toString();

        PrintWriter out = response.getWriter();
        this.init(out);
        //String nomurl = util.processUrl(url, request.getContextPath()); 
        String nomurl = util.processUrl(url);

        try {
            Mapping mapping = MappingUrls.get(nomurl);


            if( mapping == null ) {
                out.println("Not Found");
            } 
                
            out.print(mapping);
            Class<?> classe = Class.forName(mapping.getClassName());
            Object o = classe.getDeclaredConstructor().newInstance();
            ModelView mview = (ModelView) o.getClass().getMethod(mapping.getMethod()).invoke(o);

            HashMap<String,Object> data = new HashMap<String,Object>();
            data = mview.getData();
            // out.print(data);

            if(data == null) {
                out.print("Votre data est null");
            }

            for( String key : data.keySet() ) {

                Object dataObjet = data.get(key);
                request.setAttribute(key,dataObjet);
            }
            // out.print("Votre key "+request.getAttribute("data"));

            RequestDispatcher dispatcher = request.getRequestDispatcher(mview.getView());
            dispatcher.forward(request, response);

        } catch (Exception e) {
            out.print(e.getMessage());
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
