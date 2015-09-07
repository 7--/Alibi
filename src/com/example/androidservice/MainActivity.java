package com.example.androidservice;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.RandomStringUtils;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//Alibi - GPS Recorder

public class MainActivity extends FragmentActivity {
	
	public static String uniqueId;	 
	public PostLocation cnp;
	public static boolean running;
	
	//For UI
	private Button btnRegister; 
	private static final String TAG = "BroadcastTest";
	private Intent intent;
	

	
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		
		running=true;
		setContentView(R.layout.activity_main);
		super.onCreate(savedInstanceState);		
		SharedPreferences settings = getSharedPreferences("MyPrefs", Context.MODE_WORLD_READABLE);		
		createUniqueId();
		
		btnRegister=(Button)findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //Open register activity
                        Intent registerIntent = new Intent(getApplicationContext(),Register.class);
                        startActivity(registerIntent);
                    }
        	});
		
		if(!isMyServiceRunning()){
			intent = new Intent(this,service.class);
	        this.startService(intent);
	        Log.i("start", "main activity started service");
		}		
	}
	

	private boolean isMyServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (service.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	//Toast location
	public void printLocation(double longi, double lat){
		String longAndLat = Double.toString(longi).concat("   ").concat(Double.toString(lat));
		Toast.makeText(this, longAndLat, Toast.LENGTH_LONG).show();
	}
		  
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	public static class AndroidService extends Application {
        private static AndroidService sInstance;
        public AndroidService() {
            super();
            sInstance = this;
        } 
        public static AndroidService getInstance() {
        return sInstance;
       }
	}
	
	private void createUniqueId(){
		   SharedPreferences settings = getSharedPreferences("MyPrefs", Context.MODE_WORLD_READABLE);
		   uniqueId=settings.getString("uniqueId","notSet");

		   if (uniqueId.equals("notSet")) {

			   Toast.makeText(this, "Creating unique ID", Toast.LENGTH_LONG).show();
			   //Create unique ID at first run
			   
			   uniqueId = RandomStringUtils.randomAlphanumeric(10).toUpperCase();//iwbn8
			   Log.v("unique",uniqueId);
		       SharedPreferences.Editor editor = settings.edit();
		       editor.putBoolean("FirstTime", false);
		       editor.putString("uniqueId", uniqueId);
		       editor.commit();
		   }
		   
	}

    
	public String makeTimeReadable(Long time){
	    Date d = new Date(time);

	    SimpleDateFormat sf = new SimpleDateFormat("mm-dd-yyyy' 'HH:mm:ss");
	    
	    String sDate = sf.format(d);
	    return sDate;
	}

    
    @Override
    public void onPause(){
    	super.onPause();
    }
    @Override
    public void onStop(){
    	super.onStop();
    	running=false;
    }
    
   
    private static String pad(int c) {
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}
    
    
   
}
