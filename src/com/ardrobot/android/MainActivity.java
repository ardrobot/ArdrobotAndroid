package com.ardrobot.android;

import java.io.IOException;

import android.app.Activity;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;

import com.ardrobot.androidopenaccessory.AndroidOpenAccessory;
import com.ardrobot.androidopenaccessory.UsbConnection;


public class MainActivity extends Activity {
	private AndroidOpenAccessory mAccessory;
	private UsbManager mUsbManager;
	private UsbConnection connection;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);

		mUsbManager = (UsbManager) getSystemService(USB_SERVICE);
		connection = new UsbConnection(this, mUsbManager);
		mAccessory = new AndroidOpenAccessory(this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		try {
			mAccessory.connect(AndroidOpenAccessory.USB_ACCESSORY, connection);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Temp sending data to Arduino
		byte[] buffer = new byte[1];
		buffer[0] = 1;

		try {
			Log.i("Ardrobot", "mv");
			mAccessory.publish("mv", buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		try {
			mAccessory.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
