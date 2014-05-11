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
/
