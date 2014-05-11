package com.hoor.lostfinder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import com.hoor.lostfinder.R;

public class LostService extends Service {

	private String backupNumber;
	private String SENT_SMS_ACTION = "SENT_SMS_ACTION";
	private String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
	private PendingIntent sentPI, deliverPI;
	private Intent sentIntent, deliveryIntent;
	private Context _context;
	private String mobileNumber = "Unknown";
	private String simSerialNumber = "Unknown";
	private SharedPreferences sharedPrefs;

	// Service///////////////////////////////////////////////////////

	@Override
	public void onCreate() {  
		// TODO Auto-generated method stub
		super.onCreate();
		// Define the criteria how to select the locatioin provider -> use
		// Get the location manager
		Log.e("lostFinder", "onCreate LostService");
		//
		// //////////////////////////////////////////////////////////
		// create pending intent to trace sms sent
		sentIntent = new Intent(SENT_SMS_ACTION);
		sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0,
				sentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		// Create the deliveryIntent parameter
		deliveryIntent = new Intent(DELIVERED_SMS_ACTION);
		deliverPI = PendingIntent.getBroadcast(getApplicationContext(), 0,
				deliveryIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		//////////////////////////////////////
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		//call dialog
		//
		Log.e("lostFinder", "onStartCommand LostService");
		// //////////////////////////////////////////////////////////
		_context = getBaseContext();
		mobileNumber = getPhoneNumber(_context);
		simSerialNumber = getSimSerialNumber(_context);
		//get backup number from Shared preferences
		backupNumber=sharedPrefs.getString("prefBackupNO","no_user");
		Log.e("lostFinder", backupNumber);
		// get Mobile number and serial number
		Log.e("lostFinder", mobileNumber + "\n" + simSerialNumber + "__"
				+ backupNumber);
		sendSMS(simSerialNumber);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Log.e("lostFinder", "onDestroy LostService");
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void sendSMS(String SerialNumber) {
		
		String simSE = SerialNumber.length() > 0 ? SerialNumber : "Unknown";
		Log.e("lostFinder", backupNumber +"SIM Serial : " + simSE);
		try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(backupNumber, null,"SIM Serial : " + simSE, sentPI, deliverPI);
			Log.e("lostFinder", "onStartCommand SMS Sent");

			/*
			 * Toast.makeText(getApplicationContext(), "SMS Sent!",
			 * Toast.LENGTH_LONG).show();
			 */
		} catch (Exception e) {
			/*
			 * Toast.makeText(getApplicationContext(),
			 * "SMS faild, please try again later!", Toast.LENGTH_LONG).show();
			 */
			Log.e("lostFinder", e.getMessage());
		}
	}

	// get mobile number
	public static String getPhoneNumber(Context context) {
		TelephonyManager phoneManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNumber = phoneManager.getLine1Number();
		return phoneNumber;
	}

	// get sim serial number
	public static String getSimSerialNumber(Context context) {
		TelephonyManager phoneManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNumber = phoneManager.getSimSerialNumber();
		return phoneNumber;
	}

	private String readFromFile() {

		String ret = "";

		try {
			InputStream inputStream = openFileInput("bkno.txt");

			if (inputStream != null) {
				InputStreamReader inputStreamReader = new InputStreamReader(
						inputStream);
				BufferedReader bufferedReader = new BufferedReader(
						inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while ((receiveString = bufferedReader.readLine()) != null) {
					stringBuilder.append(receiveString);
				}

				inputStream.close();
				ret = stringBuilder.toString();
			}
		} catch (FileNotFoundException e) {
			Log.e("lostFinder", "File not found: " + e.toString());
		} catch (IOException e) {
			Log.e("lostFinder", "Can not read file: " + e.toString());
		}

		return ret;
	}
	
}
