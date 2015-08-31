package com.example.androidservice;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.RandomStringUtils;

import sql.DatabaseHandler;
import sql.Locate;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

//Alibi - GPS Recorder

public class MainActivity extends FragmentActivity {
	
	public static String uniqueId;	 
	public CreateNewProduct cnp;
	public static boolean running;
	
	//For UI
	private static final String TAG = "BroadcastTest";
	private Intent intent;
	
	int hour;
	int minute;
	int ampm;
	int month;
	int day;
	int year;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		running=true;
		setContentView(R.layout.activity_main);
		super.onCreate(savedInstanceState);
		setTimeDate();
		addListenerOnButton();
		SharedPreferences settings = getSharedPreferences("MyPrefs", Context.MODE_WORLD_READABLE);
		
		createUniqueId();
		updateUI();

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
		   
		   TextView tv1 = (TextView)findViewById(R.id.id);
		   Log.v("private id", uniqueId);
		   tv1.setText(uniqueId);
		   
		   final Button button = (Button) findViewById(R.id.button);
	         button.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	            	 Log.v("clickety", "click");
	      		   	SharedPreferences settings = getSharedPreferences("MyPrefs",  Context.MODE_WORLD_READABLE);
	      		   	Editor editor = settings.edit();
	      		   	editor.putBoolean("invisible", true);
	      		   	editor.commit();
	      	        //Toast.makeText(MainActivity.this,"Invisible mode only availible in paid version, availible on the website",Toast.LENGTH_LONG).show();
	             }
	         });
	}

    
	public String makeTimeReadable(Long time){
	    Date d = new Date(time);

	    SimpleDateFormat sf = new SimpleDateFormat("mm-dd-yyyy' 'HH:mm:ss");
	    
	    String sDate = sf.format(d);
	    return sDate;
	}

    private void updateUI() {
		DatabaseHandler db = new DatabaseHandler(this);
        List<Locate> LocateList = db.getAllLocates();

        Log.v("locatelist size",Integer.toString(LocateList.size()));
        

        //If more than 5 set them all
    	if(LocateList.size()>5){
    		 //5 locations to show by defualt.
            String time1=makeTimeReadable(LocateList.get(1).getTime());
            String time2=makeTimeReadable(LocateList.get(2).getTime());
            String time3=makeTimeReadable(LocateList.get(3).getTime());
            String time4=makeTimeReadable(LocateList.get(4).getTime());
            String time5=makeTimeReadable(LocateList.get(5).getTime());
            
	    	Log.v("update ui","updateeeeeeeeee");
	    	TextView t =(TextView)findViewById(R.id.text1); 
	    	t.setText(time1);
	    	TextView t2 =(TextView)findViewById(R.id.text2); 
	    	t2.setText(time2);
	    	TextView t3 =(TextView)findViewById(R.id.text3); 
	    	t3.setText(time3);
	    	TextView t4 =(TextView)findViewById(R.id.text4); 
	    	t4.setText(time4);
	    	TextView t5 =(TextView)findViewById(R.id.text5); 
	    	t5.setText(time5);
	    	final Context c=this;
	    	
	    	//Only the first button has location
	    	final float lat1 = (float) LocateList.get(1).getLat();
	    	final float log1 = (float) LocateList.get(1).getLog();
	    	
	    	 final Button button1 = (Button) findViewById(R.id.button1);
	         button1.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	            	 Log.v("onclick11111111", lat1+"       "+log1);
	            	 String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat1, log1);
	            	 Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
	            	 c.startActivity(intent);	
	             }
	         });
	         
	         final Button button2 = (Button) findViewById(R.id.button2);
	         button2.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	            	 
	             }
	         });
	         
	         final Button button3 = (Button) findViewById(R.id.button3);
	         button3.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	            	
	             }
	         });
	         
	         final Button button4 = (Button) findViewById(R.id.button4);
	         button4.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	            		             
	             }
	         });
	         
	         final Button button5= (Button) findViewById(R.id.button5);
	         button5.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	            	
	             }
	         });
	    	
	    	
	    	
    	}
    	//If theres less than  5
    	else if(LocateList.size()>=1){
    		String time1=makeTimeReadable(LocateList.get(1).getTime());
            
	    	Log.v("update ui","updateeeeeeeeee");
	    	TextView t =(TextView)findViewById(R.id.text1); 
	    	t.setText(time1);
    	}
    	db.close();
    	
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
    
    public void setTimeDate() {
    	 
		TextView time = (TextView) findViewById(R.id.time);
		TextView date = (TextView) findViewById(R.id.date);

		
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR);
		int minute = c.get(Calendar.MINUTE);
		int ampm = c.get(Calendar.AM_PM);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int year = c.get(Calendar.YEAR);

 
		// set current time into textview
		time.setText(
                    new StringBuilder().append(pad(timeConv(hour)))
                                       .append(":").append(pad(minute)));
 
		date.setText(new StringBuilder().append(pad(month))
				.append("/").append(pad(day)).append("/")
				.append(pad(year)));
	}
    private static String pad(int c) {
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}
    
    
    
    //Set date time buttons, dialogs
    public void addListenerOnButton() {
    	 
		Button settimebutton = (Button) findViewById(R.id.settimebutton);
		Button setdatebutton= (Button) findViewById(R.id.setdatebutton);

		setdatebutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				DatePickerDialogFragment picker =  new DatePickerDialogFragment();
	            picker.show(getSupportFragmentManager(), "datePicker");  
			}});
		settimebutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				showDialog(1);
			}		
			});
	}
 
	@Override
	//For show time Dialog
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 1:
			// set time picker as current time
			return new TimePickerDialog(this, 
                                       timePickerListener, hour, minute,false);
		}
		return null;
	}
	//Not used I dont think
	private DatePickerDialog.OnDateSetListener datePickerListener = 
            new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year1, int monthOfYear,
				int dayOfMonth) {
			year = year1;
			month = monthOfYear+1;
			day=dayOfMonth;
			TextView date = (TextView) findViewById(R.id.date);
			// set current time into textview
			date.setText(new StringBuilder().append(pad(month))
					.append("/").append(pad(day)).append("/")
					.append(pad(year)));
		
		}
	};
	
	private TimePickerDialog.OnTimeSetListener timePickerListener = 
            new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = timeConv(selectedHour);
			minute = selectedMinute;
			
			TextView time = (TextView) findViewById(R.id.time);
			//set current time into textview
			time.setText(new StringBuilder().append(pad(hour))
					.append(":").append(pad(minute)));
		}
	};
    
	public int timeConv(int hour){
		TextView ampm = (TextView) findViewById(R.id.ampm);
		if(hour==0){
			ampm.setText("AM");
			return 12;
		}
		else if(hour>12){ 
			ampm.setText("PM");
			return hour-12;
		}
		ampm.setText("AM");
		return hour;
	}
}
