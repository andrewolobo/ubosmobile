package org.ubos.apps.ubosstat.config;

import org.ubos.apps.ubosstat.json.JsonPacket;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.util.ByteArrayBuffer;


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
    public static String read(InputStream ist) throws IOException {
        InputStream is = ist;
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayBuffer baf = new ByteArrayBuffer(250);
        int current = 0;
        while ((current = bis.read()) != -1) {
            baf.append((byte) current);
        }
        String st = new String(baf.toByteArray());
        return st;
    }
}
