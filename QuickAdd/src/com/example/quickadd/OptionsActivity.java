package com.example.quickadd;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemSelectedListener;

public class OptionsActivity extends Activity{
	private ToggleButton mColWiseAddBtn;
	private SharedPreferences mSharedPref;
	private SharedPreferences.Editor mEditor;
	private Spinner mDifficultySpinner;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
		
		initializeColumnWiseAdditionBtn();
		mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		mEditor = mSharedPref.edit();
		Boolean isColWiseAdd = mSharedPref.getBoolean("isColWiseAdd",false);
		mColWiseAddBtn.setChecked(isColWiseAdd);
		createDifficultySpinner();
	}
	
	private void initializeColumnWiseAdditionBtn() {
		mColWiseAddBtn = (ToggleButton) findViewById(R.id.column_wise_additions);
		mColWiseAddBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onColWiseAddBtnClick(v);
			}
		});
	}
	
	private void onColWiseAddBtnClick(View view) {
		// Is the toggle on?
	    boolean on = ((ToggleButton) view).isChecked();
	    mEditor.putBoolean("isColWiseAdd", on);
	    mEditor.commit();
	    /*
	    if (on) {
	        // Enable vibrate
	    } else {
	        // Disable vibrate
	    }
	    */
	}
	
	private void createDifficultySpinner() {
		ArrayAdapter<CharSequence> adapter = null;
		mDifficultySpinner = (Spinner) findViewById(R.id.difficultySpinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		adapter = ArrayAdapter.createFromResource(getApplicationContext(),
		        R.array.difficulty_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		mDifficultySpinner.setAdapter(adapter);
		int level = mSharedPref.getInt("difficultyLevel",0);
		mDifficultySpinner.setSelection(level);
		mDifficultySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, 
		            int pos, long id) {
		        // An item was selected. You can retrieve the selected item using
		        // parent.getItemAtPosition(pos)
				mEditor.putInt("difficultyLevel", pos);
				mEditor.commit();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		return super.onOptionsItemSelected(item);
	}
}
