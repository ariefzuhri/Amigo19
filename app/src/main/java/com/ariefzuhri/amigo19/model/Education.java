package com.ariefzuhri.amigo19.model;

public class Education {
    private String topic;
    private String desc;

    public Education(String topic, String desc) {
        this.topic = topic;
        this.desc = desc;
    }

    public String getTopic() {
        return topic;
    }

    public String getDesc() {
        return desc;
    }
}
