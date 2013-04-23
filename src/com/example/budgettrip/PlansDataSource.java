package com.example.budgettrip;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PlansDataSource {

	private SQLiteDatabase database;
	private MySQLHelper dbHelper;
	private String[] itemsAllCol = {
			MySQLHelper.ITEMS_IDS,
			MySQLHelper.ITEMS_PLAN,
			MySQLHelper.ITEMS_NAME,
			MySQLHelper.ITEMS_DESC,
			MySQLHelper.ITEMS_PRICE
			};
	
	private String[] plansAllCol = {
			MySQLHelper.PLANS_ID,
			MySQLHelper.PLANS_NAME,
			MySQLHelper.PLANS_BUDGET
			};
	
	public PlansDataSource(Context context) {
		dbHelper = new MySQLHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public Plans createPlan(String name, String budget) {
		ContentValues vals = new ContentValues();
		vals.put(MySQLHelper.PLANS_NAME, name);
		vals.put(MySQLHelper.PLANS_BUDGET, budget);
		try {
			long insertId = database.insert(MySQLHelper.TABLE_PLANS, null, vals);
			Cursor cursor = database.query(MySQLHelper.TABLE_PLANS, plansAllCol, MySQLHelper.PLANS_ID + "=" + insertId, null,
					null, null, null);
			cursor.moveToFirst();
			Plans newPlan = CursorToPlans(cursor);
			cursor.close();
			return newPlan;
		} catch (SQLException e) {
			return null;
		}
		
	}
	
	public void deletePlan(long planid) {
		database.delete(MySQLHelper.TABLE_PLANS, MySQLHelper.PLANS_ID + "=" + planid, null);
		database.delete(MySQLHelper.TABLE_ITEMS, MySQLHelper.ITEMS_PLAN + "=" + planid, null);
	}
	
	public BudgetItems createItem(String name, String desc, String cost, long planId) {
		ContentValues vals = new ContentValues();
		vals.put(MySQLHelper.ITEMS_NAME, name);
		vals.put(MySQLHelper.ITEMS_DESC, desc);
		vals.put(MySQLHelper.ITEMS_PRICE, cost);
		vals.put(MySQLHelper.ITEMS_PLAN, planId);
		long insertId = database.insert(MySQLHelper.TABLE_ITEMS,  null, vals);
		Cursor cursor = database.query(MySQLHelper.TABLE_ITEMS, itemsAllCol, MySQLHelper.ITEMS_IDS + "=" + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		BudgetItems newItem = CursorToBudgetItems(cursor);
		cursor.close();
		return newItem;
	}
	
	public void deleteItem(BudgetItems item) {
		long itemId = item.getItemId();
		database.delete(MySQLHelper.TABLE_ITEMS, MySQLHelper.ITEMS_IDS + "=" + itemId, null);
	}
	
	public ArrayList<Plans> fetchPlans() {
		ArrayList<Plans> planList = new ArrayList<Plans>();
		Cursor cursor = database.query(MySQLHelper.TABLE_PLANS, plansAllCol, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Plans newPlan = CursorToPlans(cursor);
			planList.add(newPlan);
			cursor.moveToNext();
		}
		cursor.close();
		return planList;
	}
	
	public ArrayList<BudgetItems> fetchItems(long planid) {
		ArrayList<BudgetItems> budgetItems = new ArrayList<BudgetItems>();
		Cursor itemsCursor = database.query(MySQLHelper.TABLE_ITEMS, itemsAllCol, MySQLHelper.ITEMS_PLAN + "=" + planid, null,
				null, null, null);
		itemsCursor.moveToFirst();
		while(!itemsCursor.isAfterLast()) {
			BudgetItems newItem = CursorToBudgetItems(itemsCursor);
			budgetItems.add(newItem);
			itemsCursor.moveToNext();
		}
		itemsCursor.close();
		return budgetItems;
		
	}
	
	private Plans CursorToPlans(Cursor cursor) {
		Plans plan = new Plans(round2(Double.parseDouble(cursor.getString(2))), cursor.getString(1));
		plan.setPlanId(cursor.getLong(0));
		return plan;
	}
	
	private BudgetItems CursorToBudgetItems(Cursor cursor) {
		BudgetItems newItem = new BudgetItems(cursor.getLong(1), round2(Double.parseDouble(cursor.getString(4))),
				cursor.getString(2), cursor.getString(3));
		newItem.setItemId(cursor.getLong(0));
		return newItem;
	}
	
	public static double round2(double num) {
		double result = num * 100;
		result = Math.round(result);
		result = result / 100;
		return result;
	}
}
