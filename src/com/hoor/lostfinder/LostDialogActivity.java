package com.hoor.lostfinder;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.hoor.lostfinder.R;

public class LostDialogActivity extends Activity {

	private AudioManager myAudioManager;
	private MediaPlayer mediaPlayer;
	private AssetFileDescriptor afd;
	private Uri path;
	private ScheduledFuture<?> future;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/////////////////////////////////////////////
		//Set mobile to normal AudioManager
		myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		myAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, myAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
		myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		/////////////////////////////////////////////
		//Play Sound
		mediaPlayer= MediaPlayer.create(getBaseContext(),R.raw.lfalarm);
		afd = this.getResources().openRawResourceFd(R.raw.lfalarm);
		path = Uri.parse("android.resource://com.hoorshid.lostfinder/"
				+ R.raw.lfalarm);
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mediaPlayer.setDataSource(afd.getFileDescriptor());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediaPlayer.start();
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				mediaPlayer.start();
			}
		});
		/////////////////////////////////////////////
		//Show alert dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder
			.setTitle("موبایل یاب")
			.setMessage("موبایل در حالت صدا دار تنظیم شد")
			.setCancelable(false)
			.setIcon(R.drawable.ic_launcher)
			.setPositiveButton("Yes", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Log.e("lostFinder", "PositiveButton dialog");
					//Stop the alarm
					mediaPlayer.stop();
					//set dialog to dismiss
					dialog.dismiss();
					//Close the  activity
					finish();
				}
			});
			/*.setNegativeButton("No", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Log.e("lostFinder", "NegativeButton dialog");
					finish();
				}
			});*/
		final AlertDialog dialog = builder.create();
		dialog.show();
/*		// ///////////////////////////////////////////
				// media player looper
				// //////////////////////////////
				ScheduledExecutorService scheduler = Executors
						.newScheduledThreadPool(2);
				Runnable yourRecurrentTask = new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							mediaPlayer.setDataSource(afd.getFileDescriptor());
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// mediaPlayer.setDataSource(getApplicationContext(),path);
						try {
							mediaPlayer.prepare();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mediaPlayer.start();
					}
				};
				// run every seconds
				future = scheduler.scheduleAtFixedRate(
						yourRecurrentTask, 0, 1, TimeUnit.SECONDS);
				Runnable cancelTask = new Runnable() {
					@Override
					public void run() {
						future.cancel(true);
						mediaPlayer.stop();
					}
				};
				// cancel the reccurent task in 15 seconds
				scheduler.schedule(cancelTask, 120, TimeUnit.SECONDS);
				// //////////////////////////////
*/	}
}
