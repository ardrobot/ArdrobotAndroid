package com.toddsampson.toddbot;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	private FileInputStream mInput;
	private FileOutputStream mOutput;
	private String Tag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			openAccessory();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void openAccessory() {
		UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);

		Intent intent = getIntent();
		UsbAccessory accessory = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);

		ParcelFileDescriptor mFD = manager.openAccessory(accessory);
		
		if(mFD != null) {
			FileDescriptor fd = mFD.getFileDescriptor();
			try {
				mInput = new FileInputStream(fd);
				mOutput = new FileOutputStream(fd);
				Log.d(Tag, mInput.toString());
				Log.d(Tag, mOutput.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Log.d(Tag, "didn't connect");
		}
	}
}
