package com.example.androidservice;

import java.util.Date;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

//The service that starts the alarm manager, to get location at specified spinner value
public class service extends Service
{
	private String locationString;
    private static final String TAG = "MyService";
    public static String uniqueId;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate(){
        Log.i("3.2", "main activity started service");

    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        Log.i("3.1", "main activity started service");

    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i("3.5", "main activity started service");
    	SharedPreferences settings = getSharedPreferences("MyPrefs", Context.MODE_WORLD_READABLE);
    	uniqueId=settings.getString("uniqueId","Error2");
        Log.i("4", "main activity started service");
        
        int pos = intent.getIntExtra("record_every", 2);
        Log.i("pos service", "main activity started service");

        Alarm alarm = new Alarm();
        alarm.SetAlarm(this,pos);
        //Intent alarmIntent=new Intent(this, Alarm.class);
        //alarmIntent.putExtra("record_every",pos);
        //sendBroadcast(alarmIntent);
        
        
        return START_STICKY;
    }
    
    public Context getContext(){
    	return this;
    }
}