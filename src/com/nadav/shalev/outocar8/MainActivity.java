package com.nadav.shalev.outocar8;

import android.support.v7.app.ActionBarActivity;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity {
	
	static Button Bstart,Bend,Bsettings;
	static TextView tTimeLeft, tDriveTime, tStatusG0, tStatusReady, tStatusStop;
	static int state = 0,counter = 0,addTime,StartTime,sendTime = 0,Start_drive = 0;
	static String Time = "0:00",S_SecureNum,test_SendTo,test_SendFrom;
	static Boolean BTconnect = false,BTlast = false,S_silent2,S_BTon,S_BTconnect,S_SndAgain,Test;
	static SendSms sms = new SendSms();
	BluetoothClass BT = new BluetoothClass();
	static Timer updateTimer;
	static updateTask UpdateTask;
	AudioManager audiomanage;
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Test = false;
		if (Test){
			test_SendTo = "+972547808553";
			test_SendFrom = "+972547808553";
		}else{
			test_SendTo = "3455";
			test_SendFrom = "FNX";
		}
		
		System.out.println("Created!!!!!!!!!!!");
		MainActivity act = new MainActivity();
		act.CancelTimer();
		preStart();	
		Bstart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				state = 1;
				Ui();
				sms.send(test_SendTo, "1 "+S_SecureNum+" 1");
				BT.BTToggle(true,S_BTon);
				if (S_silent2){	
					audiomanage.setRingerMode(AudioManager.RINGER_MODE_SILENT);	
				}
				StartTime = (int) (System.currentTimeMillis()/1000);
				CreateNotification();
				updateTimer.schedule(UpdateTask, 0, 1000);
				S_SndAgain = true;
			}
		});		
		Bend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				endDrive();
			}

		});	
		Bsettings.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this,Settings.class));
			}
		});
	}
	private void endDrive() {
		// TODO Auto-generated method stub
		state = 2;
		Ui();
		sms.send(test_SendTo, "1 "+S_SecureNum+" 2");
		BT.BTToggle(false,S_BTon);
		if (S_silent2){
			audiomanage.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		}
		NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(0);
		S_SndAgain = true;
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		System.out.println("Strart!!!!!!!!!");
		//preStart();
		try {
			if(state==1||state==3){
				SharedPreferences settings = getSharedPreferences("Data", Context.MODE_PRIVATE);
				StartTime = settings.getInt("StartTime", StartTime);
				int EndTime = (int) (System.currentTimeMillis()/1000);
				addTime = Math.abs(EndTime-StartTime);
				counter = addTime;
				updateTimer.schedule(UpdateTask, 0, 1000);
				System.out.println("Timer On: " + state);
			}

		} catch (Exception e) {
		    e.printStackTrace();
		    System.out.println("Timer Crashed: " + state);
		}
		
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		SharedPreferences settings = getSharedPreferences("Data", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("state", state);
		editor.putString("Time", Time);
		if(state==1||state==3){
			MainActivity act = new MainActivity();
			//act.CancelTimer();
			editor.putInt("StartTime", StartTime);
		}
		editor.commit();
		System.out.println("stop!!!!!!!!!");
	}
	
	/**
	 * When confirm Sms Received
	 */
	public void Recieved() {
		// TODO Auto-generated method stub
		MainActivity act = new MainActivity();
		
		String Message = SendSms.reseiveMessage;
		ReadText.ReadText(Message);
		String set = ReadText.set;
		
		//System.out.println("Recieved:" + state);
		//System.out.println("Message = " + Message + " ; Set = " + set);
		
		switch(set){
		case "on":
			if (state==1){
				state = 3;
				Start_drive = counter;
				Ui();
				S_SndAgain = false;
			}
			break;
		case "off":
			if(state==2){	
				Time = ReadText.Time;
				state = 0;
				Ui();
				tTimeLeft.setText(" זמן ביטוח נותר"+Time);
				act.CancelTimer();
				
				//Upset the vars
				counter = 0; StartTime = 0; sendTime = 0; Start_drive = 0;
				BTconnect = false; BTlast = false; 
				updateTimer = new Timer();
				UpdateTask = new updateTask(new Handler(), this);
				
				//System.out.println("secure stoped secssesfully");
				S_SndAgain = false;
			}
			break;
		case "nothing":
			break;
		}
		SharedPreferences settings = getSharedPreferences("Data", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("state", state);
	}	
	public void preStart(){
		Bstart = (Button) findViewById(R.id.bStart);
		Bend = (Button) findViewById(R.id.bEnd);
		Bsettings = (Button) findViewById(R.id.bSettings);
		tTimeLeft = (TextView) findViewById(R.id.tTimeLeft);
		tDriveTime = (TextView) findViewById(R.id.tDriveTime);
		tStatusG0 = (TextView) findViewById(R.id.tGreenGo);
		tStatusReady = (TextView) findViewById(R.id.tTellowReady);
		tStatusStop = (TextView) findViewById(R.id.tRedStop);
		
		updateTimer = new Timer();
		UpdateTask = new updateTask(new Handler(), this);
		
		audiomanage = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		
		//get saved
		SharedPreferences settings = getSharedPreferences("Data", Context.MODE_PRIVATE);
		state = settings.getInt("state", 0);
		Time = settings.getString("Time", "0:00");
		tTimeLeft.setText(" זמן ביטוח נותר"+Time);
		//get settings saved
		SharedPreferences settings2 = getApplicationContext().getSharedPreferences("DriveData", 0);
		S_SecureNum= settings2.getString("SAVESecureNum", "");
		S_silent2 = settings2.getBoolean("SAVEsilent2", true);
		S_BTon = settings2.getBoolean("SAVEBTon", true);
		S_BTconnect = settings2.getBoolean("SAVEBTconnect", true);
		S_SndAgain = settings.getBoolean("SAVESndAgain", true);
		
		System.out.println("PreStart: " + state);
		Ui();
	}
	public void Ui(){
		switch(state){
		case 0: //off
			Bstart.setEnabled(true); Bstart.setBackgroundColor(Color.parseColor("#00ff00"));
			Bend.setEnabled(false); Bend.setBackgroundColor(Color.parseColor("#800000"));
			//set green false
			tStatusG0.setTextColor(Color.parseColor("#999999"));
			tStatusG0.setBackgroundColor(Color.parseColor("#333333"));
			//set yellow false
			tStatusReady.setTextColor(Color.parseColor("#999999"));
			tStatusReady.setBackgroundColor(Color.parseColor("#333333"));
			//set red 
			tStatusStop.setTextColor(Color.parseColor("#000000"));
			tStatusStop.setBackgroundColor(Color.parseColor("#ff0000"));
			break;
		case 1:  //start
			tStatusReady.setText("היכון לנסיעה");
			Bstart.setEnabled(false); Bstart.setBackgroundColor(Color.parseColor("#008000"));
			Bend.setEnabled(false); Bend.setBackgroundColor(Color.parseColor("#800000"));
			//set red false
			tStatusStop.setTextColor(Color.parseColor("#999999"));
			tStatusStop.setBackgroundColor(Color.parseColor("#333333"));
			//set green false
			tStatusG0.setTextColor(Color.parseColor("#999999"));
			tStatusG0.setBackgroundColor(Color.parseColor("#333333"));
			//set yellow
			tStatusReady.setTextColor(Color.parseColor("#000000"));
			tStatusReady.setBackgroundColor(Color.parseColor("#ffff00"));
			break;
		case 2:  // end
			tStatusReady.setText("בקשת הפסקה נשלחה");
			Bstart.setEnabled(false); Bstart.setBackgroundColor(Color.parseColor("#008000"));
			Bend.setEnabled(false); Bend.setBackgroundColor(Color.parseColor("#800000"));
			//set red false
			tStatusStop.setTextColor(Color.parseColor("#999999"));
			tStatusStop.setBackgroundColor(Color.parseColor("#333333"));
			//set green false
			tStatusG0.setTextColor(Color.parseColor("#999999"));
			tStatusG0.setBackgroundColor(Color.parseColor("#333333"));
			//set yellow
			tStatusReady.setTextColor(Color.parseColor("#000000"));
			tStatusReady.setBackgroundColor(Color.parseColor("#ffff00"));
			break;
		case 3: //on
			tTimeLeft.setText("זמן ביטוח נותר"+Time);
			Bstart.setEnabled(false); Bstart.setBackgroundColor(Color.parseColor("#008000"));
			Bend.setEnabled(true); Bend.setBackgroundColor(Color.parseColor("#ff0000"));
			//set red false
			tStatusStop.setTextColor(Color.parseColor("#999999"));
			tStatusStop.setBackgroundColor(Color.parseColor("#333333"));
			//set yellow false
			tStatusReady.setTextColor(Color.parseColor("#999999"));
			tStatusReady.setBackgroundColor(Color.parseColor("#333333"));
			//set green
			tStatusG0.setTextColor(Color.parseColor("#000000"));
			tStatusG0.setBackgroundColor(Color.parseColor("#00ff00"));
			break;
		}
		
		SharedPreferences settings = getSharedPreferences("Data", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("state", state);
	}

	private void CancelTimer(){
		try{
			updateTimer.cancel();
			updateTimer.purge();
			updateTimer = null;
			updateTimer = new Timer();
			UpdateTask = new updateTask(new Handler(), this);
		}catch(Exception e){
			System.out.println("Cant cancel timer");
		}
	}
	/**
	 * Update every second since start secure
	 */
	public void update(){
		++counter;
		
		if (state ==3){
			String Time2 = SecToTime.Sec2Time((counter-Start_drive));
			tDriveTime.setText("זמן נסיעה"+" "+Time2);
		}
		
		// Send SMS again
		if (counter == sendTime + 90){
			if(S_SndAgain){
				if(state==1){
					sms.send(test_SendTo, "1 "+S_SecureNum+" 1");
					sendTime = counter;
				}else if(state==2){
					sms.send(test_SendTo, "1 "+S_SecureNum+" 2");
					sendTime = counter;
				}
			}
		}
		
		// End drive if BT disconnect
		if (state==3){	
			if (counter%60==0){
				if(S_BTconnect){
					BTconnect = BT.isConect();
					if (BTlast){
						if (!BTconnect){
							endDrive();
						}
					}
					BTlast=BTconnect;
					System.out.println("BT 60% = " + BTconnect.toString());
				}
			}
		}
		
	}
	private class updateTask extends TimerTask {
		Handler handler; 
		MainActivity ref; 

		public updateTask(Handler handler, MainActivity ref) { 
			super(); 
			this.handler = handler; 
			this.ref = ref; 
		} 
		
		@Override 
		public void run() { 
			handler.post(new Runnable() {				 
				@Override 
				public void run() { 
					ref.update(); 
				} 
			}); 
		}	 
	} 

	private void CreateNotification(){
		NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        @SuppressWarnings("deprecation")
		Notification notify=new Notification(R.drawable.ic_launcher,"ביטוח מופעל",System.currentTimeMillis());
        PendingIntent pending= PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0);
        
        notify.flags=Notification.FLAG_NO_CLEAR;
        notify.setLatestEventInfo(getApplicationContext(),"ביטוח מופעל","שים לב! הביטוח שלך מופעל",pending);
        notif.notify(0, notify);
	}
}
