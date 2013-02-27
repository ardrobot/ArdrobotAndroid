package com.ardrobot.android;

import java.io.IOException;
import java.net.URI;

import android.hardware.Camera;
import org.ros.address.InetAddressFactory;
import org.ros.android.MessageCallable;
import org.ros.android.RosActivity;
import org.ros.android.view.RosTextView;
import org.ros.android.view.camera.RosCameraPreviewView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import org.ros.rosjava_tutorial_pubsub.Listener;

import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.ardrobot.androidopenaccessory.AndroidOpenAccessory;
import com.ardrobot.androidopenaccessory.UsbConnection;



public class MainActivity extends RosActivity {
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

		
		
		
		
		rosTextView.setMessageToStringCallable(new MessageCallable<String, geometry_msgs.Twist>() {
			@Override
			public String call(geometry_msgs.Twist message) {

				if (message.getLinear().getX() > 0) {
					Log.i("Direction", "Forward");
					direction = 'F';
				} else if (message.getLinear().getX() < 0) {
					Log.i("Direction", "Backward");
					direction = 'B';
				} else if (message.getLinear().getY() < 0) {
					Log.i("Direction", "Right");
					direction = 'R';
				} else if (message.getLinear().getY() > 0) {
					Log.i("Direction", "Left");
					direction = 'L';
				} else {
					Log.i("Direction", "Stop");
					direction = 'S';
				}
				
				
				byte[] buffer = new byte[1];
				buffer[0] = direction;
				
				try {
					Log.i("Ardrobot", "mv");
					mAccessory.publish("mv", buffer);
				} catch (IOException e) {
					e.printStackTrace();
				}

				
				
				String coords = String.valueOf(message.getLinear().getX()) + " : " + String.valueOf(message.getLinear().getY());
				return coords;
			}
		});

		
		
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
	
	@Override
	protected void init(NodeMainExecutor nodeMainExecutor) {
		listener = new Listener();


		NodeConfiguration nodeConfiguration =
				NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress());

		URI MyURI = URI.create("http://10.8.0.1:11311");

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
