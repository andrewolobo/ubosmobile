package org.ubos.apps.ubosstat.utility;

/**
 * Created by Andrew on 10-08-2017.
 */

public class Global {
    public static String ENDPOINT = "http://192.168.1.30:8080/ubos";
    public static String json_updates_for_indicators = ENDPOINT+"/check_updates.php";
    public static String census_url = "http://statrm.azurewebsites.net/api/data/census";
}
