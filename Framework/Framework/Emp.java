package model;

import etu1865.framework.MethodAnnotation;
import etu1865.framework.ModelView;
import java.util.HashMap;

public class Emp {
    int id;
    String name;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    @MethodAnnotation(url = "/EmpAll")
    public ModelView name() {

        HashMap<String,Object> data = new HashMap<String,Object>();
        data.put("name","Mialivola");
        return new ModelView("test.jsp",data);
    }



}
