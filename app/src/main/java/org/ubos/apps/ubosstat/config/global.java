package org.ubos.apps.ubosstat.config;

import org.ubos.apps.ubosstat.json.JsonPacket;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Andrew on 13-06-2017.
 */

public class global {
    public static String base_url ="";
    public static ArrayList<JsonPacket> data;
    public static boolean switch_flag = false;
    public static int position = 0;
    public static HashMap<Integer, String> section_map = new HashMap<Integer, String>();
    public static void createMap(){
        section_map.put(0,"Titles");
        section_map.put(1,"Statistics");
        section_map.put(2,"Tables");
        section_map.put(3,"SpecialStats");
        section_map.put(4,"Graphs");
        section_map.put(5,"Headers");
        section_map.put(6,"GeneralText");
        section_map.put(7,"Order");
        section_map.put(8,"Section");
    }
}
