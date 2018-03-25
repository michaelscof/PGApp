package com.example.prem.pgapp;

/**
 * Created by Prem on 03-Mar-18.
 */

public class PGs {
    private String name,area,type;
    public PGs(String name, String area, String type) {
        this.name = name;
        this.area = area;
        this.type = type;
    }

    public PGs(String name) {
        this.name = name;
    }

    public PGs() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
}
