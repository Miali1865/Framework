package model;

import etu1865.framework.MethodAnnotation;
import etu1865.framework.Scope;
import etu1865.framework.type.TypeScope;
import etu1865.framework.ModelView;

@Scope( "SINGLETON" )
public class TestSingleton {

    private static TestSingleton instance;

    private int idtest;
    private String strintest;

    // Constructeur privé pour empêcher l'instanciation directe
    private TestSingleton() {}

    // Méthode statique pour obtenir l'instance unique du singleton
    public static TestSingleton getInstance() {
        if (instance == null) {
            synchronized (TestSingleton.class) {
                if (instance == null) {
                    instance = new TestSingleton();
                }
            }
        }
        return instance;
    }
 
    @MethodAnnotation( url = "/testSingleton")
    public static void testsingleton() {}

    public int getIdtest() {
        return idtest;
    }

    public void setIdtest(int idtest) {
        this.idtest = idtest;
    }

    public String getStrintest() {
        return strintest;
    }

    public void setStrintest(String strintest) {
        this.strintest = strintest;
    }

    
}
