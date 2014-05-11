package com.hoor.lostfinder;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import com.hoor.lostfinder.R;

public class LostReceiver extends BroadcastReceiver {

	private SmsMessage SMessage;
	private String sender;
	private String body;
	private Intent lostIntent;
	private String resultText = "UNKNOWN";
	private SharedPreferences sharedPrefs;
	private String smsFormat;//get lost finder format of sms 

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//create shared preferences
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		// Create Intent to Service handling
		lostIntent = new Intent(context, LostService.class);
		// Get running service list and check LonLatService is run
		context.stopService(lostIntent);
		Log.e("lostFinder","onReceive");
		// check Receiver action
		//get mobile number when mobile was boot
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) && sharedPrefs.getBoolean("prefIsBackupNO", false)) {
			Log.e("lostFinder", "StartUpBootReceiver");
			// start LoastService
			Intent lostIntent = new Intent(context,
					LostService.class);
			context.startService(lostIntent);
			
		} else if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")&& sharedPrefs.getBoolean("prefIsSmsFormat", false)) {
			// //////////////////////////////////////////////////////////////////
			Bundle extras = intent.getExtras();
			if (extras != null) {

				Object[] pdus = (Object[]) extras.get("pdus");
				SMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
				sender = SMessage.getOriginatingAddress();
				body = SMessage.getMessageBody().toString();
				////////////////////////////////////////////////////////
				
				//get sms format from shared preferences
				smsFormat=sharedPrefs.getString("prefSmsFormat", "no_format");
				// Check SMS body
				if (body.equals(smsFormat)) {
					Log.e("lostFinder", "SMS_RECEIVED frm :"+sender);
					//send me to alarm dialog
					dispalyDialog(context);
				}
			}
		} else if (intent.getAction().equals("android.provider.Telephony.SMS_SENT")) {
			
			switch (getResultCode()) {
			case Activity.RESULT_OK:
				resultText = "SMS SENT";
				// when SMS send to operator service must be down
				context.stopService(lostIntent);
				break;
			case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				resultText = "GENERIC_FAILURE";
				// Send Location SMS again
				context.startService(lostIntent);
				break;
			case SmsManager.RESULT_ERROR_RADIO_OFF:
				resultText = "RADIO_OFF";
				// Send Location SMS again
				context.startService(lostIntent);
				break;
			case SmsManager.RESULT_ERROR_NULL_PDU:
				resultText = "NULL_PDU";
				// Send Location SMS again
				context.startService(lostIntent);
				break;
			case SmsManager.RESULT_ERROR_NO_SERVICE:
				resultText = "NO_SERVICE";
				// Send Location SMS again
				context.stopService(lostIntent);
				break;
			}
			Log.e("lostFinder", resultText);
			//Toast.makeText(context, resultText, Toast.LENGTH_LONG).show();
		}
		else if (intent.getAction().equals(
				"android.provider.Telephony.SMS_DELIVER")) {
			switch (getResultCode()) {
			case Activity.RESULT_OK:
				resultText = "Delivered";
				break;
			case Activity.RESULT_CANCELED:
				resultText = "Not delivered";
				break;
			}
			Log.e("lostFinder", resultText);
			//Toast.makeText(context, resultText, Toast.LENGTH_LONG).show();
		}
		//Manual test
		else if (intent.getAction().equals("myCustomAction")) {
			
			context.startService(lostIntent);
		}
	}


	/**
	 * @param context
	 */
	private void dispalyDialog(Context context) {
		//Show dialog activity
		Intent dint=new Intent(context,LostDialogActivity.class);
		dint.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(dint);
		Log.e("lostFinder", "onReceive_AlarmDialogActivity");
	}

}
