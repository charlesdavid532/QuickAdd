package com.example.quickadd;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.BasicStroke;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;














import com.example.quickadd.ChangeDateDialogFragment.ChangeDateDialogListener;
import com.example.quickadd.GridData.DifficultyLevel;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class ChartActivity extends MenuOptionsActivity implements ChangeDateDialogListener{
	private GraphicalView mChart;
	private String[] mMonths = new String[] { "Jan", "Feb", "Mar", "Apr", "May",
			"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	private Spinner mMonthSpinner;
	private DBHelper mDBHelper;
	private XYSeries easySeries;
	private XYSeries mediumSeries;
	private XYSeries nastySeries;
	private XYMultipleSeriesDataset dataset;
	private XYMultipleSeriesRenderer multiRenderer;
	private XYSeriesRenderer easyRenderer;
	private XYSeriesRenderer mediumRenderer;
	private XYSeriesRenderer nastyRenderer;
	private int mDay = 30;
	private int mMonth = 5;
	private int mYear = 2013;
	static final int DATE_DIALOG_ID = 1;
	private Button mChangeDateBtnView;
	private final String TAG = "CHartActivity";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chart);
		// Create the month and year spinners (Replace with dialog later)
		//createSpinners();
		final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
		// Create the change date button
		createChangeDateButton();
		// Draw the Income vs Expense Chart
		//openChart();
		createScoreChart();
		
	}
	
	private void createSpinners() {
		createMonthSpinner();
	}
	
	private void createMonthSpinner() {
		ArrayAdapter<CharSequence> adapter = null;
		mMonthSpinner = (Spinner) findViewById(R.id.MonthSpinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		adapter = ArrayAdapter.createFromResource(getApplicationContext(),
		        R.array.month_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		mMonthSpinner.setAdapter(adapter);
		mMonthSpinner.setSelection(3);
		mMonthSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, 
		            int pos, long id) {
		        // An item was selected. You can retrieve the selected item using
		        // parent.getItemAtPosition(pos)
				Log.i(TAG,"month:"+pos);
				createScoreSeries(pos, 2015,0);
				if (multiRenderer != null){
					Log.i(TAG,"Wow it did reach here: but why!!");
					multiRenderer.setXTitle("May 2014");
				}
				mChart.repaint();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void createScoreSeries(int month,int year,int level) {
		ChartData data = mDBHelper.getScoresAndDates(month, year,level);
		ArrayList<Integer> scores = data.getScores();
		ArrayList<Integer> dates = data.getDates();
		//scoreSeries.clear();
		XYSeries series = getSeriesFromLevel(level); 
		series.clear();
		if (dates != null) {
			for (int i=0; i < dates.size(); i++) {
				Log.i(TAG,"i:"+i+"date:"+dates.get(i)+"score:"+ scores.get(i));
				//scoreSeries.add(dates.get(i), scores.get(i));
				series.add(dates.get(i), scores.get(i));
			}
		}
	}
	
	private XYSeries getSeriesFromLevel(int level) {
		switch (level) {
		case 0:
			return easySeries;
		case 1:
			return mediumSeries;
		case 2:
			return nastySeries;
			default:
				return easySeries;
		}
	}
	private void initializeDataSet() {
		easySeries = new XYSeries("EasyScores");
		mediumSeries = new XYSeries("MediumScores");
		nastySeries = new XYSeries("NastyScores");
	}
	private void createDataSet() {
		
		createScoreSeries(mMonth, mYear,0); // TODO: Replace this with current month and year
		Log.i(TAG,"no problem with the loop");
		// Creating a dataset to hold each series
		dataset = new XYMultipleSeriesDataset();
		// Adding Income Series to the dataset
		dataset.addSeries(easySeries);
		
		
		createScoreSeries(mMonth, mYear,1); // TODO: Replace this with current month and year
		dataset.addSeries(mediumSeries);
		
		
		createScoreSeries(mMonth, mYear,2); // TODO: Replace this with current month and year
		dataset.addSeries(nastySeries);
	}
	
	private void createRenderers() {
		createEasyRenderer();
		createMediumRenderer();
		createNastyRenderer();
	}
	
	private void createEasyRenderer() {
		// Creating XYSeriesRenderer to customize scoreSeries
		easyRenderer = new XYSeriesRenderer();
		easyRenderer.setColor(Color.CYAN); // color of the graph set to cyan
		easyRenderer.setFillPoints(true);
		easyRenderer.setLineWidth(2f);
		easyRenderer.setDisplayChartValues(true);
		// setting chart value distance
		easyRenderer.setDisplayChartValuesDistance(10);
		// setting line graph point style to circle
		easyRenderer.setPointStyle(PointStyle.CIRCLE);
		// setting stroke of the line chart to solid
		easyRenderer.setStroke(BasicStroke.SOLID);
	}
	
	private void createMediumRenderer() {
		// Creating XYSeriesRenderer to customize scoreSeries
		mediumRenderer = new XYSeriesRenderer();
		mediumRenderer.setColor(Color.GREEN); // color of the graph set to cyan
		mediumRenderer.setFillPoints(true);
		mediumRenderer.setLineWidth(2f);
		mediumRenderer.setDisplayChartValues(true);
		// setting chart value distance
		mediumRenderer.setDisplayChartValuesDistance(10);
		// setting line graph point style to circle
		mediumRenderer.setPointStyle(PointStyle.CIRCLE);
		// setting stroke of the line chart to solid
		mediumRenderer.setStroke(BasicStroke.SOLID);
	}
	
	private void createNastyRenderer() {
		// Creating XYSeriesRenderer to customize scoreSeries
		nastyRenderer = new XYSeriesRenderer();
		nastyRenderer.setColor(Color.RED); // color of the graph set to cyan
		nastyRenderer.setFillPoints(true);
		nastyRenderer.setLineWidth(2f);
		nastyRenderer.setDisplayChartValues(true);
		// setting chart value distance
		nastyRenderer.setDisplayChartValuesDistance(10);
		// setting line graph point style to circle
		nastyRenderer.setPointStyle(PointStyle.CIRCLE);
		// setting stroke of the line chart to solid
		nastyRenderer.setStroke(BasicStroke.SOLID);
	}
	
	private void createScoreChart() {
		mDBHelper = new DBHelper(getApplicationContext());
		
		initializeDataSet();
		createDataSet();
				
		createRenderers();
		
		
		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
		multiRenderer.setChartTitle("Scores Chart");
		multiRenderer.setXTitle(mMonths[mMonth]+ " " + String.valueOf(mYear));
		multiRenderer.setYTitle("Scores");
		
		/***
		 * Customizing graphs
		 */
		// setting text size of the title
		multiRenderer.setChartTitleTextSize(18);
		// setting text size of the axis title
		multiRenderer.setAxisTitleTextSize(16);
		// setting text size of the graph lable
		multiRenderer.setLabelsTextSize(14);
		// setting zoom buttons visiblity
		multiRenderer.setZoomButtonsVisible(false);
		// setting pan enablity which uses graph to move on both axis
		multiRenderer.setPanEnabled(true, false);
		// setting click false on graph
		multiRenderer.setClickEnabled(false);
		// setting zoom to false on both axis
		multiRenderer.setZoomEnabled(false, false);
		// setting lines to display on y axis
		multiRenderer.setShowGridY(false);
		// setting lines to display on x axis
		multiRenderer.setShowGridX(false);
		// setting legend to fit the screen size
		multiRenderer.setFitLegend(true);
		// setting displaying line on grid
		multiRenderer.setShowGrid(true);
		// setting zoom to false
		multiRenderer.setZoomEnabled(false);
		// setting external zoom functions to false
		multiRenderer.setExternalZoomEnabled(false);
		// setting displaying lines on graph to be formatted(like using
		// graphics)
		multiRenderer.setAntialiasing(true);
		// setting to in scroll to false
		multiRenderer.setInScroll(false);
		// setting to set legend height of the graph
		multiRenderer.setLegendHeight(30);
		// setting x axis label align
		multiRenderer.setXLabelsAlign(Align.CENTER);
		// setting y axis label to align
		multiRenderer.setYLabelsAlign(Align.LEFT);
		// setting text style
		multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
		// setting no of values to display in y axis
		multiRenderer.setYLabels(5);
		// setting y axis max value, Since i'm using static values inside the
		// graph so i'm setting y max value to 4000.
		// if you use dynamic values then get the max y value and set here
		multiRenderer.setYAxisMin(0);
		multiRenderer.setYAxisMax(1000);
		// setting used to move the graph on xaxiz to .5 to the right
		multiRenderer.setXAxisMin(1);
		// setting used to move the graph on xaxiz to .5 to the right
		multiRenderer.setXAxisMax(31);
		// setting bar size or space between two bars
		// multiRenderer.setBarSpacing(0.5);
		// Setting background color of the graph to transparent
		multiRenderer.setBackgroundColor(Color.TRANSPARENT);
		// Setting margin color of the graph to transparent
		multiRenderer.setMarginsColor(getResources().getColor(
				R.color.transparent_background));
		multiRenderer.setApplyBackgroundColor(true);
		multiRenderer.setScale(2f);
		// setting x axis point size
		multiRenderer.setPointSize(4f);
		// setting the margin size for the graph in the order top, left, bottom,
		// right
		multiRenderer.setMargins(new int[] { 30, 30, 30, 30 });
		multiRenderer.addSeriesRenderer(easyRenderer);
		multiRenderer.addSeriesRenderer(mediumRenderer);
		multiRenderer.addSeriesRenderer(nastyRenderer);
		
		// this part is used to display graph on the xml
		LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart);
		// remove any views before u paint the chart
		chartContainer.removeAllViews();
		// drawing bar chart
		mChart = ChartFactory.getLineChartView(ChartActivity.this, dataset,
				multiRenderer);
		// adding the view to the linearlayout
		chartContainer.addView(mChart);

	}

	private void createChangeDateButton() {
		mChangeDateBtnView = (Button) findViewById(R.id.change_date_button);
		mChangeDateBtnView.setOnClickListener(new OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//showDialog(DATE_DIALOG_ID);
				DialogFragment fragment = new ChangeDateDialogFragment(mMonth, mYear);
				fragment.show(getSupportFragmentManager(), "Change Date");
			}
		}); 
	}
	
	
	
	@Override
	public void onDialogPositiveClick(DialogFragment dialog, DateModel data) {
		// TODO Auto-generated method stub
		mMonth = data.getMonth();
		mYear = data.getYear();
		//createScoreSeries(mMonth, mYear,0);
		createDataSet();
		if (multiRenderer != null){
			Log.i(TAG,"Wow it did reach here: but why!!");
			multiRenderer.setXTitle(mMonths[mMonth]+ " " + String.valueOf(mYear));
		}
		mChart.repaint();
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}
}
