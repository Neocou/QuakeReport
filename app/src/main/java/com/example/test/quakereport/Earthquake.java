package com.example.test.quakereport;

/**
 * 地震MODEL类
 */

public class Earthquake {
    private Double mMagnitude;
    private String mLocation;
    private long mDate;
    private String mUrl;

    public String getmUrl() {
        return mUrl;
    }

    public Double getmMagnitude() {
        return mMagnitude;
    }



    public String getmLocation() {
        return mLocation;
    }



    public long getmDate() {
        return mDate;
    }

    public Earthquake(Double mMagnitude, String mLocation, long mDate, String mUrl) {
        this.mMagnitude = mMagnitude;
        this.mLocation = mLocation;
        this.mDate = mDate;
        this.mUrl = mUrl;
    }
}
