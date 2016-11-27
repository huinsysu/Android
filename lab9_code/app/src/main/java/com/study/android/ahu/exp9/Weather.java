package com.study.android.ahu.exp9;

/**
 * Created by ahu on 16-11-27.
 */
public class Weather {
    private String date;
    private String weather_description;
    private String temperature;

    public void setDate(String date) {
        this.date = date;
    }

    public void setWeather_description(String weather_description) {
        this.weather_description = weather_description;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDate() {
        return date;
    }

    public String getWeather_description() {
        return weather_description;
    }

    public String getTemperature() {
        return temperature;
    }
}
