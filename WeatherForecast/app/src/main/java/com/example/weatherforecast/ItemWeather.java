package com.example.weatherforecast;

public class ItemWeather {
    private String img;
    private String area;
    private String temp;

    public ItemWeather( String img, String area, String temp) {
        this.img = img;
        this.area = area;
        this.temp = temp;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

}
