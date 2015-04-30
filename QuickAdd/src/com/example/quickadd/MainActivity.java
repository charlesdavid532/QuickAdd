package com.example.quickadd;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.example.quickadd.GridData.DifficultyLevel;
import com.example.quickadd.LeaveGameDialogFragment.LeaveGameDialogListener;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends MenuOptionsActivity implements LeaveGameDialogListener {
	private GridData mGridData;
	private GridView mGridView;
	private ResultData mResultData;
	private ArrayList<ImageView> mHorImages;
	private ArrayList<ImageView> mVerImages;
	private ImageView mTotalImage;
	private UserTimer mUserTimer;
	private TextView mScoreLabelText;
	private Button mPlayAgainBtnView;
	private Button mDoneBtnView;
	private View mGridTableView;
	private RelativeLayout mainLayout;
	private DBHelper mDBHelper;
	private SharedPreferences mSharedPref;
	private int mLevel;
	
	private static final int BACK_BTN_OK = 0;
	private static final int CHART_BTN_OK = 1;
	private static final int OPTIONS_BTN_OK = 2;
	private static final int PLAY_BTN_OK = 3;
	
	private LeaveGameDialogFragment mFragment;
	
	
	private static final String TAG = "MAIN-ACTIVITY";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDBHelper = new DBHelper(this);
		//mDBHelper.insertScore(100,0);
		//mDBHelper.insertScore(400,1);
		//mDBHelper.insertScore(700,2);
		/*
		mDBHelper.insertRowInTable(27, 4, 2015, 200,0);
		mDBHelper.insertRowInTable(27, 4, 2015, 100,1);
		mDBHelper.insertRowInTable(27, 4, 2015, 900,2);
		mDBHelper.insertRowInTable(28, 4, 2015, 300,1);
		mDBHelper.insertRowInTable(29, 4, 2015, 800,2);
		mDBHelper.insertRowInTable(30, 4, 2015, 400,0);
		*/
		mainLayout = (RelativeLayout) findViewById(R.id.linnnnlayout);
		createGridDataObj();
		mGridView = new GridView(mGridData, getApplicationContext());
		mGridTableView = mGridView.constructTable();
		mainLayout.addView(mGridTableView);
		
		mGridView.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
		
		// Binding keyevents on the edittexts
		bindEvents();
		// initializing the tick and cross images
		initializeFeedbackImages();
		
		// initialize the timer
		initializeTimer();
		
		// initialize the score label and the play again button
		initializeFooter();
		
		mDoneBtnView = (Button) findViewById(R.id.done_button);
		mDoneBtnView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onDoneBtnClick();
			}
		});
		
		// TODO: Remove this later
				initializeChartBtn();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		//return true;
		super.onCreateOptionsMenu(menu);
		menu.add(0,Menu.FIRST+2,Menu.NONE,R.string.title_chart);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == Menu.FIRST+2) {
			onChartBtnClicked();
			return true;
		} else if (id == R.id.options) {
			onOptionsBntClick();
			return true;
		} else if (id == R.id.play) {
			onPlayAgainBtnClick();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void createGridDataObj() {
		mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		mLevel =mSharedPref.getInt("difficultyLevel", 0); 
		mGridData = new GridData(5, getDifficultyFromNumber(mLevel));
	}
	
	private DifficultyLevel getDifficultyFromNumber(int num) {
		switch(num) {
		case 0:
			return DifficultyLevel.EASY;
		case 1:
			return DifficultyLevel.MEDIUM;
		case 2:
			return DifficultyLevel.NASTY;
		default:
			return DifficultyLevel.EASY;
			
		}
	}
	
	private void bindEvents() {
		Boolean isColWiseAdd = mSharedPref.getBoolean("isColWiseAdd", false);
		if (isColWiseAdd) {
			Resources r = getResources();
			String name = getPackageName();
			
			for (int i = 0; i < mGridData.getGridSize(); i++) {
				final EditText temp = (EditText) findViewById(r.getIdentifier(
						"row_" + i, "id", name));
				final EditText temp1 = (EditText) findViewById(r.getIdentifier(
						"column_" + i, "id", name));
				temp.addTextChangedListener(new CustomTextWatcher(temp));
				temp1.addTextChangedListener(new CustomTextWatcher(temp));
			}
			final EditText temp2 = (EditText) findViewById(R.id.column_5);
			temp2.addTextChangedListener(new CustomTextWatcher(temp2));
		}
	}

	
	private void onDoneBtnClick() {
		// Stop the timer
		mUserTimer.stop();
		Log.i(TAG,"time taken:"+mUserTimer.getTimeInSeconds());
		Resources r = getResources();
		String name = getPackageName();
		int[] userRowInputs = new int[mGridData.getGridSize()];
		int[] userColumnInputs = new int[mGridData.getGridSize()];
		for (int i=0; i < userRowInputs.length; i++) {
			EditText temp = (EditText)findViewById(r.getIdentifier("row_" + i, "id", name));
			EditText temp1 = (EditText)findViewById(r.getIdentifier("column_" + i, "id", name));
			userRowInputs[i] = Integer.parseInt(cleanEmptyInput(temp.getText().toString()));
			userColumnInputs[i] = Integer.parseInt(cleanEmptyInput(temp1.getText().toString()));
		}
		EditText temp2 = (EditText)findViewById(R.id.column_5);
		UserInputData mUserInputData = new UserInputData(userRowInputs, 
				userColumnInputs, Integer.parseInt(cleanEmptyInput(temp2.getText().toString())), mUserTimer.getTimeInSeconds());
		
		mResultData = new ResultData(mGridData, mUserInputData);
		
		Log.i(TAG, "rows correct:"+ mResultData.getRowsCorrect());
		Log.i(TAG, "columns correct:"+ mResultData.getColumnsCorrect());
		Log.i(TAG, "total score:"+ mResultData.getScore());
		
		setCorrectFeedbackImages();
		showFooter();
	}
	
	public void onExitActivity() {
		int[] userRowInputs = new int[mGridData.getGridSize()];
		int[] userColumnInputs = new int[mGridData.getGridSize()];
		for (int i=0; i < mGridData.getGridSize(); i++) {
			userRowInputs[i] = 0;
			userColumnInputs[i] = 0;
		}
		UserInputData mUserInputData = new UserInputData(userRowInputs, 
				userColumnInputs, 0, mUserTimer.getTimeInSeconds());
		
		mResultData = new ResultData(mGridData, mUserInputData);
		insertScoreInDB();
	}
	
	@SuppressLint("NewApi")
	private String cleanEmptyInput(String text) {
		if (text == "" || text == null || text.equals("") || text.length() == 0) {
			return "0";
		} else {
			return text;
		}
	}
	
	private void setTextBoxToDefault() {
		Resources r = getResources();
		String name = getPackageName();
		for (int i=0; i < mGridData.getGridSize(); i++) {
			EditText temp = (EditText)findViewById(r.getIdentifier("row_" + i, "id", name));
			temp.setText("");
			EditText temp1 = (EditText)findViewById(r.getIdentifier("column_" + i, "id", name));
			temp1.setText("");
		}
		EditText temp2 = (EditText)findViewById(R.id.column_5);
		temp2.setText("");
	}
	
	private void initializeFeedbackImages() {
		ImageView testImage;
		ImageView testImage1;
		Resources r = getResources();
		String name = getPackageName();
		mHorImages = new ArrayList<ImageView>();
		mVerImages = new ArrayList<ImageView>();
		
		for (int i=0; i < mGridData.getGridSize(); i++) {
			testImage = (ImageView)findViewById(r.getIdentifier("hor_image_" + i, "id", name));
			testImage.setVisibility(View.INVISIBLE);
			mHorImages.add(testImage);
			testImage1 = (ImageView)findViewById(r.getIdentifier("ver_image_" + i, "id", name));
			testImage1.setVisibility(View.INVISIBLE);
			mVerImages.add(testImage1);
		}
		
		mTotalImage = (ImageView) findViewById(R.id.total_image);
		mTotalImage.setVisibility(View.INVISIBLE);
		
	}
	
	private void hideFeedBackImages() {
		for (int i=0; i < mGridData.getGridSize(); i++) {
			mHorImages.get(i).setVisibility(View.INVISIBLE);
			mVerImages.get(i).setVisibility(View.INVISIBLE);
		}
		
		mTotalImage.setVisibility(View.INVISIBLE);
	}
	
	private void setCorrectFeedbackImages () {
		for (int i=0; i < mGridData.getGridSize(); i++) {
			if (mResultData.getRowResultAt(i) == true) {
				mVerImages.get(i).setImageResource(R.drawable.greentick4);
			} else {
				mVerImages.get(i).setImageResource(R.drawable.cross);
			}
			
			mVerImages.get(i).setVisibility(View.VISIBLE);
			
			if (mResultData.getColumnResultAt(i) == true) {
				mHorImages.get(i).setImageResource(R.drawable.greentick4);
			} else {
				mHorImages.get(i).setImageResource(R.drawable.cross);
			}
			
			mHorImages.get(i).setVisibility(View.VISIBLE);
		}
		
		if (mResultData.isTotalCorrect() == true) {
			mTotalImage.setImageResource(R.drawable.greentick4);
		} else {
			mTotalImage.setImageResource(R.drawable.cross);
		}
		
		mTotalImage.setVisibility(View.VISIBLE);
	}
	
	private void initializeTimer() {
		mUserTimer = new UserTimer(this);
		mUserTimer.start();
	}
	
	private void initializeFooter() {
		initializeScoreLabel();
		initializePlayAgainBtn();	
		
	}
	
	private void initializeChartBtn() {
		Button chartBtn = (Button) findViewById(R.id.chart_button);
		
		chartBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onChartBtnClicked();
			}
		});
	}
	
	private void onChartBtnClicked() {
		checkAndDisplayWarningDialog(CHART_BTN_OK);
		
	}
	
	private void hideFooter() {
		showDoneBtn();
		hideScoreLabel();
		hidePlayAgainBtn();
	}
	
	private void showFooter() {
		hideDoneBtn();
		//setScoreLabel(mResultData.getScore());
		showScoreDialog();
		showPlayAgainBtn();
		insertScoreInDB();
	}
	
	public void insertScoreInDB() {
		mDBHelper.insertScore(mResultData.getScore(),mLevel);
	}
	
	private void showScoreDialog() {
		YourScoreDialogFragment fragment = new YourScoreDialogFragment(mResultData.getScore());
		fragment.show(getSupportFragmentManager(), "score");
	}
	
	private void onOptionsBntClick() {
		checkAndDisplayWarningDialog(OPTIONS_BTN_OK);
	}
	private void initializePlayAgainBtn() {
		mPlayAgainBtnView = (Button) findViewById(R.id.play_again_button);
		mPlayAgainBtnView.setVisibility(View.INVISIBLE);
		mPlayAgainBtnView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onPlayAgainBtnClick();
			}
		});
	}
	
	private void onPlayAgainBtnClick() {
		checkAndDisplayWarningDialog(PLAY_BTN_OK);
	}
	
	private void onPlayAgainOkBtnClick() {
		mGridData.createNewGrid(DifficultyLevel.EASY);
		mainLayout.removeView(mGridTableView);
		mGridTableView = mGridView.constructTable();
		mainLayout.addView(mGridTableView);
		
		hideFeedBackImages();
		hideFooter();
		setTextBoxToDefault();
		mUserTimer.start();
	}
	
	private void hidePlayAgainBtn() {
		mPlayAgainBtnView.setVisibility(View.INVISIBLE);
	}
	
	private void showPlayAgainBtn() {
		mPlayAgainBtnView.setVisibility(View.VISIBLE);
	}
	
	private void initializeScoreLabel() {
		mScoreLabelText = (TextView) findViewById(R.id.score_label);
		mScoreLabelText.setVisibility(View.INVISIBLE);
	}
	
	private void hideScoreLabel() {
		mScoreLabelText.setVisibility(View.INVISIBLE);
	}
	
	private void showScoreLabel() {
		mScoreLabelText.setVisibility(View.VISIBLE);
	}
	
	private void setScoreLabel(int score){
		showScoreLabel();
		mScoreLabelText.setText(this.getString(R.string.score_text) + String.valueOf(score));
	}
	
	private void hideDoneBtn() {
		mDoneBtnView.setVisibility(View.INVISIBLE);
	}
	
	private void showDoneBtn() {
		mDoneBtnView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		onExitActivity();
		finish();
		handleNavigation();
		
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBackPressed() {
		checkAndDisplayWarningDialog(BACK_BTN_OK);
	}
	
	private void checkAndDisplayWarningDialog(int id) {
		if (mUserTimer.getIsTimerRunning()) {
			mFragment = new LeaveGameDialogFragment(id);
			mFragment.show(getSupportFragmentManager(), "EXIT DIALOG");
		} else {
			handleNavigation(id);
		}
	}
	
	private void handleNavigation(int id) {
		decideNavigation(id);
	}
	
	private void handleNavigation() {
		decideNavigation(mFragment.getBtnId());
	}
	
	private void decideNavigation(int id) {
		Intent intent;
		switch (id) {
		case BACK_BTN_OK:
			break;
		case CHART_BTN_OK:
			intent = new Intent(getApplicationContext(), ChartActivity.class);
			startActivity(intent);
			break;
		case OPTIONS_BTN_OK:
			intent = new Intent(getApplicationContext(), OptionsActivity.class);
			startActivity(intent);
			break;
		case PLAY_BTN_OK:
			onPlayAgainOkBtnClick();
			break;
		}
	}

}
