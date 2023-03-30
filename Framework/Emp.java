package model;

import etu1865.framework.MethodAnnotation;

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

    
    @MethodAnnotation(url = "EmpAll")
    public void FindAll() {}

}
