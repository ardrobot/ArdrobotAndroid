package com.toddsampson.toddbot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

// receive USB_DEVICE_ATTACHED events and launch the main activity
public final class USBAccessoryActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent i = (new Intent(this, MainActivity.class));
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);

		finish();
	}
}
