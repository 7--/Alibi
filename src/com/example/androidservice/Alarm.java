package com.example.androidservice;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import sql.DatabaseHandler;
import sql.Locate;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.example.androidservice.MyLocation.LocationResult;

public class Alarm extends BroadcastReceiver 
{    
   	 
     protected static final String[] Intent = null;
     public int index;
     int tries;
     int successes;
     int pos; //position of the record_every spinner
     
	 @Override
     public void onReceive(Context context, Intent intent) 
     {   
         PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
         PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
         wl.acquire();
         
         //SetAlarm(context);
         SharedPreferences settings = context.getSharedPreferences("MyPrefs", context.MODE_WORLD_WRITEABLE);
		 String uniqueid = settings.getString("uniqueId", "Error"); 
	        
		 
		 Log.i("6", "main activity started alarm");   
         getLoc(context,uniqueid); 

	     Log.i("pos",String.valueOf(pos));

         // Put here YOUR code.
         //getLoc(context);
         //Toast.makeText(context, "o", Toast.LENGTH_LONG).show(); // For example
         
         wl.release();
     }

 public void SetAlarm(Context context,int pos)
 {
	 Log.v("Alarm set,num of mins:" ,String.valueOf(posToMin(pos)));
	 //TODO function changes pos to appropriate number of minutes 
     AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
     Intent i = new Intent(context, Alarm.class);
     PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
     am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * posToMin(pos), pi); // Millisec * Second * Minute
 }

 public void CancelAlarm(Context context)
 {
     Intent intent = new Intent(context, Alarm.class);
     PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
     AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
     alarmManager.cancel(sender);
 }
 

 public void getLoc(final Context context,final String uniqueid){
	    LocationResult locationResult = new LocationResult(){
	    	@Override
	        public void gotLocation(Location location){
	    		Log.v("getLoc()","gotLocation called");
	    		tries++;
	    		if(location!=null){
	    			successes++;
	    			Log.v("Location not null Tries: "+tries+"  Successes: "+successes ,"great");
		    			
	    				//Creates a row of location data
	    				//CreateNewLocation
		    			CreateNewProduct cnp = new CreateNewProduct();
		    			cnp.doInBackground(uniqueid,
		    					trimStringTo10Digits(makeDateReadable(location.getTime())),
		    					Double.toString(location.getLatitude()), 
		    					Double.toString(location.getLongitude()),
		    					Float.toString(location.getSpeed()),
		    					Double.toString(location.getAltitude()),
		    					Float.toString(location.getAccuracy()));
		    			
		    			//should be date format in MM/DD/YYYY
		    			//Then youll be able to select all the locations for a date
		    			Log.v("Make the date readable", makeDateReadable(location.getTime()));
		    			
		    			
		    			 //this is for local SQL which doesnt work
		    			DatabaseHandler db = new DatabaseHandler(context);
		    	         
		    		        /**
		    		         * For local db
		    		         * */
		    		        // Inserting Contacts
		    		        Log.d("Insert: ", "Inserting .."); 
		    		        db.addLocate(new Locate(location.getTime(),location.getLatitude(),location.getLongitude(),
		    		        		location.getSpeed(),location.getAltitude(),location.getAccuracy()));
		    		        
		    		        // Reading all contacts 
		    		        /*
		    		        Log.d("Reading: ", "Reading all contacts.."); 
		    		        List<Locate> locates = db.getAllLocates();       
		    		         
		    		        for (Locate cn : locates) {
		    		            String log = "Id: "+cn.getId()+" ,Time: " + cn.getTime() + " ,Lat: " + cn.getLat();
		    		                // Writing Contacts to log
		    		        Log.d("Name: ", log);
		    		    	}
		    		    	*/
		    			db.close();
		    		//UILocations[0][index]=Double.toString(location.getLatitude());
		    		//UILocations[1][index]=Double.toString(location.getLongitude());
		    		//UILocations[2][index]=Double.toString(location.getLongitude());
		    		//UILocations[3][index]=Float.toString(location.getSpeed());
		    		//UILocations[4][index]=Double.toString(location.getAltitude());
		    		//UILocations[5][index]=Float.toString(location.getAccuracy());
		    		//index=(index+1)%100;		
		    		
		    		//Start service for UI display
		    		//Intent intent = new Intent(context,service.class);
		    		//intent.put("UILocations", UILocations);
		    	    //context.startService(intent);
		    	
	    		}
	        }
	    };
	    MyLocation myLocation = new MyLocation();
	    boolean gotL =myLocation.getLocation(context, locationResult);
	    if(gotL==false)
	    	Log.v("Unable to get Loc", "dayum");
 }
 
 public String makeDateReadable(Long time){
	 SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss z");
	 System.out.println(sdf.format(time));

	 sdf.setTimeZone(TimeZone.getDefault());
	 
	 return sdf.format(time);
 }
 
 public String trimStringTo10Digits(String s){
	 StringBuilder sb = new StringBuilder(s);
	 sb.delete(10, 9999);
	 return sb.toString();
 }
 
 //Change the pos the spinner to the appropriate number of mins
 public int posToMin(int pos){
	switch(pos){
		case 0: return 2;
		case 1: return 5;
		case 2: return 10;
		case 3: return 15;
		case 4: return 30;
		case 5: return 60;
		case 6: return 120;
		case 7: return 180;
		default: return 10;
	}
 }
/*
 public void getLoc(Context context){
	 LocationResult locationResult = new LocationResult(){
	        @Override
	        public void gotLocation(Location location){
	        	//Toast.makeText(AndroidService.getInstance(), "Latitude: "+location.getLatitude()+"\n Longitude: "+location.getLongitude() , Toast.LENGTH_SHORT).show();
	        	Log.v("gotLocation", "!!!!!");
	        	if(location!=null){
		        	Log.v("gotLocation", "!!!!!"+ location.getLatitude());
	        	}
	        }
	    };
	    MyLocation myLocation = new MyLocation(); 
	    myLocation.getLocation(context, locationResult);
 }
 */
}