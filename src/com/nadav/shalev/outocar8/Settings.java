package com.nadav.shalev.outocar8;

import android.support.v7.app.ActionBarActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Settings extends ActionBarActivity {

	Button bSave,Button1,Button2,Button3,Button4,Button5;
	EditText eSecure;
	CheckBox chSilent,chBTon,chBTconnect,chAgain;
	String SecureNum;
	Boolean silent2,BTon,BTconnect,SndAgain;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		bSave = (Button) findViewById(R.id.bSave);
		eSecure = (EditText) findViewById(R.id.Escure);
		chSilent = (CheckBox) findViewById(R.id.chSilent);
		chBTon = (CheckBox) findViewById(R.id.chBTon);
		chBTconnect = (CheckBox) findViewById(R.id.chBTconnect);
		chAgain = (CheckBox) findViewById(R.id.chAgain);
		
		Button1 = (Button) findViewById(R.id.Button1);
		Button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "הכנס את מספר הביטוח האישי שקבלת", Toast.LENGTH_LONG).show();
			}});
		Button2 = (Button) findViewById(R.id.Button2);
		Button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "בזמן נסיעה, הטלפון יעבור תמיד למצב שקט על מנת לא להפריע בנסיעה", Toast.LENGTH_LONG).show();
			}});
		Button3 = (Button) findViewById(R.id.Button3);
		Button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "בזמן נסיעה, הבלוטות' יופעל באופן אוטומטי כדי להתחבר לאוטו", Toast.LENGTH_LONG).show();
		}});
		Button4 = (Button) findViewById(R.id.button4);
		Button4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "כאשר הבלוטות' יתנתק מהאוטו, הוא יפסיק את הביטוח באופן אוטומטי", Toast.LENGTH_LONG).show();
				Toast.makeText(getApplicationContext(), "זהו מנגנון הגנה בלבד ואין לסמוך על כך", Toast.LENGTH_LONG).show();
		}});
		Button5 = (Button) findViewById(R.id.Button5);
		Button5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "הטלפון יבקש הודעת אישור מהביטוח שוב ושוב עד שזו תתקבל. על מנת לוודא שהביטוח אכן הופעל או הופסק", Toast.LENGTH_LONG).show();
		}});

		//get from database
		SharedPreferences settings = getApplicationContext().getSharedPreferences("DriveData", 0);
		SecureNum= settings.getString("SAVESecureNum", "");
		silent2 = settings.getBoolean("SAVEsilent2", true);
		BTon = settings.getBoolean("SAVEBTon", true);
		BTconnect = settings.getBoolean("SAVEBTconnect", true);
		SndAgain = settings.getBoolean("SAVESndAgain", true);
		
		//set
		eSecure.setText(SecureNum);
		chSilent.setChecked(silent2);
		chBTon.setChecked(BTon);
		chBTconnect.setChecked(BTconnect);
		chAgain.setChecked(SndAgain);
		
bSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//update
				SecureNum = eSecure.getText().toString();
				silent2 = chSilent.isChecked();
				BTon = chBTon.isChecked();
				BTconnect = chBTconnect.isChecked();
				SndAgain = chAgain.isChecked();
				
				
				
				//save
				SharedPreferences settings = getApplicationContext().getSharedPreferences("DriveData", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("SAVESecureNum", SecureNum);
				editor.putBoolean("SAVEsilent2", silent2);
				editor.putBoolean("SAVEBTon", BTon);
				editor.putBoolean("SAVEBTconnect", BTconnect);
				editor.putBoolean("SAVESndAgain", SndAgain);
				editor.apply();
				
				startActivity(new Intent(Settings.this,MainActivity.class));
			}
		});

	}

}
