package in.alibi.locationservice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.RandomStringUtils;

import in.alibi.locationservice.R;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import in.alibi.loginandregistration.LoginActivity;
import in.alibi.loginandregistration.SQLiteHandler;
import in.alibi.loginandregistration.SessionManager;

public class MainActivity extends FragmentActivity {
	//For UI
	private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
    private Button btnViewMap;
    private String viewMapLink = "http://alibi.in";
    //SQL
    private SQLiteHandler db;
    private SessionManager session;
	public static String uniqueId;	 
	public PostLocation cnp;
	//Service
    private Intent intent;
	private static final String  TAG = "MainActivity";

	@Override 
	public void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.activity_main);
		super.onCreate(savedInstanceState);		
		
		if(!isMyServiceRunning()){
			intent = new Intent(this,service.class);
	        this.startService(intent);
	        Log.i("start", "main activity started service");
		}
		
		txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnViewMap = (Button) findViewById(R.id.btnViewMap);
        
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
 
        // session manager
        session = new SessionManager(getApplicationContext());
  
        if (!session.isLoggedIn()) {
            logoutUser();
        }
 
        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
 
        String name = user.get("name");
        String email = user.get("email");
        Log.d(TAG ,"User: "+ name);
        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);
        
        // View map button click event
        btnViewMap.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View v) {
            	Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse(viewMapLink) );
                startActivity( browse );
            }
        });
        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
	}
	
	/**
	 * http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);
 
        db.deleteUsers();
 
        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
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
    	
    }
    
   
    private static String pad(int c) {
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}
    
    
   
}
