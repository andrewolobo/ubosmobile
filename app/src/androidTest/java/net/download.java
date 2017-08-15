package net;

import android.os.AsyncTask;

import com.loopj.android.http.HttpGet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class download extends AsyncTask<Object, Void, Long> {
	public TheObserver m;
	private static String total;

	@Override
	protected Long doInBackground(Object... arg0) {
		send((String) arg0[0]);
		return null;
	}

	public static String send(String url) {
		HttpGet get = new HttpGet(url);
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpResponse response = httpclient.execute(get);
			HttpEntity entity = response.getEntity();
			String data = EntityUtils.toString(entity);
			System.out.println("Response is:"+data);
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "Complete";
	}

	public static String send(String url, String[] variables) {
		BufferedReader s = null;
		URLConnection n;
		String ts;
        System.out.println("AOTPut"+url);
		total = "";
		try {
			url = url + "?";
			URL site = new URL(url);
			n = site.openConnection();
			n.setDoOutput(true);
			OutputStreamWriter o = new OutputStreamWriter(n.getOutputStream());
			if (variables.length != 0) {
				for (int x = 0; x < variables.length - 1; x++) {
					o.write(variables[x].split("=")[0] + "="
							+ variables[x].split("=")[1] + "&");
				}

				o.write(variables[variables.length - 1].split("=")[0] + "="
						+ variables[variables.length - 1].split("=")[1]);
			} else {
				o.write(variables[0].split("=")[0] + "="
						+ variables[0].split("=")[1]);
			}

			o.close();

			s = new BufferedReader(new InputStreamReader(n.getInputStream()));
			while ((ts = s.readLine()) != null) {
				total += ts;
			}

		} catch (Exception e) {
			ts = e.toString();
		}
		return total;

	}

	@Override
	protected void onPostExecute(Long arg) {
		m.Callback(total);
		cancel(true);

		return;
	}

	public void setObserver(TheObserver observer) {
		m = observer;
	}

}
