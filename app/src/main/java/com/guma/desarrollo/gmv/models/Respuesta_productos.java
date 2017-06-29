package com.guma.desarrollo.gmv.models;

import com.guma.desarrollo.core.Productos;
import com.guma.desarrollo.core.Usuario;

import java.util.ArrayList;

/**
 * Created by alder.hernandez on 03/03/2017.
 */

public class Respuesta_productos {
    private static int count;
    private ArrayList<Productos> results;

    public ArrayList<Productos> getResults() {
        return results;
    }
    public  int getCount() {
        return count = results.size();
    }
    public void setResults(ArrayList<Productos> results) {
        this.results = results;
    }
}
