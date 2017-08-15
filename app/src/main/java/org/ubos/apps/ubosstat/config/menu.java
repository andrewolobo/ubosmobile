package org.ubos.apps.ubosstat.config;

import org.ubos.apps.ubosstat.StatisticsActivity;
import org.ubos.apps.ubosstat.json.JsonPacket;

import java.util.HashMap;


/**
 * Created by Andrew on 21-02-2017.
 */

public class menu {
    public HashMap<String, Class> sidebar;
    public HashMap<String, Integer> sidebar_v;

    public menu() {
        sidebar = new HashMap<String, Class>();
        sidebar.put("Highlights", StatisticsActivity.class);
        sidebar.put("Population and Housing", StatisticsActivity.class);
        sidebar.put("Household Distribution", StatisticsActivity.class);
        sidebar.put("Population Density", StatisticsActivity.class);
        sidebar.put("Life Expectancy", StatisticsActivity.class);
        sidebar.put("Education", StatisticsActivity.class);
        sidebar.put("Marital Status", StatisticsActivity.class);
        sidebar.put("Orphanhood", StatisticsActivity.class);
    }
    public void buildMenu(){
        sidebar_v = new HashMap<String, Integer>();
        Integer i = 0;
        for(JsonPacket packet:global.data){
            System.out.println(packet.Section.name+":"+i);
            sidebar_v.put(packet.Section.name,i);
            i++;
        }
    }
}
