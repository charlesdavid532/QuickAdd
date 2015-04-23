package com.example.quickadd;



public class ResultData {
	
	private GridData mGridData;
	private UserInputData mUserData;
	
	private boolean[] mRowCorrect;
	private boolean[] mColumnCorrect;
	private boolean mIsTotalCorrect;
	private int mScore;
	
	private static final int SCORE_FACTOR = 5000;
	
	public ResultData(GridData gridData, UserInputData userData) {
		// TODO Auto-generated constructor stub
		this.mGridData = gridData;
		this.mUserData = userData;
		int gridSize = this.mGridData.getGridSize();
		mRowCorrect = new boolean[gridSize];
		mColumnCorrect = new boolean[gridSize];
		computeResult();
	}

	public int getScore() {
		return mScore;
	}

	public void setScore(int mScore) {
		this.mScore = mScore;
	}

	public boolean isTotalCorrect() {
		return mIsTotalCorrect;
	}
	
	private int getIsTotalCorrect() {
		if (mIsTotalCorrect) {
			return 1;
		} else {
			return 0;
		}
	}

	public void setIsTotalCorrect(boolean mIsTotalCorrect) {
		this.mIsTotalCorrect = mIsTotalCorrect;
	}
	
	private void computeResult() {
		for (int i=0; i < mRowCorrect.length;i++) {
			if (mGridData.getGridRowAdditionAt(i) == mUserData.getUserRowInputAt(i)) {
				mRowCorrect[i] = true;
			} else {
				mRowCorrect[i] = false;
			}
			
			if (mGridData.getGridColumnAdditionAt(i) == mUserData.getUserColumnInputAt(i)) {
				mColumnCorrect[i] = true;
			} else {
				mColumnCorrect[i] = false;
			}
		}
		
		if (mGridData.getTotal() == mUserData.getUserTotal()) {
			mIsTotalCorrect = true;
		} else {
			mIsTotalCorrect = false;
		}
		
		computeScore();
	}
	
	private void computeScore() {
		setScore((int)(getRowsCorrect() + getColumnsCorrect() + getIsTotalCorrect()) * SCORE_FACTOR / mUserData.getTimeTaken());
	}
	
	private int getTotalAdditions () {
		return (mRowCorrect.length + mColumnCorrect.length + 1);
	}
	
	public int getRowsCorrect () {
		int correct = 0;
		for (int i=0; i < mRowCorrect.length; i++) {
			if (mRowCorrect[i]) 
				correct++;
		}
		
		return correct;
	}
	
	public int getColumnsCorrect () {
		int correct = 0;
		for (int i=0; i < mColumnCorrect.length; i++) {
			if (mColumnCorrect[i]) 
				correct++;
		}
		
		return correct;
	}
	
	
	public boolean getRowResultAt(int pos) {
		return mRowCorrect[pos];
	}
	
	public boolean getColumnResultAt(int pos) {
		return mColumnCorrect[pos];
	}
	
	public boolean[] getRowResults() {
		return mRowCorrect;
	}
	
	public boolean[] getColumnResults() {
		return mColumnCorrect;
	}
}
