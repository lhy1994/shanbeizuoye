package com.liuhaoyuan.myreader.bean;

/**
 * Created by liuhaoyuan on 2016/5/28.
 */
public class Word {
    private String word;
    private int level;

    public Word(String word, int level) {
        this.word = word;
        this.level = level;
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", level=" + level +
                '}';
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getWord() {
        return word;
    }

    public int getLevel() {
        return level;
    }
}
