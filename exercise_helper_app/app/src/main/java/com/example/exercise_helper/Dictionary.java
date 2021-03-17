package com.example.exercise_helper;

public class Dictionary {
    private String id;
    private String title;
    private String category;
    private String delay;
    private String content;
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Dictionary(String id, String title, String category, String delay, String content, String time) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.delay = delay;
        this.content = content;
        this.time = time;
    }

    public Dictionary(String title, String time) {
        this.title = title;
        this.time = time;
    }
}
