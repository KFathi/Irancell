package com.hoor.lostfinder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.hoor.lostfinder.R;

public class MainActivity extends Activity {

	private TextView txtvw;
	private SQLiteDatabase sqldb;
	private Button saveBackupNumber;
	private boolean panicMode;//For alarm when someone enter secretly 
	private boolean normalMode;//For alarm when mobile is not normal mode
	private boolean backupMode;//For sometimes mobile is stolen
	private static final int SETTINGS_RESULT = 1;//test
	private SharedPreferences sharedPrefs;

	@Override
 	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//txtvw = (TextView) findViewById(R.id.textinfo);
		// txtvw.setText(getPhoneNumber(getBaseContext())+";"+getSimSerialNumber(getApplicationContext()));
//////////////////////////////////////////////////////////////
Button btnSettings=(Button)findViewById(R.id.buttonSettings);
// start the UserSettingActivity when user clicks on Button
btnSettings.setOnClickListener(new View.OnClickListener() {

public void onClick(View v) {
// TODO Auto-generated method stub
Intent i = new Intent(getApplicationContext(), UserSettingActivity.class);
startActivityForResult(i, SETTINGS_RESULT);
}
});
////////////////////////////////////////////////////////////
		Log.e("lostFinder","MainActivity onCreate");
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		//getAccountNumber(this);
		//finish();
		//txtvw.setText(readFromFile());
		//Log.e("lostFinder",readFromFile());
		
		super.onStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.phone_number:
			txtvw.setText(getPhoneNumber(getApplicationContext()));
			Log.e("lostFinder",getPhoneNumber(getApplicationContext()));
			break;
		case R.id.sim_serialnumber:
			txtvw.setText(getSimSerialNumber(getApplicationContext()));
			Log.e("lostFinder",getSimSerialNumber(getApplicationContext()));
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// get mobile number
	public static String getPhoneNumber(Context context) {
		TelephonyManager phoneManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		// String phoneNumber = phoneManager.getSimOperatorName();//IR-MCI
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
	
	public void getAccountNumber(Context context) {
		AccountManager am = AccountManager.get(context);
		Account[] accounts = am.getAccounts();

		ArrayList<String> phoneAccounts = new ArrayList<String>();
		for (Account ac : accounts) {
		    String acname = ac.name;
		    String actype = ac.type;
		    // Take your time to look at all available accounts
		    Log.e("lostFinder","Accounts : " + acname + ", " + actype);
		}
	}
	
	private void writeToFile(String data) {
	    try {
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("bkno.txt", Context.MODE_PRIVATE));
	        outputStreamWriter.write(data);
	        outputStreamWriter.close();
	    }
	    catch (IOException e) {
	        Log.e("lostFinder", "File write failed: " + e.toString());
	    } 
	}

	private String readFromFile() {

	    String ret = "";

	    try {
	        InputStream inputStream = openFileInput("bkno.txt");

	        if ( inputStream != null ) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String receiveString = "";
	            StringBuilder stringBuilder = new StringBuilder();

	            while ( (receiveString = bufferedReader.readLine()) != null ) {
	                stringBuilder.append(receiveString);
	            }

	            inputStream.close();
	            ret = stringBuilder.toString();
	        }
	    }
	    catch (FileNotFoundException e) {
	        Log.e("lostFinder", "File not found: " + e.toString());
	    } catch (IOException e) {
	        Log.e("lostFinder", "Can not read file: " + e.toString());
	    }

	    return ret;
	}
	//پیامک به پشتیبان
	public void smstoBackupNumber(View v){
		
		// TEST
		Intent smsIn=new Intent("myCustomAction"); 
		sendBroadcast(smsIn);
		Log.e("lostFinder", "mainActivity_saveBackupNumber");
		
	}
	
	public void showNotification(){
		String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);


            int icon = R.drawable.ic_launcher;
            CharSequence tickerText = "Pet Parrot";
            long when = System.currentTimeMillis();

            Notification notification = new Notification(icon,tickerText, when);
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            Context context = getApplicationContext();
            CharSequence contentTitle = "Hungry!";
            CharSequence contentText = "your parrot food meter is: ";
            Intent notificationIntent = new Intent(context,
                    MainActivity.class);
            PendingIntent contentIntent = PendingIntent
                    .getActivity(context, 0, notificationIntent, 0);

            notification.setLatestEventInfo(context, contentTitle,
                    contentText, contentIntent);

            mNotificationManager.notify(1, notification);
            // Log.d("test", "Saving Data to File from Service.");
	}
	
	//پیامک موبایل یاب
	public void mobileFinderSMS(View v){
		Intent lint=new Intent(MainActivity.this,LostDialogActivity.class);
		startActivity(lint);
	}
	//////////////////////////////////////////////////////////////////////////
	  @Override
      protected void onActivityResult(int requestCode, int resultCode, Intent data)
      {
                  super.onActivityResult(requestCode, resultCode, data);

                      if(requestCode==SETTINGS_RESULT)
                      {
                          displayUserSettings();
                      }

      }
      
      private void displayUserSettings() 
      {
              sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

              String  settings = "";

              settings=settings+"شماره پشتیبان: " + sharedPrefs.getString("prefBackupNO", "NoNumber");

              settings=settings+"\n"+"فرمت موبایل یاب: "+ sharedPrefs.getString("prefSmsFormat", "NoFormat");
             
              TextView textViewSetting = (TextView) findViewById(R.id.textViewSettings);

              textViewSetting.setText(settings);
       }
}
