package com.example.syrok.myfiinder;

/**
 * Created by Syrok on 12/04/2016.
 */

public class GooglePlace {
    private String name;
    private String category;
    private String rating;
    private String open;
    private double latitude;
    private double longitude;

    public GooglePlace() {
        this.name = "";
        this.rating = "";
        this.open = "";
        this.setCategory("");
        this.latitude=0;
        this.longitude=0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    public void setOpenNow(String open) {
        this.open = open;
    }

    public String getOpenNow() {
        return open;
    }

    public void setLatitude(double lat){ this.latitude = lat;}

    public double getLatitude(){return latitude;}

    public void setLongitude(double lng){ this.longitude = lng;}

    public double getLongitude(){return longitude;}
}

