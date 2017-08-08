package model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import database.CategoriesTable;

import java.util.UUID;

public class NavDrawerItem implements Parcelable {

    private String itemId;
    private String category;

    public NavDrawerItem() {
    }

    public NavDrawerItem(String itemId, String category) {

        if (itemId == null) {
            itemId = UUID.randomUUID().toString();
        }

        this.itemId = itemId;
        this.category = category;

    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public ContentValues toValues() {
        ContentValues values = new ContentValues(2);

        values.put(CategoriesTable.COLUMN_ID, itemId);
        values.put(CategoriesTable.COLUMN_CATEGORY, category);
        return values;
    }

    @Override
    public String toString() {
        return "NavDataItem{" +
                "itemId='" + itemId + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemId);
        dest.writeString(this.category);
    }

    protected NavDrawerItem(Parcel in) {
        this.itemId = in.readString();
        this.category = in.readString();

    }

    public static final Creator<NavDrawerItem> CREATOR = new Creator<NavDrawerItem>() {
        @Override
        public NavDrawerItem createFromParcel(Parcel source) {
            return new NavDrawerItem(source);
        }

        @Override
        public NavDrawerItem[] newArray(int size) {
            return new NavDrawerItem[size];
        }
    };


}
