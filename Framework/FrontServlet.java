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
                            mapping.setClassName(cl.getSimpleName());
                            mapping.setMethod(method.getName());
    
                            MappingUrls.put(method.getAnnotation(MethodAnnotation.class).url(), mapping);

                            for(String key : MappingUrls.keySet()) {
                                Mapping a = MappingUrls.get(key);
                                out.println("Name classe = "+a.getClassName()+" et name methode = "+a.getMethod());
                            }
                            
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getRequestURL().toString();

        PrintWriter out = response.getWriter();
        this.init(out);
        out.print("url: "+util.processUrl(url, request.getContextPath()));
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
