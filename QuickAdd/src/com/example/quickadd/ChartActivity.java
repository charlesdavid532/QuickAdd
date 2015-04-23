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










import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.os.Bundle;
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

public class ChartActivity extends Activity {
	private GraphicalView mChart;
	private String[] mMonths = new String[] { "Jan", "Feb", "Mar", "Apr", "May",
			"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	private Spinner mMonthSpinner;
	private DBHelper mDBHelper;
	private XYSeries scoreSeries;
	private XYMultipleSeriesRenderer multiRenderer;
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
				createScoreSeries(pos, 2015);
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
	
	private void createScoreSeries(int month,int year) {
		ChartData data = mDBHelper.getScoresAndDates(month, year);
		ArrayList<Integer> scores = data.getScores();
		ArrayList<Integer> dates = data.getDates();
		scoreSeries.clear();
		if (dates != null) {
			for (int i=0; i < dates.size(); i++) {
				Log.i(TAG,"i:"+i+"date:"+dates.get(i)+"score:"+ scores.get(i));
				scoreSeries.add(dates.get(i), scores.get(i));
			}
		}
	}
	
	private void createScoreChart() {
		mDBHelper = new DBHelper(getApplicationContext());
		/*
		ChartData data = mDBHelper.getScoresAndDates(3, 2015);
		ArrayList<Integer> scores = data.getScores();
		ArrayList<Integer> dates = data.getDates();
		scoreSeries = new XYSeries("Scores");
		for (int i=0; i < dates.size(); i++) {
			Log.i(TAG,"i:"+i+"date:"+dates.get(i)+"score:"+ scores.get(i));
			scoreSeries.add(dates.get(i), scores.get(i));
		}
		*/
		scoreSeries = new XYSeries("Scores");
		createScoreSeries(3, 2015); // TODO: Replace this with current month and year
		Log.i(TAG,"no problem with the loop");
		// Creating a dataset to hold each series
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		// Adding Income Series to the dataset
		dataset.addSeries(scoreSeries);
				
		// Creating XYSeriesRenderer to customize scoreSeries
		XYSeriesRenderer scoreRenderer = new XYSeriesRenderer();
		scoreRenderer.setColor(Color.CYAN); // color of the graph set to cyan
		scoreRenderer.setFillPoints(true);
		scoreRenderer.setLineWidth(2f);
		scoreRenderer.setDisplayChartValues(true);
		// setting chart value distance
		scoreRenderer.setDisplayChartValuesDistance(10);
		// setting line graph point style to circle
		scoreRenderer.setPointStyle(PointStyle.CIRCLE);
		// setting stroke of the line chart to solid
		scoreRenderer.setStroke(BasicStroke.SOLID);
		
		
		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
		multiRenderer.setChartTitle("Scores Chart");
		multiRenderer.setXTitle("April 2014");
		multiRenderer.setYTitle("Scores");
		
		/***
		 * Customizing graphs
		 */
		// setting text size of the title
		multiRenderer.setChartTitleTextSize(14);
		// setting text size of the axis title
		multiRenderer.setAxisTitleTextSize(12);
		// setting text size of the graph lable
		multiRenderer.setLabelsTextSize(8);
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
		multiRenderer.addSeriesRenderer(scoreRenderer);
		
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
				showDialog(DATE_DIALOG_ID);
			}
		}); 
	}
	
	DatePickerDialog.OnDateSetListener mDateSetListner = new OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                int dayOfMonth) {

            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDate();
        }
    };
    
    
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
            /*
             * return new DatePickerDialog(this, mDateSetListner, mYear, mMonth,
             * mDay);
             */
            DatePickerDialog datePickerDialog = this.customDatePicker();
            return datePickerDialog;
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    protected void updateDate() {
        Log.i(TAG,"month selected:"+mMonth);
        Log.i(TAG,"year selected:"+mYear);
        createScoreSeries(mMonth, mYear);
		if (multiRenderer != null){
			Log.i(TAG,"Wow it did reach here: but why!!");
			multiRenderer.setXTitle(mMonths[mMonth]+ " " + String.valueOf(mYear));
		}
		mChart.repaint();
        
    }

    private DatePickerDialog customDatePicker() {
        DatePickerDialog dpd = new DatePickerDialog(this, mDateSetListner,
                mYear, mMonth, mDay) {
        	@Override
        	public void onDateChanged(DatePicker view, int year,
        			int month, int day) {
        		// TODO Auto-generated method stub
        		super.onDateChanged(view, year, month, day);
        		setTitle(mMonths[month] + " " + String.valueOf(year));
        		try {
                    Field[] datePickerDialogFields = this.getClass().getDeclaredFields();
                    for (Field datePickerDialogField : datePickerDialogFields) {
                        if (datePickerDialogField.getName().equals("mDatePicker")) {
                            datePickerDialogField.setAccessible(true);
                            DatePicker datePicker = (DatePicker) datePickerDialogField
                                    .get(this);
                            Field datePickerFields[] = datePickerDialogField.getType()
                                    .getDeclaredFields();
                            for (Field datePickerField : datePickerFields) {
                                if ("mDayPicker".equals(datePickerField.getName())
                                        || "mDaySpinner".equals(datePickerField
                                                .getName())) {
                                    datePickerField.setAccessible(true);
                                    Object dayPicker = new Object();
                                    dayPicker = datePickerField.get(datePicker);
                                    ((View) dayPicker).setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                	Log.i(TAG,"Exception encountered"+ex);
                }
        	}
        };
        try {
            Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField
                            .get(dpd);
                    Field datePickerFields[] = datePickerDialogField.getType()
                            .getDeclaredFields();
                    for (Field datePickerField : datePickerFields) {
                        if ("mDayPicker".equals(datePickerField.getName())
                                || "mDaySpinner".equals(datePickerField
                                        .getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = new Object();
                            dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception ex) {
        	Log.i(TAG,"Exception encountered::::::"+ex);
        }
        return dpd;
    }
}
