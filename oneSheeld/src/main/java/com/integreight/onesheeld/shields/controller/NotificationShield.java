package com.integreight.onesheeld.shields.controller;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.integreight.firmatabluetooth.ShieldFrame;
import com.integreight.onesheeld.MainActivity;
import com.integreight.onesheeld.R;
import com.integreight.onesheeld.enums.UIShield;
import com.integreight.onesheeld.shields.ControllerParent;

public class NotificationShield extends ControllerParent<NotificationShield> {
	private NotificationEventHandler eventHandler;
	private String lastNotificationText;
	private static final byte NOTIFY_PHONE_METHOD_ID = (byte) 0x01;

	public String getLastNotificationText() {
		return lastNotificationText;
	}

	public NotificationShield() {
		super();
	}

	public NotificationShield(Activity activity, String tag) {
		super(activity, tag);
	}

	@Override
	public void onUartReceive(byte[] data) {
		// if (data.length < 2)
		// return;
		// byte command = data[0];
		// byte methodId = data[1];
		// int n = data.length - 2;
		// byte[] newArray = new byte[n];
		// System.arraycopy(data, 2, newArray, 0, n);
		// if (command == NOTIFICATION_COMMAND) {
		// String notificationText = new String(newArray);
		// lastNotificationText = notificationText;
		// if (methodId == NOTIFY_PHONE_METHOD_ID) {
		// showNotification(notificationText);
		// eventHandler.onNotificationReceive(notificationText);
		// }
		//
		// }
		// super.onUartReceive(data);
	}

	protected void showNotification(String notificationText) {
		// TODO Auto-generated method stub
		NotificationCompat.Builder build = new NotificationCompat.Builder(
				activity);
		build.setSmallIcon(R.drawable.white_ee_icon);
		build.setContentTitle(notificationText);
		build.setContentText("Notification received from 1Sheeld!");
		build.setTicker(notificationText);
		// build.setContentInfo("");
		build.setWhen(System.currentTimeMillis());
		Toast.makeText(activity, notificationText, Toast.LENGTH_SHORT).show();
		// PendingIntent pendingIntent = PendingIntent.getService(this, 0, new
		// Intent(this, OneSheeldService.class), 0);

		// build.addAction(R.drawable.action_cancel,
		// "Close Service",pendingIntent );
		// build.addAction(R.drawable.action_cancel,
		// "Close Service",pendingIntent );
		// build.addAction(R.drawable.action_cancel,
		// "Close Service",pendingIntent );
		// Intent notificationIntent = new Intent(activity, MainActivity.class);
		// notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
		// | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		//
		// PendingIntent intent = PendingIntent.getActivity(this, 0,
		// notificationIntent, 0);

		// build.setContentIntent(intent);
		Vibrator v = (Vibrator) activity
				.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(1000);
		Intent notificationIntent = new Intent(activity, MainActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent intent = PendingIntent.getActivity(activity, 0,
				notificationIntent, 0);

		build.setContentIntent(intent);
		Notification notification = build.build();
		NotificationManager notificationManager = (NotificationManager) activity
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(2, notification);
	}

	public void setNotificationEventHandler(
			NotificationEventHandler eventHandler) {
		this.eventHandler = eventHandler;

	}

	public interface NotificationEventHandler {
		void onNotificationReceive(String notificationText);
	}

	@Override
	public void onNewShieldFrameReceived(ShieldFrame frame) {
		// TODO Auto-generated method stub
		if (frame.getShieldId() == UIShield.NOTIFICATION_SHIELD.getId()) {
			String notificationText = frame.getArgumentAsString(0);
			lastNotificationText = notificationText;
			if (frame.getFunctionId() == NOTIFY_PHONE_METHOD_ID) {
				showNotification(notificationText);
				if (eventHandler != null)
					eventHandler.onNotificationReceive(notificationText);
			}

		}

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}