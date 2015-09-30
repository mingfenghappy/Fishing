package com.jude.fishing.model.bean;

/**
 * Created by Mr.Jude on 2015/9/11.
 */
public class PlaceBrief {
    private int id;
    private String name;
    private String preview;
    private String address;
    private float score;
    private int distance;
    private int cost;
    private int costType;
    private String fishType;
    private int poolType;
    private int[] serviceType;
    private double lat;
    private double lng;

    public PlaceBrief(int id, String name, String preview, String address, float score, int distance, int cost, int costType, String fishType, int poolType, int[] serviceType) {
        this.id = id;
        this.name = name;
        this.preview = preview;
        this.address = address;
        this.score = score;
        this.cost = cost;
        this.distance = distance;
        this.costType = costType;
        this.fishType = fishType;
        this.poolType = poolType;
        this.serviceType = serviceType;
    }

    public PlaceBrief(int id, String name, String preview, String address, float score, int distance, int cost, int costType, String fishType, int poolType, int[] serviceType, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.preview = preview;
        this.address = address;
        this.score = score;
        this.distance = distance;
        this.cost = cost;
        this.costType = costType;
        this.fishType = fishType;
        this.poolType = poolType;
        this.serviceType = serviceType;
        this.lat = lat;
        this.lng = lng;
    }

    public int getCost() {
        return cost;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPreview() {
        return preview;
    }

    public String getAddress() {
        return address;
    }

    public float getScore() {
        return score;
    }

    public int getDistance() {
        return distance;
    }

    public int getCostType() {
        return costType;
    }

    public String getFishType() {
        return fishType;
    }

    public int getPoolType() {
        return poolType;
    }

    public int[] getServiceType() {
        return serviceType;
    }


    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

}
