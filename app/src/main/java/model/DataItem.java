package model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import database.ItemsTable;

import java.util.UUID;

public class DataItem {

    private long id;
    private String title;
    private String headline;
    private String summary;
    private String description;
    private String data;
    private String period;
    private String unit;
    private String url;
    private String updated_on;
    private String change_type;
    private String change_value;
    private String change_desc;
    private String index_value;
    private String cat_id;

    public DataItem() {
    }

    public DataItem(long id, String title, String headline, String summary, String description,  String data , String period , String unit,String url, String updated_on, String change_type, String change_value, String change_desc, String index_value , String cat_id) {



        this.id = id;
        this.title = title;
        this.headline = headline;
        this.summary = summary;
        this.unit = unit;
        this.description = description;
        this.data = data;
        this.period = period;
        this.url = url;
        this.updated_on = updated_on;

        this.change_type = change_type;
        this.change_value = change_value;
        this.change_desc = change_desc;
        this.index_value = index_value;
        this.cat_id = cat_id;
    }






}
