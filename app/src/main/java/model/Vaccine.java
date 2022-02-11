package model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Vaccine {
    private final String user_id;
    private final String name;
    private final String date;
    private final Double latitude;
    private final Double longitude;

    public Vaccine() {
        this.user_id = null;
        this.name = null;
        this.date = null;
        this.latitude = null;
        this.longitude = null;
    }

    public Vaccine(String user_id, String name, String date, Double latitude, Double longitude) {
        this.user_id = user_id;
        this.name = name;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getUserId() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public Double getLatitude(){
        return latitude;
    }

    public Double getLongitude(){
        return longitude;
    }

    public String getDate() {return date;}
}