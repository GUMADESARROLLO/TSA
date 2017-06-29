package com.guma.desarrollo.core;

/**
 * Created by alder.hernandez on 03/03/2017.
 */

public class Usuario {
    private boolean mExiste;
    private String mNombre;
    private String mId;

    public Usuario(boolean mExiste, String mNombre, String mId) {
        this.mExiste = mExiste;
        this.mNombre = mNombre;
        this.mId = mId;
    }
    public Usuario(){}

    public boolean ismExiste() {
        return mExiste;
    }

    public void setmExiste(boolean mExiste) {
        this.mExiste = mExiste;
    }

    public String getmNombre() {
        return mNombre;
    }

    public void setmNombre(String mNombre) {
        this.mNombre = mNombre;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }
}
