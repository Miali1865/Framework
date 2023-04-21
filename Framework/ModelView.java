package etu1865.framework;

import java.util.HashMap;

public class ModelView {
    
    String view;
    HashMap<String,Object> data = new HashMap<String,Object>();


    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public ModelView() {}

    public ModelView(String view , HashMap<String,Object> data ) {
        this.setView(view);
        this.setData(data);
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

}