package com.chiko;

public class Address {
    /**
     * 完整地址
     */
    private String address;

    /**
     * 路口
     */
    private String roads;

    /**
     * 经度
     */
    private double lon;

    /**
     * 纬度
     */
    private double lat;

    /**
     * 数据存储时间 格式 yyyy-MM-dd
     */
    private String date;
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getRoads() {
        return roads;
    }

    public void setRoads(String roads) {
        this.roads = roads;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "{" +
                "\"lon\":" + lon +
                ", \"lat\":" + lat +
                '}';
    }

}
