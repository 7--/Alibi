package in.alibi.locationservice;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.SharedPreferences;
import android.util.Log;
import in.alibi.loginandregistration.SessionManager;

class PostLocation{

	
	private static final String TAG ="PostLocation";
	protected void doInBackground(final String uniqueid,final String date, final String lat,final String log,
			final String speed, final String alt,final String acc, final String unique_id) {
	    
	    Thread thread = new Thread()
		{ 
	    	
		    

			@Override
		    public void run() {
		    	
		    	HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost("http://alibi.in/mobile_login/add_location.php"); 
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				 pairs.add(new BasicNameValuePair("date", date));
				 pairs.add(new BasicNameValuePair("uniqueid", uniqueid));
				 pairs.add(new BasicNameValuePair("lat", lat));
				 pairs.add(new BasicNameValuePair("log", log));
				 pairs.add(new BasicNameValuePair("speed", speed));
				 pairs.add(new BasicNameValuePair("alt", alt));
				 pairs.add(new BasicNameValuePair("acc", acc));
				 pairs.add(new BasicNameValuePair("time", getTime()));
				 pairs.add(new BasicNameValuePair("unique_id", unique_id));
				 
				
				try {
					post.setEntity(new UrlEncodedFormEntity(pairs));
					HttpResponse response = client.execute(post);
					// Execute the GET call and obtain the response
					HttpEntity responseEntity = response.getEntity();
					// Retrieve a String from the response entity
					String content = EntityUtils.toString(responseEntity);
					
					Log.v(TAG,"Post Response: "+content);
					
					
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					//Toast.makeText(this, "damn", Toast.LENGTH_LONG).show();
				} catch (ClientProtocolException e) {
					//Toast.makeText(this, "damn", Toast.LENGTH_LONG).show();
				} catch (IOException e) { 
					//Toast.makeText(this, "damn", Toast.LENGTH_LONG).show();
				}  
		    }
		};
		thread.start();
	}
	public String getTime(){
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("h:mm aa");
		String time1 = sdf.format(dt);
		return time1;
	}
}