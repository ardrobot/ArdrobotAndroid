package com.toddsampson.toddbot;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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
		
		for(int x = 1; x < 20; x = x+1) {
			try {
				sendMessage('F');
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
	
	private void sendMessage(char direction) {
		byte[] buffer = new byte[2];

		buffer[0] = (byte) 0x2;
		buffer[1] = (byte) 0x46;
        if (mOutput != null && buffer[1] != -1) {
            try {
                mOutput.write(buffer);
            } catch (IOException e) {
                Log.e(Tag, "write failed", e);
            }
        }
	
	}
}
