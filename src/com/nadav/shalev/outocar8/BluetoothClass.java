package com.nadav.shalev.outocar8;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;

public class BluetoothClass {

	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	public void BTToggle(Boolean set,Boolean active){
		if(active){
			if (set) {
				mBluetoothAdapter.enable(); 
			} else{
				mBluetoothAdapter.disable();
			} 
		}
	}
	@SuppressLint("NewApi")
	public Boolean isConect(){
		Boolean st = false;
		int state = BluetoothAdapter.getDefaultAdapter().getProfileConnectionState(BluetoothProfile.HEADSET);
		if (state==BluetoothProfile.STATE_CONNECTED){
			System.out.println("BT_Connect!");
			st = true;
		}else if (state==BluetoothProfile.STATE_DISCONNECTED){
			System.out.println("BT_Disconnect!");
			st = false;
		}else{
			System.out.println("BT_Else!");
			st = false;
		}
		return st;
	}

    
}
