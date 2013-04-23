package com.example.budgettrip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class addItemActivity extends Activity {
	
	private String item_name;
	private String item_desc;
	private String item_cost;
	private PlansDataSource datasrc;
	private long lastPlan;
	private EditText itemName;
	private EditText itemDesc;
	private EditText itemCost;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_item);
		datasrc = new PlansDataSource(this);
		datasrc.open();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			lastPlan = extras.getLong("planId");
		}
		itemName = (EditText) findViewById(R.id.item_name);
		itemDesc = (EditText) findViewById(R.id.item_desc);
		itemCost = (EditText) findViewById(R.id.item_cost);
		itemCost.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(12,2)});
		
	}
	
	public void addNow(View v) {
		if (!itemName.getText().toString().equals("") && !itemCost.getText().toString().equals("")) {
			BudgetItems newItem = datasrc.createItem(itemName.getText().toString(), itemDesc.getText().toString(), itemCost.getText().toString(), lastPlan) ;
			datasrc.close();
			Intent intent = new Intent();
			intent.putExtra("planId", newItem.getPlanId());
			setResult(1, intent);
			finish();
		}
		else {
			Toast.makeText(this, "Please don't leave the name or the cost fields empty", Toast.LENGTH_LONG).show();
		}
	}
	
	public void cancelNow(View v) {
		datasrc.close();
		finish();
	}
	
	@Override
	protected void onResume() {
		datasrc.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasrc.close();
		super.onPause();
	}
	
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  savedInstanceState.putString("itemName", item_name);
	  savedInstanceState.putString("itemDesc", item_desc);
	  savedInstanceState.putString("itemCost", item_cost);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onRestoreInstanceState(savedInstanceState);
	  item_name= savedInstanceState.getString("itemName");
	  item_desc= savedInstanceState.getString("itemDesc");
	  item_cost= savedInstanceState.getString("itemCost");  
	}

}
