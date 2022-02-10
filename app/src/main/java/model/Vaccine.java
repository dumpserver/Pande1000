package model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Vaccine {
    private final String user_id;
    private final String name;
    public Vaccine() {
        this.user_id = null;
        this.name = null;
    }
    public Vaccine(String user_id, String name) {
        this.user_id = user_id;
        this.name = name;
    }

    public String getUserId() {
        return user_id;
    }

    public String getName() {
        return name;
    }
}