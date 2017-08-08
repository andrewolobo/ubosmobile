package model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import database.OutletsTable;

import java.util.UUID;

/**
 * Created by Dell on 3/8/2017.
 */

public class Outlet implements Parcelable {

    private String outletId;
    private String outletName;

    public Outlet() {
    }

    public Outlet(String outletId, String outletName) {

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

    public Outlet(String OutletName)
    {

        this.setOutletName(OutletName);
    }

    public String getOutletName(){
        return outletName;
    }

    public  void setOutletName(String outletname) {

        outletName = outletname;
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

        protected Outlet(Parcel in) {
            this.outletId = in.readString();
            this.outletName = in.readString();
        }

        public static final Parcelable.Creator<Outlet> CREATOR = new Parcelable.Creator<Outlet>() {
            @Override
            public Outlet createFromParcel(Parcel source) {
                return new Outlet(source);
            }

            @Override
            public Outlet[] newArray(int size) {
                return new Outlet[size];
            }
        };
}
