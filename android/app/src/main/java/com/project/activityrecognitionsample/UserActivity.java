package com.project.activityrecognitionsample;

/**
 * Created by Talha Hasan Zia on 05-Apr-18.
 * <p></p><b>Description:</b><p></p> Why class was created?
 * <p></p>
 * <b>Public Methods:</b><p></p> Only listing to public methods usage.
 */
public class UserActivity {
    private String name;
    private float confidence;

    public UserActivity(String name, float confidence) {
        this.name = name;
        this.confidence = confidence;
    }

    public float getConfidence() {
        return confidence;
    }

    public String getName() {
        return name;
    }
}
