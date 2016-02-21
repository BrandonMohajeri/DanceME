package web_classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.util.Log;

/*
 * To get the source data from the website to be used for scraping
 */
class QuoteScraper{
	
	private final static String TAG = "QuoteScraper";
	
	public static String scrapeQuote(String url){
		StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
                      
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                      builder.append(line);
                }                  
            } 
            else {
               Log.e("Getter", "Failed to download file");
            }
         } catch (ClientProtocolException e) {
                e.printStackTrace();
                Log.i(TAG, "Unable to Connect1");
         } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, "Unable to Connect");
         }
        return builder.toString();
	}
}