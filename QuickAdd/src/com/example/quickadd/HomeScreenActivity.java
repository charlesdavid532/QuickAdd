package com.example.quickadd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeScreenActivity extends Activity{
	private Button mProgressBtnView;
	private Button mOptionsBtnView;
	private Button mPlayBtnView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		
		initializeProgressBtn();
		initializeOptionsBtn();
		initializePlayBtn();
		
	}
	
	private void initializeProgressBtn() {
		mProgressBtnView = (Button) findViewById(R.id.view_progress_button);
		mProgressBtnView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onProgressBtnClick();
			}
		});
	}
	
	private void onProgressBtnClick() {
		Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
		startActivity(intent);
	}
	
	private void initializeOptionsBtn() {
		mOptionsBtnView = (Button) findViewById(R.id.options_button);
		mOptionsBtnView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onOptionsBtnClick();
			}
		});
	}
	
	private void onOptionsBtnClick() {
		Intent intent = new Intent(getApplicationContext(), OptionsActivity.class);
		startActivity(intent);
	}
	
	private void initializePlayBtn() {
		mPlayBtnView = (Button) findViewById(R.id.play_game_button);
		mPlayBtnView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onPlayBtnClick();
			}
		});
	}
	
	private void onPlayBtnClick() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
	}
	
	
	
	
}
