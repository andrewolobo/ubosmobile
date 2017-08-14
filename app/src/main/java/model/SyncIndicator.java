package model;

/**
 * Created by RJ-OTYEK on 2/18/2017.
 */

public class SyncIndicator {



    public long id;
    public long indicatorId;
    public String title;
    public String headline;
    public String summary;
    public String description;
    public String data;
    public String period;
    public String unit;
    public String url;
    public String updated_on;
    public String change_type;
    public String change_value;
    public String change_desc;
    public String index_value;
    public String cat_id;


    public SyncIndicator(Long indicatorId, String title) {

        indicatorId = indicatorId;
        title = title;
    }

    public long getIndicatorId(){
        return indicatorId;
    }

    public String getTitle(){
        return title;
    }

}