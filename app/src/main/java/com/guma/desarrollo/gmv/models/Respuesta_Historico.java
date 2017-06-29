package com.guma.desarrollo.gmv.models;

import com.guma.desarrollo.core.Historico;
import com.guma.desarrollo.core.Productos;

import java.util.ArrayList;

/**
 * Created by alder.hernandez on 03/03/2017.
 */

public class Respuesta_Historico {
    private static int count;
    private ArrayList<Historico> results;

    public ArrayList<Historico> getResults() {
        return results;
    }
    public  int getCount() {
        return count = results.size();
    }
    public void setResults(ArrayList<Historico> results) {
        this.results = results;
    }
}
