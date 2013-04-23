package com.example.budgettrip;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLHelper extends SQLiteOpenHelper{
	
	public static final String TABLE_ITEMS = "budgetItems" ;
	public static final String ITEMS_IDS = "item_id" ;
	public static final String ITEMS_PLAN = "plan_id";
	public static final String ITEMS_NAME = "name";
	public static final String ITEMS_DESC = "desc";
	public static final String ITEMS_PRICE = "price";
	
	public static final String TABLE_PLANS = "plans" ;
	public static final String PLANS_ID = "plan_id" ;
	public static final String PLANS_NAME = "name" ;
	public static final String PLANS_BUDGET = "budget" ;
	
	
	private static final String DB_NAME = "plans.db";
	private static final int DB_VER = 1;
	
	private static final String ITEMS_TABLE_CREATE = " create table " 
			+ TABLE_ITEMS + "(" 
			+ ITEMS_IDS + " integer primary key autoincrement, " 
			+ ITEMS_PLAN + " integer, "
			+ ITEMS_NAME + " text, "
			+ ITEMS_DESC + " text, "
			+ ITEMS_PRICE + " text); ";
	
	private static final String PLANS_TABLE_CREATE = " create table " 
			+ TABLE_PLANS + "(" 
			+ PLANS_ID + " integer primary key autoincrement, " 
			+ PLANS_NAME + " text unique, "
			+ PLANS_BUDGET + " text); ";
	

	public MySQLHelper(Context context) {
		super(context, DB_NAME, null, DB_VER);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(ITEMS_TABLE_CREATE);
		database.execSQL(PLANS_TABLE_CREATE);
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(MySQLHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANS);
	    onCreate(db);	
	}
	
	
	

}
