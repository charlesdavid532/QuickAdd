package com.example.quickadd;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class YourScoreDialogFragment extends DialogFragment{
	private int score;
	public YourScoreDialogFragment(int score) {
		// TODO Auto-generated constructor stub
		this.score = score;
	}
	 @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 builder.setMessage(getString(R.string.score_text) + String.valueOf(score))
		 		.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				}); 
		 return builder.create();
	 }
}
