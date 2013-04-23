package com.example.budgettrip;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

public class MyService extends IntentService {
	
	public MyService() {
		super("MyIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		File log = new File(Environment.getExternalStorageDirectory(), "BudgetLog.txt");
		if (!log.exists() || log.isDirectory()) {
			try {
				log.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("budgetService", "Error creating log file");
			}
		}
		Log.e("budgetService", "Service Started");
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(log.getAbsolutePath(), log.exists()));
			out.write(new Date().toString());
			out.write(" tripBudget add button clicked\n");
			out.close();	
		}
		catch (IOException e) {
			Log.e("budgetService","Service failed to write to file.", e);
		}
		
	}
}
