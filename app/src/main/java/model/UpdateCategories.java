package model;

/**
 * Created by Dell on 8/20/2017.
 */

public class UpdateCategories {


    public String cat_id;
    public String cat_name;

    public UpdateCategories(String cat_id, String cat_name){
        cat_id = cat_id;
        cat_name = cat_name;

    }



    public String getCatId(){
        return cat_id;
    }
    public String getCatName(){
        return cat_name;
    }


}
