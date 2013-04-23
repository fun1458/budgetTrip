package com.example.budgettrip;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyListAdapter extends BaseAdapter{
	
	private ArrayList<BudgetItems> budgetItems;
	private LayoutInflater mInflater;
	
	public MyListAdapter(Context context, int resourceId, ArrayList<BudgetItems> items){
		this.budgetItems = items;
		this.mInflater = LayoutInflater.from(context);
	}

	private class ViewHolder {
		TextView txtTitle;
		//TextView txtDesc;
		TextView price;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.custom_row_view, null);
			holder = new ViewHolder();
			holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
			//holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.txtTitle.setText(budgetItems.get(position).getName());
		//holder.txtDesc.setText(budgetItems.get(position).getDesc());
		holder.price.setText( "$" + budgetItems.get(position).getPrice());
		
		return convertView;
		
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return budgetItems.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return budgetItems.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}
