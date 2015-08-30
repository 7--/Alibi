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

public class service extends Service
{
	private String locationString;
    private static final String TAG = "MyService";
    public static String uniqueId;
    Alarm alarm = new Alarm();
    String[][] UIInfo;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
    	SharedPreferences settings = getSharedPreferences("MyPrefs", Context.MODE_WORLD_READABLE);
    	uniqueId=settings.getString("uniqueId","Error2");
    	
        alarm.SetAlarm(this);
        
        //Toast.makeText(this, "Service Start ", Toast.LENGTH_SHORT).show();
        //Log.d(TAG, "onStart");
        return START_STICKY;
    }
    
    public Context getContext(){
    	return this;
    }
}