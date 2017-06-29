package com.guma.desarrollo.core;

/**
 * Created by maryan.espinoza on 28/06/2017.
 */

public class Historico {
    private boolean mOk;
    private Integer mCount;
    private String mVia,mStatus,mFecha;


    public Historico(boolean mOk, Integer mCount, String mVia, String mStatus, String mFecha) {
        this.mOk = mOk;
        this.mCount = mCount;
        this.mVia = mVia;
        this.mStatus = mStatus;
        this.mFecha = mFecha;
    }
    public Historico(){}

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

    public String getmVia() {
        return mVia;
    }

    public void setmVia(String mVia) {
        this.mVia = mVia;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getmFecha() {
        return mFecha;
    }

    public void setmFecha(String mFecha) {
        this.mFecha = mFecha;
    }
}
