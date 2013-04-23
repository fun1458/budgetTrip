package com.example.budgettrip;

import java.util.ArrayList;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PlansActivity extends SherlockListActivity implements OnNavigationListener {
	
	private ArrayList<String> list = new ArrayList<String>();
	private ArrayList<Long> ids = new ArrayList<Long>();
	private ListView listView;
	private ArrayList<Plans> planlist;
	private ArrayList<BudgetItems> itemslist;
	private long lastPlan;
	private ArrayAdapter<String> sAdapter;
	private PlansDataSource datasrc;
	private MyListAdapter mAdapter;
	int requestCode;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		datasrc = new PlansDataSource(this);
		datasrc.open();
		
		planlist = datasrc.fetchPlans();
		if (planlist.size() <= 0) {
			setContentView(R.layout.budget_list_empty);
		}
		else {
			setContentView(R.layout.budget_list);
			for (Plans p : planlist) {
				ids.add(p.getPlanId());
				list.add(p.getName());
			}
			com.actionbarsherlock.app.ActionBar actionBar = getSupportActionBar();
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			sAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
	        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
	        getSupportActionBar().setListNavigationCallbacks(sAdapter, this);
	        registerForContextMenu(findViewById(android.R.id.list));
	        if (savedInstanceState != null) {
	        	getSupportActionBar().setSelectedNavigationItem(savedInstanceState.getInt("lastSelected"));
	        }
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
	    getSupportMenuInflater().inflate(R.menu.activity_main, menu);
	    return true;
	}
	
	@Override
	public final boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.add_plan:
	    	//Intent serviceIntent = new Intent (this, MyService.class);
	    	//startService(serviceIntent);
	    	addPlan((ListView) findViewById(android.R.id.list));
	    	return true;
	    case R.id.remove_plan:
	    	int selectedIndex = getSupportActionBar().getSelectedNavigationIndex();
	    	datasrc.deletePlan(ids.get(selectedIndex));
	    	sAdapter.remove(list.get(selectedIndex));
	    	ids.remove(selectedIndex);
	    	planlist.remove(selectedIndex);
	    	if (planlist.size() == 0) {
	    		setContentView(R.layout.budget_list_empty);
	    		return true;
	        }
	    	sAdapter.notifyDataSetChanged();
	    	getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
	        getSupportActionBar().setListNavigationCallbacks(sAdapter, this);
	       
	    	return true;
	    default:
	    	return super.onOptionsItemSelected(item);
	    }
	}

	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		Toast.makeText(this, "size" + ids.size() + "selected " + itemPosition, Toast.LENGTH_SHORT).show();
		listView = (ListView) findViewById(android.R.id.list);
		lastPlan = ids.get(itemPosition);
		itemslist = datasrc.fetchItems(lastPlan);
		mAdapter = new MyListAdapter(this, R.layout.custom_row_view, itemslist);
		listView.setAdapter(mAdapter);
		TextView totalExpenses = (TextView) findViewById(R.id.expense);
		TextView budget = (TextView) findViewById(R.id.limit);
		TextView leftOver = (TextView) findViewById(R.id.money_left);
		double expenses = calcExpenses();
		double budgetLimit = planlist.get(itemPosition).getLimit();
		double leftover = budgetLimit - expenses;
		totalExpenses.setText("Total Expense: $" + expenses);
		budget.setText("Budget: $" + budgetLimit);
		if (leftover > 0) {
			leftOver.setText("Still Have: $" + leftover);
		} else {
			leftOver.setText("Over Budget: $" + Math.abs(leftover));
		}
		return true;
	}
	
	public void addPlan(View v) {
		LayoutInflater inflater = LayoutInflater.from(this);
    	View addView = inflater.inflate(R.layout.add_plan, null);
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    	alertDialogBuilder.setView(addView);
    	final EditText plan_name = (EditText) addView.findViewById(R.id.editText1);
    	final EditText plan_budget = (EditText) addView.findViewById(R.id.editText3);
    	plan_budget.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(12,2)});
    	alertDialogBuilder.setPositiveButton(R.string.add, 
    			new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog, int id) {
    					if (plan_name.getText().toString().equals("") || plan_budget.getText().toString().equals("")) {
    						Toast.makeText(getApplicationContext(), "Please fill in all the information", Toast.LENGTH_LONG).show();
    					} else {
	    					Plans newPlan = datasrc.createPlan(plan_name.getText().toString(), plan_budget.getText().toString()) ;
	    					if (newPlan != null) {
		    					planlist.add(newPlan);
		    					ids.add(newPlan.getPlanId());		
	    						if (planlist.size() == 1) {
	    							changeContentView();
	    					        sAdapter.add(newPlan.getName());
	    					        sAdapter.notifyDataSetChanged();
	    						} else {
	    							sAdapter.add(newPlan.getName());
	    							sAdapter.notifyDataSetChanged();
			    					getSupportActionBar().setSelectedNavigationItem(sAdapter.getCount());
	    						}
	    					}
    					}
    				}
    			}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog, int id) {
    					dialog.cancel();
    				}
    			});
    	
    	AlertDialog alertDialog = alertDialogBuilder.create();
    	alertDialog.show();
	}
	
	public void changeContentView() {
		setContentView(R.layout.budget_list);
		com.actionbarsherlock.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		sAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSupportActionBar().setListNavigationCallbacks(sAdapter, this);
        registerForContextMenu(findViewById(android.R.id.list));
	}
	
	public double calcExpenses() {
		double totalExpenses = 0;
		for (BudgetItems bi: itemslist) {
			totalExpenses += bi.getPrice();
		}
		return totalExpenses;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		menu.add("delete");
	}
	
	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		AdapterView.AdapterContextMenuInfo info;
        try {
            info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        } catch (ClassCastException e) {
            Log.e("", "bad menuInfo", e);
            return false;
        }
        long id = mAdapter.getItemId(info.position);
        datasrc.deleteItem((BudgetItems) mAdapter.getItem(info.position));
        itemslist.remove(info.position);
        mAdapter.notifyDataSetChanged();
        TextView totalExpenses = (TextView) findViewById(R.id.expense);
		TextView leftOver = (TextView) findViewById(R.id.money_left);
		double expenses = calcExpenses();
		double budgetLimit = planlist.get(getSupportActionBar().getSelectedNavigationIndex()).getLimit();
		double leftover = budgetLimit - expenses;
		totalExpenses.setText("Total Expense: $" + expenses);
		if (leftover > 0) {
			leftOver.setText("Still Have: $" + leftover);
		} else {
			leftOver.setText("You're $" + Math.abs(leftover) +"over your budget");
		}
	    return true;
	}
	
	public void addNewItem(View v) {
		datasrc.close();
		Intent myIntent = new Intent(PlansActivity.this, addItemActivity.class);
		myIntent.putExtra("planId", lastPlan);
		startActivityForResult(myIntent, requestCode);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == 1) {
			datasrc.open();
			Bundle extras = intent.getExtras();
			if (extras != null) {			
				listView = (ListView) findViewById(android.R.id.list);
				lastPlan = extras.getLong("planId");
				itemslist = datasrc.fetchItems(lastPlan);
				mAdapter = new MyListAdapter(this, R.layout.custom_row_view, itemslist);
				listView.setAdapter(mAdapter);
				TextView totalExpenses = (TextView) findViewById(R.id.expense);
				TextView budget = (TextView) findViewById(R.id.limit);
				TextView leftOver = (TextView) findViewById(R.id.money_left);
				double expenses = calcExpenses();
				double budgetLimit = planlist.get(getSupportActionBar().getSelectedNavigationIndex()).getLimit();
				double leftover = budgetLimit - expenses;
				totalExpenses.setText("Total Expense: $" + expenses);
				if (leftover > 0) {
					leftOver.setText("Still Have: $" + leftover);
				} else {
					leftOver.setText("You're $" + Math.abs(leftover) +"over your budget");
				}
			}
		}
	}

	@Override
	protected void onResume() {
		datasrc.open();
		super.onResume();
	}

	@Override
	protected void onStop() {
		datasrc.close();
		super.onStop();
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putInt("lastSelected", getSupportActionBar().getSelectedNavigationIndex());
	}

}
