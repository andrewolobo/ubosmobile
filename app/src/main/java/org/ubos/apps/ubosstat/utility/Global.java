package org.ubos.apps.ubosstat.utility;

/**
 * Created by Andrew on 10-08-2017.
 */

public class Global {
    public static String ENDPOINT = "http://192.168.8.101/ubos_app";
    public static String json_updates_for_indicators = ENDPOINT+"/check_updates_test.php";
    public static String census_url = "http://uboscensusp.azurewebsites.net/api/data/census";
    public static final String SERVER_IP = "http://192.168.8.101/ubos_app";
    public static final String ENDPOINT_CATEGORIES = "http://192.168.8.101/ubos_app/index_get_categories.php";
    public static final String DOWNLOAD_URL = "http://statrm.azurewebsites.net/api/data/Export?id=1";

    //private static final String json_updates_for_indicators = "http://192.168.8.101/ubos_app/check_updates.php";
}
