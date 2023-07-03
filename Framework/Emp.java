package model;

import etu1865.framework.MethodAnnotation;
import etu1865.framework.ModelView;
import etu1865.framework.Scope;

import java.util.HashMap;

@Scope( "SINGLETON" )
public class Emp {

    public int id;
    public String name ;


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

    @MethodAnnotation( url = "/EmpAll")
    public ModelView name() {

        HashMap<String,Object> data = new HashMap<String,Object>();
        data.put("data","Mialivola");
        return new ModelView("test.jsp",data);
    }

    public String save() {
        return(this.getName());
    }

}
