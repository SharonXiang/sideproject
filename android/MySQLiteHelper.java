/*Database for storing a list of alarms*/

package com.example.wakemeup;

import java.util.ArrayList;
import java.util.List;

import com.example.wakemeup.AlarmData.Columns;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	// Database Name
	private static final String DATABASE_NAME = "alarmdata";

	// Database version
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	static final String DATABASE_CREATE = "CREATE TABLE "
			+ Columns.ALARM_TABLE_NAME + " (" + Columns._ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Columns.HOUR + " HOUR,"
			+ Columns.MINUTE + " MINUTE," + Columns.REPEAT + " REPEAT,"
			+ Columns.RINGTONE + " RINGTONE," + Columns.VIBRATE + " VIBRATE,"
			+ Columns.LABEL + " LABEL," + Columns.TIME_STRING + " TIMESTRING,"
			+ Columns.AMPM + " AMPM," + Columns.ENABLE + " ENABLE,"
			+ Columns.SNOOZE + " SNOOZE," + Columns.DIFFICULTY + " DIFFICULTY"
			+ ");";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		Log.i("myAlarm", "onCreat");
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
		onCreate(db);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

	/* **********************************************
	 * 
	 * CRUD(Create, Read, Update, Delete) Operations
	 * 
	 * *********************************************
	 */
	public int alarmCount() {
		String countQuery = "SELECT COUNT(*) FROM " + Columns.ALARM_TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.moveToFirst();
		int i = cursor.getInt(0);
		cursor.close();
		// return count
		return i;
	}

	// Add a new alarm to the database
	public void addAlarm(int h, int m, int repeat, String ringtone,
			boolean vibrate, String label, String ts, String ap, int snooze,
			int diff) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Columns.HOUR, h);
		values.put(Columns.MINUTE, m);
		values.put(Columns.REPEAT, repeat);
		values.put(Columns.RINGTONE, ringtone);
		values.put(Columns.VIBRATE, vibrate);
		values.put(Columns.LABEL, label);
		values.put(Columns.TIME_STRING, ts);
		values.put(Columns.AMPM, ap);
		values.put(Columns.ENABLE, 1);
		values.put(Columns.SNOOZE, snooze);
		values.put(Columns.DIFFICULTY, diff);

		// Inserting Row
		db.insert(Columns.ALARM_TABLE_NAME, null, values);
		// db.close(); // Closing database connection
	}

	// Update an alarm
	public void updateAlarm(long id, int h, int m, int repeat, String ringtone,
			boolean vibrate, String label, String ts, String ap, int snooze,
			int diff) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(Columns.HOUR, h);
		values.put(Columns.MINUTE, m);
		values.put(Columns.REPEAT, repeat);
		values.put(Columns.RINGTONE, ringtone);
		values.put(Columns.VIBRATE, vibrate);
		values.put(Columns.LABEL, label);
		values.put(Columns.TIME_STRING, ts);
		values.put(Columns.AMPM, ap);
		values.put(Columns.ENABLE, 1);
		values.put(Columns.SNOOZE, snooze);
		values.put(Columns.DIFFICULTY, diff);

		db.update(Columns.ALARM_TABLE_NAME, values, Columns._ID + "= ?",
				new String[] { String.valueOf(id) });
	}

	// Delete an alarm
	public void deleteAlarm(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(Columns.ALARM_TABLE_NAME, Columns._ID + "=?",
				new String[] { String.valueOf(id) });
		// db.close();
	}

	// Delete all existing alarms
	public void deleteAllAlarm() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(Columns.ALARM_TABLE_NAME, null, null);
	}

	// Check if a table exists
	public boolean isTableExists(String tableName, boolean openDb) {
		SQLiteDatabase mDatabase = null;
		if (openDb) {
			if (mDatabase == null || !mDatabase.isOpen()) {
				mDatabase = getReadableDatabase();
			}

			if (!mDatabase.isReadOnly()) {
				mDatabase.close();
				mDatabase = getReadableDatabase();
			}
		}

		Cursor cursor = mDatabase.rawQuery(
				"select DISTINCT tbl_name from sqlite_master where tbl_name = '"
						+ tableName + "'", null);
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				cursor.close();
				return true;
			}
			cursor.close();
		}
		return false;
	}

	// Get single alarms
	public Cursor getAlarm(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + Columns.ALARM_TABLE_NAME
				+ " WHERE " + Columns._ID + " = " + id, null);
		// cursor.close();
		return cursor;
	}

	// Get all alarms
	public List<Alarm> getAllAlarms() {
		List<Alarm> alarmList = new ArrayList<Alarm>();
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM " + Columns.ALARM_TABLE_NAME;
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Alarm alarm = new Alarm();
				alarm.setID(Integer.parseInt(cursor.getString(0)));
				alarm.setHour(Integer.parseInt(cursor.getString(1)));
				alarm.setMinutes(Integer.parseInt(cursor.getString(2)));
				alarm.setRepeat(Integer.parseInt(cursor.getString(3)));
				alarm.setRingtone(cursor.getString(4));
				alarm.setVibrate(Integer.parseInt(cursor.getString(5)));
				alarm.setLabel(cursor.getString(6));
				// Add alarms to the list
				alarmList.add(alarm);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return alarmList;
	}

	public Cursor getAllData() {
		SQLiteDatabase db = this.getReadableDatabase();
		String buildSQL = "SELECT * FROM " + Columns.ALARM_TABLE_NAME;
		return db.rawQuery(buildSQL, null);
	}

	// Get the repeat day
	public int getAlarmRepeat(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(Columns.ALARM_TABLE_NAME,
				new String[] { Columns.REPEAT }, Columns._ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		int i = Integer.parseInt(cursor.getString(0));
		cursor.close();
		return i;
	}

	// Get the time string
	public String getTimeString(long i) {
		int id = (int) i;
		String s = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(Columns.ALARM_TABLE_NAME, new String[] {
				Columns.TIME_STRING, Columns.AMPM }, Columns._ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			s = cursor.getString(0) + " " + cursor.getString(1);
		}
		cursor.close();
		return s;
	}

	// Get the snooze duration
	public int getSnooze(int id) {
		int i = 1;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(Columns.ALARM_TABLE_NAME,
				new String[] { Columns.SNOOZE }, Columns._ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			i = Integer.parseInt(cursor.getString(0));
		}
		cursor.close();
		return i;
	}

	// Get the game difficulty
	public int getDifficulty(int id) {
		int i = 2;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(Columns.ALARM_TABLE_NAME,
				new String[] { Columns.DIFFICULTY }, Columns._ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			i = Integer.parseInt(cursor.getString(0));
		}
		cursor.close();
		return i;
	}

	// Set the state of the toggle
	public void setSwitch(boolean b, int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		if (b) {
			value.put(Columns.ENABLE, 1);
		} else {
			value.put(Columns.ENABLE, 0);
		}
		db.update(Columns.ALARM_TABLE_NAME, value, Columns._ID + "= ?",
				new String[] { String.valueOf(id) });
	}

	// Disable all alarms
	public void disableAllAlarm() {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Columns.ENABLE, 0);
		db.update(Columns.ALARM_TABLE_NAME, value, null, null);
	}

}
