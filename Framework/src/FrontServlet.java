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

    HashMap<String,Mapping> MappingUrls;
    protected Util util;

    @Override
    public void init() throws ServletException {
        try {
            
            this.util = new Util();
            this.MappingUrls = new HashMap<>();
            List<Class<?>> allClass = util.getClassesInPackage("model");
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
        out.print("url: "+util.processUrl(url, request.getContextPath()));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>




}
