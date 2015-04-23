package com.example.quickadd;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class GridView extends View{
	
	private GridData mGridData;
	private final Context mContext;
	private TableRow mTableRow;
	private TextView mCellText;
	
	private static final String TAG = "GRID-VIEW";
	
	public GridView(GridData gridData, Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		this.mGridData = gridData;
		this.mContext = context;
	}
	
	public GridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }
	
	public View constructTable() {
		final TableLayout tableLayout = (TableLayout)LayoutInflater.from(mContext).inflate(R.layout.grid, null).findViewById(R.id.grid_table);
		
		for (int i=0; i < mGridData.getGridSize(); i++) {
			mTableRow = new TableRow(mContext);
			
			for (int j=0; j < mGridData.getGridSize(); j++) {
				mCellText = new TextView(mContext);
				//Log.i(TAG, String.valueOf(mGridData.getGridNumber(i, j)));
				mCellText.setText(String.valueOf(mGridData.getGridNumber(i, j)));
				mCellText.setTextColor(Color.GRAY);
				mCellText.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
				mCellText.setTextSize(25);
				mCellText.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));
				mCellText.setPadding(5,5,15,5);
				mTableRow.addView(mCellText);  // Adding textView to tablerow.
			}
			
			tableLayout.addView(mTableRow);
		}
		
		return tableLayout;
		
	}
	
	
}
