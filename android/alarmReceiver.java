/*Wake me up - Android alarm app*/
/*part of coding package*/

package com.example.wakemeup;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	private PowerManager mPowerManager;
	private PowerManager.WakeLock mWakeLock;
	final public static String ONE_TIME = "onetime";

	@SuppressLint({ "Wakelock", "SimpleDateFormat" })
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		mPowerManager = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		mWakeLock = mPowerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.ON_AFTER_RELEASE, "WakeLock");
		mWakeLock.acquire();

		// You can do the processing here update the widget/remote views.
		Bundle extras = intent.getExtras();
		StringBuilder msgStr = new StringBuilder();
		Intent i = new Intent(context, Alert.class);
		Intent serviceIntent = new Intent(context, AlarmService.class);

		if (extras != null) {
			if (extras.getInt("REPEAT") == 0) {
				msgStr.append("One Time Timer: ");
				i.putExtra("oneTime", true);
			} else {
				msgStr.append("Repeat Timer: ");
				i.putExtra("oneTime", false);
			}

			Format formatter = new SimpleDateFormat("hh:mm:ss a");
			msgStr.append(formatter.format(new Date()));
			msgStr.append(" + id: " + extras.getInt("ID"));

			// Set the intents
			serviceIntent.putExtra("RINGTONE", extras.getString("RINGTONE"));
			serviceIntent.putExtra("VIBRATION", extras.getBoolean("VIBRATION"));
			Log.i("myAlarm", "receive ring: " + extras.getString("RINGTONE"));
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.putExtra("ID", extras.getInt("ID"));
			i.putExtra("LABEL", extras.getString("LABEL"));

			// Start the Alert activity and AlarmService
			context.startService(serviceIntent);
			context.startActivity(i);
			// Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();
		}
		mWakeLock.release();
	}

	public void setSnooze(Context context, int id, int snooze) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, 0);

		am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + snooze
				* 60 * 1000, pi);

		Toast.makeText(context, "Snooze for " + snooze + "minute(s)",
				Toast.LENGTH_SHORT).show();
	}

	public void cancelAlarm(Context context, long l) {
		int id = (int) l;
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, id, intent,
				0);
		am.cancel(sender);

	}

	public void setTimer(Context context, int hour, int minute, int day,
			boolean vib, String ringtone, int id, String label) {
		Log.i("myAlarm", "setTimer ring: " + ringtone);

		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent timerIntent = new Intent(context, AlarmReceiver.class);
		timerIntent.putExtra("VIBRATION", vib);
		timerIntent.putExtra("ID", id);
		timerIntent.putExtra("LABEL", label);
		timerIntent.putExtra("RINGTONE", ringtone);
		timerIntent.putExtra("REPEAT", day);
		PendingIntent pi = PendingIntent.getBroadcast(context, id, timerIntent,
				0);

		final Calendar c = Calendar.getInstance();
		int d = c.get(Calendar.DAY_OF_WEEK);
		int h = c.get(Calendar.HOUR_OF_DAY);
		int m = c.get(Calendar.MINUTE);
		int s = c.get(Calendar.SECOND);
		int ms = c.get(Calendar.MILLISECOND); // current time

		int repeatT = 0;
		int repeatd = 0;
		if ((day - d) == 0) {

			repeatd = 0;// if the repeat date is the same day. e.g today is
						// Monday, repeat on every Monday
		}

		if ((day - d) > 0) {
			repeatd = (day - d) * 24 * 60 * 60 * 1000;
		}

		if ((day - d) < 0) {

			repeatd = (7 + (day - d)) * 24 * 60 * 60 * 1000;
		}

		int ch = 0;
		if ((hour - h) >= 0) {
			ch = (hour - h) * 60 * 60 * 1000;
		}

		if ((hour - h) < 0) {
			ch = (hour - h + 24) * 60 * 60 * 1000;
		}

		int cm = (minute - m) * 60 * 1000;
		int cs = (0 - s) * 1000;
		int msd = (0 - ms); // calculate difference between set time and current
		// time

		int restMinutes;
		int restHours;
		if((hour - h) >= 0){
		restHours = hour - h;
		}else {
			restHours = 24 + (hour - h);
		}
		if((minute - m) == 1){
			
			restMinutes = 0;
		}else{
			if (minute - m < 0){
				restMinutes = 60 - (m - minute);
				restHours = 24 + (hour - h);
			}else {
				restMinutes = minute - m;
			}
		}

		if (day == 0) {
			repeatT = 0;
			repeatd = 0;

			if (minute - m < 0) {

				repeatd = 24 * 60 * 60 * 1000;
			} else {
				repeatd = 0;
			}

			am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ch
					+ cm + cs + msd + repeatd, pi);

			// Toast Message
			StringBuilder toastMsg = new StringBuilder();
			toastMsg.append("The next alarm will be launched in ");

			if (restHours > 0) {
				toastMsg.append(restHours + " hour(s), ");
			}

			if (restMinutes > 1) {
				toastMsg.append(restMinutes + " minutes.");
			}

			else {
				toastMsg.append("less than 1 minute.");
			}
			Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show();

		} else {

			if (minute - m < 0) {

				repeatd = 24 * 60 * 60 * 1000;
			} 

			repeatT = 86400000 * 7;

			am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
					+ ch + cm + cs + msd + repeatd, repeatT, pi);

			int restDays = repeatd / (24 * 60 * 60 * 1000);

			// Toast Message
			StringBuilder toastMsg = new StringBuilder();
			toastMsg.append("The next alarm will be launched in ");

			if (restDays > 0) {
				toastMsg.append(restDays + " day(s), ");
			}

			if (restHours > 0) {
				toastMsg.append(restHours + " hour(s), ");
			}

			if (restMinutes > 1) {
				toastMsg.append(restMinutes + " minutes.");
			}

			else {
				toastMsg.append("less than 1 minute.");
			}
			Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show();
		}
	}
}
