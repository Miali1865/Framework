package model;

import etu1865.framework.MethodAnnotation;
import etu1865.framework.AuthProfile;
import etu1865.framework.FileUpload;
import etu1865.framework.ModelView;
import etu1865.framework.Scope;

import java.util.HashMap;

@Scope( "SINGLETON" )
public class Emp {

    public int id;
    public String name ;

    public FileUpload getFile() {
        return fileUpload;
    }

    public void setFile(FileUpload fileUpload) {
        this.fileUpload = fileUpload;
    }

    private FileUpload fileUpload;

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
        // return new ModelView();

    }

    @AuthProfile( profile = "admin,mialivola" )
    @MethodAnnotation( url = "/sprint8" , paramName = "name")
    public ModelView getSprint(String name) {

        HashMap<String,Object> data = new HashMap<String,Object>();
        this.setName(name);

        data.put("data",this.getName());
        return new ModelView("test.jsp",data);
    }

    @MethodAnnotation( url = "/verifProfil")
    public ModelView verifProfil() {

        HashMap<String,Object> data = new HashMap<String,Object>();
        data.put("data","Mialivola");
        return new ModelView("contenu.jsp",data);
    }

    @MethodAnnotation( url = "/uploadFile", paramName = "File" )
    public ModelView uploadFile( FileUpload fileUpload ) {

        HashMap<String,Object> data = new HashMap<String,Object>();
        this.setFile(fileUpload);
        data.put("data",this.getFile());
        return new ModelView("file.jsp",data);
    }

    public String save() {
        return(this.getName());
    }

}
