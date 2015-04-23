package com.example.quickadd;

import java.util.ArrayList;


public class ChartData {
	private ArrayList<Integer> mScores;
	private ArrayList<Integer> mDates;
	private int mMonth;
	private int mYear;
	
	public ChartData(ArrayList<Integer> scores,ArrayList<Integer> dates,
			int month,int year) {
		setScores(scores);
		setDates(dates);
		setMonth(month);
		setYear(year);
	}
	
	public ChartData() {
		
	}

	public ArrayList<Integer> getScores() {
		return mScores;
	}

	public void setScores(ArrayList<Integer> mScores) {
		this.mScores = mScores;
	}

	public ArrayList<Integer> getDates() {
		return mDates;
	}

	public void setDates(ArrayList<Integer> mDates) {
		this.mDates = mDates;
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
