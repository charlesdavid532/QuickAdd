package com.example.quickadd;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

public class ChangeDateDialogFragment extends DialogFragment{
	private int mYear;
	private int mMonth;
	public ChangeDateDialogFragment(int month,int year) {
		mYear = year;
		mMonth = month;
	}
	/* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface ChangeDateDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, DateModel data);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    
    // Use this instance of the interface to deliver action events
    ChangeDateDialogListener mListener;
    
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ChangeDateDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
	 @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
		        // Use the Builder class for convenient dialog construction
			 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			// Get the layout inflater
		    LayoutInflater inflater = getActivity().getLayoutInflater();
		    LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.date_dialog, null);
		    final NumberPicker monthPicker = (NumberPicker) layout.findViewById(R.id.monthPicker);
		    monthPicker.setMinValue(0);
		    monthPicker.setMaxValue(11);
		    
		    monthPicker.setValue(mMonth);
		    monthPicker.setDisplayedValues(getResources().getStringArray(R.array.month_array));
		    
		    
		    final NumberPicker yearPicker = (NumberPicker) layout.findViewById(R.id.yearPicker);
		    yearPicker.setMinValue(0);
		    yearPicker.setMaxValue(3000);
		    yearPicker.setValue(mYear);
		    
			 builder.setView(layout)
			 		.setTitle(R.string.set_date_string)
			 		.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							DateModel dateModel = new DateModel(monthPicker.getValue(),
									yearPicker.getValue());
							mListener.onDialogPositiveClick(ChangeDateDialogFragment.this, dateModel);
						}
					})
					.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							mListener.onDialogNegativeClick(ChangeDateDialogFragment.this);
						}
					});
			 return builder.create();
		 }
}
