package com.guma.desarrollo.core;

/**
 * Created by maryan.espinoza on 28/06/2017.
 */

public class Productos {
    private boolean mOk;
    private Integer mCount;
    private String mId,mProducto,mFactura,mDOc;

    public Productos(boolean mOk, Integer mCount, String mId, String mProducto, String mFactura, String mDOc) {
        this.mOk = mOk;
        this.mCount = mCount;
        this.mId = mId;
        this.mProducto = mProducto;
        this.mFactura = mFactura;
        this.mDOc = mDOc;
    }
    public Productos(){}

    public boolean ismOk() {
        return mOk;
    }

    public void setmOk(boolean mOk) {
        this.mOk = mOk;
    }

    public Integer getmCount() {
        return mCount;
    }

    public void setmCount(Integer mCount) {
        this.mCount = mCount;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmProducto() {
        return mProducto;
    }

    public void setmProducto(String mProducto) {
        this.mProducto = mProducto;
    }

    public String getmFactura() {
        return mFactura;
    }

    public void setmFactura(String mFactura) {
        this.mFactura = mFactura;
    }

    public String getmDOc() {
        return mDOc;
    }

    public void setmDOc(String mDOc) {
        this.mDOc = mDOc;
    }
}
