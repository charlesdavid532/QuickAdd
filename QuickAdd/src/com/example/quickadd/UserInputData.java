package com.example.quickadd;

public class UserInputData {
	private int[] mRowAdditions;
	private int[] mColumnAdditions;
	private int mTotal;
	private int mTimeTaken; // note this time is in seconds
	
	public UserInputData(int[] rowData, int[] columnData, int total, int timeTaken) {
		// TODO Auto-generated constructor stub
		setUserTotal(total);
		mRowAdditions = new int[rowData.length];
		mColumnAdditions = new int[columnData.length];
		for (int i=0; i < rowData.length; i++) {
			mRowAdditions[i] = rowData[i];
			mColumnAdditions[i] = columnData[i];
		}
		setTimeTaken(timeTaken);
	}

	public int getUserTotal() {
		return mTotal;
	}

	public void setUserTotal(int mTotal) {
		this.mTotal = mTotal;
	}
	
	public int getUserRowInputAt(int pos) {
		return this.mRowAdditions[pos];
	}
	
	public int getUserColumnInputAt(int pos) {
		return this.mColumnAdditions[pos];
	}
	
	public int[] getUserRowInputs() {
		return mRowAdditions;
	}
	
	public int[] getUserColumnInputs() {
		return mColumnAdditions;
	}

	public int getTimeTaken() {
		return mTimeTaken;
	}

	public void setTimeTaken(int mTimeTaken) {
		this.mTimeTaken = mTimeTaken;
	}
}
