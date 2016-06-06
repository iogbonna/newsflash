package icom.com.news.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import icom.com.news.Helper.DateHelper;
import icom.com.news.Model.Rss;

/**
 * Created by Charlie on 12/08/2015.
 */
public class Repository extends DbAdapter{
    public Repository(Context context) {
        super(context);

    }

    // private static final String DATABASE_NAME = "StoreDB";
    private static final String TABLE_NAME = "Favourites";



    private static final String KEY_ID = "Id";
    private static final String KEY_LINK = "Link";
    private static final String KEY_TITLE = "Title";
    private static final String KEY_DESCRIPTION = "Description";
    private static final String KEY_PUBDATE = "PubDate";
    private static final String KEY_CATEGORY = "Category";

    public long AddToFavourites(Rss rss) {
        SQLiteDatabase db = openDb();

        ContentValues values = new ContentValues();
        values.put(KEY_LINK, rss.getLink());
        values.put(KEY_TITLE, rss.getTitle());
        values.put(KEY_DESCRIPTION, rss.getDescription());
        values.put(KEY_PUBDATE,String.valueOf(rss.getPubDate()));
        values.put(KEY_CATEGORY,rss.getCategory());
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }









    public ArrayList<Rss> GetFavourites() {
        SQLiteDatabase db = openDb();
        ArrayList<Rss> feeds = new ArrayList<Rss>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                Rss rss = new Rss();
                rss.setId(Integer.parseInt(cursor.getString(0)));
                rss.setLink(cursor.getString(1));
                rss.setTitle(cursor.getString(2));
                rss.setDescription((cursor.getString(3)));
                //rss.setPubDate(DateHelper.convertToDate(cursor.getString(4)));
                rss.setCategory(cursor.getString(5));
                // product.setRegDate(Date.)
                feeds.add(rss);
            } while (cursor.moveToNext());
        }

        return feeds;
    }


    public void saveLastFeed(String rss) {
        SQLiteDatabase db = openDb();
        ContentValues values = new ContentValues();
        db.execSQL("delete from Latest");
        values.put("Value", rss);
        String result=GetLastest();
        db.insert("Latest", null, values);
        db.close();
        //return id;
    }

    public String GetLastest() {
        SQLiteDatabase db = openDb();
        String feeds = null;
        String query = "SELECT  * FROM Latest";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                feeds=cursor.getString(1);

            } while (cursor.moveToNext());
        }

        return feeds;
    }



}
