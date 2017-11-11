package com.nadav.shalev.outocar8;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SendSms extends BroadcastReceiver {


	final SmsManager sms = SmsManager.getDefault();
	private static String sendToNum;
    private static String sendMessage;
    public static Boolean alreadyReseived = true;

    private static String receiveNum;
    public static String reseiveMessage;
    private static String sender;
	
	public void send(String number, String body){
		sendToNum = number;
		sendMessage = body;
		//alreadyReseived = false;
		try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(sendToNum, null, sendMessage, null, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {             
            if (bundle != null) {                 
                final Object[] pdusObj = (Object[]) bundle.get("pdus");                 
                for (int i = 0; i < pdusObj.length; i++) {                     
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress(); 
                    System.out.println(phoneNumber);
                    if (phoneNumber.contentEquals(MainActivity.test_SendFrom)){
                    	
                    	receiveNum = phoneNumber;
                    	reseiveMessage = currentMessage.getDisplayMessageBody();
                    	//alreadyReseived = true;
                    	MainActivity act = new MainActivity();
                    	act.Recieved();
                    }
                } // end for loop
              } // bundle is null
        } catch (Exception e) {Log.e("SmsReceiver", "Exception smsReceiver" +e);}
    }

	
	
	
}

