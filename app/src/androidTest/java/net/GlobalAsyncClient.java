package net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Andrew on 17-05-2016.
 */
public class GlobalAsyncClient {

        private static final String BASE_URL = Shared.base_url;

        private static AsyncHttpClient client = new AsyncHttpClient();


        public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            client.get(url, params, responseHandler);
        }

        public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            client.post(url, params, responseHandler);
        }


}
