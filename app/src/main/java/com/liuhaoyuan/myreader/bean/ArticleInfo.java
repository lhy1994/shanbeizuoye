package com.liuhaoyuan.myreader.bean;

/**
 * Created by liuhaoyuan on 2016/5/27.
 */
public class ArticleInfo {
    private String unit;
    private String lesson;
    private String title;

    @Override
    public String toString() {
        return "ArticleInfo{" +
                "unit='" + unit + '\'' +
                ", lesson='" + lesson + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public ArticleInfo(String unit, String lesson, String title) {
        this.unit = unit;
        this.lesson = lesson;
        this.title = title;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUnit() {
        return unit;
    }

    public String getLesson() {
        return lesson;
    }

    public String getTitle() {
        return title;
    }
}
