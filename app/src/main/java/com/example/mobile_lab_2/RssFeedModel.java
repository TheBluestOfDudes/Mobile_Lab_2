package com.example.mobile_lab_2;

import java.io.Serializable;

public class RssFeedModel implements Serializable {
    public String title;
    public String link;
    public String description;

    public RssFeedModel(String title, String link, String desc) {
        this.title = title;
        this.link = link;
        this.description = desc;
    }
}
