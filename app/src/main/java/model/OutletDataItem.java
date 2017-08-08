package model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import database.OutletsTable;

import java.util.UUID;

/**
 * Created by Dell on 3/15/2017.
 */

public class OutletDataItem implements Parcelable {

    private String outletId;
    private String outletName;


    public OutletDataItem() {
    }

    public OutletDataItem(String outletId, String outletName) {

        if (outletId == null) {
            outletId = UUID.randomUUID().toString();
        }


        this.outletId = outletId;
        this.outletName = outletName;
    }

        // set getters and setters

        public String getoutletId() {
            return outletId;
        }

        public void setoutletId(String outletId) {
            this.outletId = outletId;
        }


        public String getoutletName() {
            return outletName;
        }

        public void setoutletName(String outletName) {
            this.outletName = outletName;
        }


    public ContentValues toValues() {
        ContentValues values = new ContentValues(2);

        values.put(OutletsTable.COLUMN_OUTLET_ID, outletId);
        values.put(OutletsTable.COLUMN_OUTLET_NAME, outletName);
        return values;
    }

    @Override
    public String toString() {
        return "OutletItem{" +
                "outletId='" + outletId + '\'' +
                ", outletName='" + outletName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.outletId);
        dest.writeString(this.outletName);

    }

    public OutletDataItem(Parcel in) {
        this.outletId = in.readString();
        this.outletName = in.readString();

    }

    public static final Parcelable.Creator<OutletDataItem> CREATOR = new Parcelable.Creator<OutletDataItem>() {
        @Override
        public OutletDataItem createFromParcel(Parcel source) {
            return new OutletDataItem(source);
        }

        @Override
        public OutletDataItem[] newArray(int size) {
            return new OutletDataItem[size];
        }
    };

    }
