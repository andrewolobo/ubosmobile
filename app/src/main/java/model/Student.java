package model;

/**
 * Created by Dell on 8/2/2017.
 */

public class Student{
   public long rollno;
    public String name;
   public   int age;

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


   public  Student(long rollno,String name,int age){
        this.rollno=rollno;
        this.name=name;
        this.age=age;
    }
}
