package com.example.quickadd;

public class DateModel {
	private int mMonth;
	private int mYear;
	
	public DateModel(int month,int year) {
		setMonth(month);
		setYear(year);
	}

	public int getMonth() {
		return mMonth;
	}

	public void setMonth(int mMonth) {
		this.mMonth = mMonth;
	}

	public int getYear() {
		return mYear;
	}

	public void setYear(int mYear) {
		this.mYear = mYear;
	}

}
