package com.study.android.ahu.experimentfour;

import java.io.Serializable;

/**
 * Created by ahu on 16-10-21.
 */
public class serializedItem implements Serializable {
    private String name;
    private int id;

    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setId(int id) {
        this.id = id;
    }
}
