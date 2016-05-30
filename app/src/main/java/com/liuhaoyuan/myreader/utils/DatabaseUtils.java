package com.liuhaoyuan.myreader.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.liuhaoyuan.myreader.bean.ArticleInfo;
import com.liuhaoyuan.myreader.bean.Word;

import java.util.ArrayList;

/**
 * Created by liuhaoyuan on 2016/5/27.
 */
public class DatabaseUtils {
    private static final String PATH = "/data/data/com.liuhaoyuan.myreader/files/reader.db";

    public static ArrayList<ArticleInfo> getArticleInfoList() {
        ArrayList<ArticleInfo> list = new ArrayList<>();
        SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = database.query("article", new String[]{"unit", "lesson", "title"}, null, null, null, null, null);
        while (cursor.moveToNext()){
            String unit=cursor.getString(cursor.getColumnIndex("unit"));
            String lesson=cursor.getString(cursor.getColumnIndex("lesson"));
            String title=cursor.getString(cursor.getColumnIndex("title")).replaceAll("\r","").replaceAll("\n","");
            ArticleInfo articleInfo=new ArticleInfo(unit,lesson,title);
            list.add(articleInfo);
        }
        database.close();
        return list;
    }
    public static String getContent(String lesson){
        String content="";
        SQLiteDatabase database=SQLiteDatabase.openDatabase(PATH,null,SQLiteDatabase.OPEN_READONLY);
        Cursor cursor=database.query("article",new String[]{"content"},"lesson=?",new String[]{lesson},null,null,null);
        while (cursor.moveToNext()){
            content=cursor.getString(0);
        }
        database.close();
        return content;
    }
    public static ArrayList<Word> getWordList(){
        ArrayList<Word> words=new ArrayList<>();
        SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = database.query("words", null, null, null, null, null, null);
        while(cursor.moveToNext()){
            String word=cursor.getString(cursor.getColumnIndex("word"));
            int level=cursor.getInt(cursor.getColumnIndex("level"));
            words.add(new Word(word,level));
        }
        database.close();
        return words;
    }
    public static void addWord(Context context,String word){
        MyOpenHelper helper=new MyOpenHelper(context,"myword",null,1);
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("word",word);
        database.insert("wordnote", "", values);
        database.close();
    }
    public static ArrayList<String> queryWords(Context context){
        ArrayList<String> wordList=new ArrayList<>();
        MyOpenHelper helper=new MyOpenHelper(context,"myword",null,1);
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor cursor = database.query(true, "wordnote", null, null, null, null, null, null, null);
        while (cursor.moveToNext()){
            String word=cursor.getString(0);
            wordList.add(word);
        }
        cursor.close();
        database.close();
        return wordList;
    }
    public static void deleteWord(Context context,String word){
        MyOpenHelper helper=new MyOpenHelper(context,"myword",null,1);
        SQLiteDatabase database = helper.getWritableDatabase();
        database.delete("wordnote","word=?",new String[]{word});
        database.close();
    }
}
