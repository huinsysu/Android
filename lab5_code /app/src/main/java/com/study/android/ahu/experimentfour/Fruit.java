package com.study.android.ahu.experimentfour;

/**
 * Created by ahu on 16-10-21.
 */
public class Fruit {
    private String fruitName;
    private int imageId;

    public Fruit(String name, int id) {
        this.fruitName = name;
        this.imageId = id;
    }

    public String GetFruitName() {
        return fruitName;
    }

    public int GetImageId() {
        return imageId;
    }
}

