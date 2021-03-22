package com.example.exercise_helper;

public class Dictionary {
    private String id;
    private String average;
    private String delay;
    
    private String title;
    private String category;
    private String content;
    private String time;

    // 생성자
    public Dictionary(String id, String title, String category, String delay, String content, String time, String average) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.delay = delay;
        this.content = content;
        this.time = time;
        this.average = average;
    }

    public Dictionary(String category, String delay, String average) {
        this.id = "";
        this.title = "";
        this.category = category;
        this.delay = delay;
        this.content = "";
        this.time = "";
        this.average = average;
    }

    // getter
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDelay() {
        return delay;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public String getAverage() {
        return average;
    }

    // setter
    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setAverage(String average) {
        this.average = average;
    }
}
