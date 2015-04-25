package com.example.quickadd;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class CustomTextWatcher implements TextWatcher {
	EditText view;
	public CustomTextWatcher(EditText view) {
		// TODO Auto-generated constructor stub
		this.view = view;
	} 
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		view.setSelection(0);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

}
