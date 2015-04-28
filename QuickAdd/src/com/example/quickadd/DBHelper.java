package com.example.quickadd;

import java.util.ArrayList;
import java.util.Calendar;

import com.example.quickadd.GridData.DifficultyLevel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	private static final String TAG = "Database Helper";
	public static final String DATABASE_NAME = "scores.db";
	public static final String SCORE_HISTORY_TABLE_NAME = "scores";
	public static final String SCORE_HISTORY_COLUMN_ID = "id";
	public static final String SCORE_HISTORY_COLUMN_DATE = "date";
	public static final String SCORE_HISTORY_COLUMN_MONTH = "month";
	public static final String SCORE_HISTORY_COLUMN_YEAR = "year";
	public static final String SCORE_HISTORY_COLUMN_SCORE = "score";
	public static final String SCORE_HISTORY_COLUMN_COUNTER = "counter";
	public static final String SCORE_HISTORY_DIFFICULTY_LEVEL = "difficulty";
	
	
	private static final String TEXT_TYPE = " TEXT";
	private static final String INTEGER_TYPE = " INTEGER";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_TABLE =
	    "CREATE TABLE IF NOT EXISTS " + SCORE_HISTORY_TABLE_NAME + " (" +
	    		SCORE_HISTORY_COLUMN_ID + " INTEGER PRIMARY KEY," +
	    		SCORE_HISTORY_COLUMN_DATE + INTEGER_TYPE + COMMA_SEP +
	    		SCORE_HISTORY_COLUMN_MONTH + INTEGER_TYPE + COMMA_SEP +
	    		SCORE_HISTORY_COLUMN_YEAR + INTEGER_TYPE + COMMA_SEP +
	    		SCORE_HISTORY_COLUMN_SCORE + INTEGER_TYPE + COMMA_SEP +
	    		SCORE_HISTORY_COLUMN_COUNTER + INTEGER_TYPE + COMMA_SEP +
	    		SCORE_HISTORY_DIFFICULTY_LEVEL + INTEGER_TYPE +
		")";

	private static final String SQL_DELETE_ENTRIES =
	    "DROP TABLE IF EXISTS " + SCORE_HISTORY_TABLE_NAME;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.i(TAG,"on create");
		db.execSQL(SQL_DELETE_ENTRIES);
		db.execSQL(SQL_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
	}
	
	public Boolean insertScore(int score,int level) {
		//Log.i(TAG,"what the fuck");
		//SQLiteDatabase db = this.getWritableDatabase();
		//db.execSQL(SQL_DELETE_ENTRIES);
		//db.execSQL(SQL_CREATE_TABLE);
		Calendar c = Calendar.getInstance();
		int date = c.get(Calendar.DATE);
		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);
		
		
		
		if (!updateScoreInTable(date, month, year, score,level)) {
			insertRowInTable(date, month, year, score,level);
		}
		
		return true;
	}
	
	public void insertRowInTable(int date,int month,int year,int score,int level) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues content = new ContentValues();
		content.put(SCORE_HISTORY_COLUMN_ID, 
				(int) DatabaseUtils.queryNumEntries(db, SCORE_HISTORY_TABLE_NAME) + 1);
		content.put(SCORE_HISTORY_COLUMN_DATE, date);
		content.put(SCORE_HISTORY_COLUMN_MONTH, month);
		content.put(SCORE_HISTORY_COLUMN_YEAR,year);
		content.put(SCORE_HISTORY_COLUMN_SCORE, score);
		content.put(SCORE_HISTORY_COLUMN_COUNTER, 1);
		content.put(SCORE_HISTORY_DIFFICULTY_LEVEL, level);
		
		Log.i(TAG,"date:"+ date + "month:"+ month + "year:"+ year);
		
		db.insert(SCORE_HISTORY_TABLE_NAME, null, content);
	}
	
	private Boolean updateScoreInTable(int date,int month,int year,int score,int level) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("SELECT "+ SCORE_HISTORY_COLUMN_ID + COMMA_SEP  +
				SCORE_HISTORY_COLUMN_SCORE + COMMA_SEP +
				SCORE_HISTORY_COLUMN_COUNTER + " FROM " + SCORE_HISTORY_TABLE_NAME +
				" WHERE " + SCORE_HISTORY_COLUMN_DATE + " = " + date + " AND " +
				SCORE_HISTORY_COLUMN_MONTH + " = " + month + " AND " +
				SCORE_HISTORY_COLUMN_YEAR + " = " + year + " AND " +
				SCORE_HISTORY_DIFFICULTY_LEVEL + " = " + level + "", null);
		
		if (res.getCount() > 0) {
			res.moveToFirst();
			updateScore(res.getInt(res.getColumnIndex(SCORE_HISTORY_COLUMN_ID)), 
					res.getInt(res.getColumnIndex(SCORE_HISTORY_COLUMN_SCORE)), score, 
					res.getInt(res.getColumnIndex(SCORE_HISTORY_COLUMN_COUNTER)));
			res.close();
			return true;
		}
		return false;
	}
	
	private void updateScore(int id,int oldScore, int newScore, int counter) {
		SQLiteDatabase db = this.getWritableDatabase();
		int newAvg = ((oldScore * counter) + newScore)/ (counter + 1);
		ContentValues content = new ContentValues();
		content.put(SCORE_HISTORY_COLUMN_SCORE, newAvg);
		content.put(SCORE_HISTORY_COLUMN_COUNTER, counter+1);
		db.update(SCORE_HISTORY_TABLE_NAME, content, SCORE_HISTORY_COLUMN_ID + " = " + id, null);
	}
	
	public ChartData getScoresAndDates(int month, int year,int level) {
		SQLiteDatabase db = this.getReadableDatabase();
		ChartData data;
		ArrayList<Integer> scores = new ArrayList<Integer>();
		ArrayList<Integer> dates = new ArrayList<Integer>();
		Cursor res = db.rawQuery("SELECT "+ SCORE_HISTORY_COLUMN_SCORE + COMMA_SEP +
				SCORE_HISTORY_COLUMN_DATE + " FROM " + SCORE_HISTORY_TABLE_NAME +
				" WHERE " + SCORE_HISTORY_COLUMN_MONTH + " = " + month + " AND " +				
				SCORE_HISTORY_COLUMN_YEAR + " = " + year + " AND " +
				SCORE_HISTORY_DIFFICULTY_LEVEL + " = " + level + "", null);
		
		if (res.getCount() > 0) {
			res.moveToFirst();
			while (res.isAfterLast() == false) {
				scores.add(res.getInt(res.getColumnIndex(SCORE_HISTORY_COLUMN_SCORE)));
				dates.add(res.getInt(res.getColumnIndex(SCORE_HISTORY_COLUMN_DATE)));
				res.moveToNext();
			}
			data = new ChartData(scores,dates,month,year);
			res.close();
		} else {
			data = new ChartData();
			res.close();
		}
		
		return data;
	}

}
