package model;

import etu1865.framework.MethodAnnotation;
import etu1865.framework.AuthProfile;
import etu1865.framework.ModelView;
import etu1865.framework.Scope;

import java.util.HashMap;

@Scope( "SINGLETON" )
public class Emp {

    private static Emp instance;
    public int id;
    public String name ;
    private static int compteurInstances = 0;

    public Emp() {
        // Incrémenter le compteur d'instances à chaque création d'objet
        compteurInstances++;
    }

    public static int getCompteurInstances() {
        return compteurInstances;
    }

    public static Emp getInstance() {
        if (instance == null) {
            instance = new Emp();
        }
        return instance;
    }

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

    @AuthProfile( profile = "admin,mialivola" )
    @MethodAnnotation( url = "/EmpAll")
    public ModelView name() {

        HashMap<String,Object> data = new HashMap<String,Object>();
        data.put("data","Mialivola");
        return new ModelView("test.jsp",data);
    }

    @MethodAnnotation( url = "/sprint8" , paramName = "name")
    public ModelView getSprint(String name) {

        HashMap<String,Object> data = new HashMap<String,Object>();
        this.setName(name);

        data.put("data",this.getName());
        return new ModelView("test.jsp",data);
    }

    @AuthProfile( profile = "admin,mialivola" )
    @MethodAnnotation( url = "/verifProfil")
    public ModelView verifProfil() {

        HashMap<String,Object> data = new HashMap<String,Object>();
        data.put("data","Mialivola");
        return new ModelView("contenu.jsp",data);
    }

    public String save() {
        return(this.getName());
    }

}
