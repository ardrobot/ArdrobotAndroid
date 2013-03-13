package com.ardrobot.android;

import geometry_msgs.Vector3;

import java.io.IOException;
import java.net.URI;

import org.ros.address.InetAddressFactory;
import org.ros.android.MessageCallable;
import org.ros.android.RosActivity;
import org.ros.android.view.RosTextView;
import org.ros.android.view.camera.RosCameraPreviewView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import org.ros.rosjava_tutorial_pubsub.Listener;

import android.hardware.Camera;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.ardrobot.androidopenaccessory.AndroidOpenAccessory;
import com.ardrobot.androidopenaccessory.UsbConnection;

public class MainActivity extends RosActivity {
	
	private static final String MASTER_URI = "http://10.8.0.1:11311";

	private static final String TAG = "Ardrobot";
	
	private AndroidOpenAccessory mAccessory;
	private UsbManager mUsbManager;
	private UsbConnection connection;
	private RosTextView<geometry_msgs.Twist> rosTextView;
	private Listener listener;
	private byte direction;
	private int cameraId;
	private RosCameraPreviewView rosCameraPreviewView;

	public MainActivity() {
		super("Ardrobot", "Ardrobot");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    
		setContentView(R.layout.activity_main);
	    rosCameraPreviewView = (RosCameraPreviewView) findViewById(R.id.ros_camera_preview_view);

		mUsbManager = (UsbManager) getSystemService(USB_SERVICE);
		connection = new UsbConnection(this, mUsbManager);
		mAccessory = new AndroidOpenAccessory(this);
				
		rosTextView = (RosTextView<geometry_msgs.Twist>) findViewById(R.id.text);
		rosTextView.setTopicName("cmd_vel");
		rosTextView.setMessageType(geometry_msgs.Twist._TYPE);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		try {
			mAccessory.connect(AndroidOpenAccessory.USB_ACCESSORY, connection);
		} catch (IOException e) {
			Log.e(TAG, "Unable to connect: " + e.getMessage(), e);
		}
		
		rosTextView.setMessageToStringCallable(new MessageCallable<String, geometry_msgs.Twist>() {
			@Override
			public String call(geometry_msgs.Twist message) {

				Vector3 vector = message.getLinear();
				double x = vector.getX();
				double y = vector.getY();

				if (x > 0) {
					Log.i(TAG, "Direction: Forward");
					direction = 'F';
				} else if (x < 0) {
					Log.i(TAG, "Direction: Backward");
					direction = 'B';
				} else if (y < 0) {
					Log.i(TAG, "Direction: Right");
					direction = 'R';
				} else if (y > 0) {
					Log.i(TAG, "Direction: Left");
					direction = 'L';
				} else {
					Log.i(TAG, "Direction: Stop");
					direction = 'S';
				}

				logDirection(direction);
				
				byte[] buffer = new byte[1];
				buffer[0] = direction;
				
				try {
					Log.i(TAG, "mv");
					mAccessory.publish("mv", buffer);
				} catch (IOException e) {
					Log.e(TAG, "Unable to publish: " + e.getMessage(), e);
				}
				
				StringBuilder sb = new StringBuilder();
				
				sb.append(x).append(":").append(y);
				String coords = sb.toString();
				Log.v(TAG, "Coordinates: " + coords);
				
				return coords;
			}
		});
	}
	
	private void logDirection(byte direction) {
		String dir = null;
		switch(direction) {
		case 'F' : dir = "Forward"; break;
		case 'B' : dir = "Backward"; break;
		case 'R' : dir = "Right"; break;
		case 'L' : dir = "Left"; break;
		case 'S' : dir = "Stop"; break;
		default:
			throw new IllegalArgumentException("Unknown direction code: " + direction);
		}
		
		Log.i(TAG, "Direction: " + dir);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		try {
			mAccessory.disconnect();
		} catch (IOException e) {
			Log.e(TAG, "Error while disconnecting: " + e.getMessage(), e);
		}
	}
	
	@Override
	protected void init(NodeMainExecutor nodeMainExecutor) {
		listener = new Listener();

		NodeConfiguration nodeConfiguration =
				NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress());

		URI MyURI = URI.create(MASTER_URI);

		//	    NodeConfiguration nodeConfiguration = NodeConfiguration.newPrivate();
		// At this point, the user has already been prompted to either enter the URI
		// of a master to use or to start a master locally.
		nodeConfiguration.setMasterUri(MyURI);
		nodeMainExecutor.execute(listener, nodeConfiguration);
		
		// The RosTextView is also a NodeMain that must be executed in order to
		// start displaying incoming messages.
		nodeMainExecutor.execute(rosTextView, nodeConfiguration);
		
	    cameraId = 0;
	    rosCameraPreviewView.setCamera(Camera.open(cameraId));
	    nodeMainExecutor.execute(rosCameraPreviewView, nodeConfiguration);
	}
}
