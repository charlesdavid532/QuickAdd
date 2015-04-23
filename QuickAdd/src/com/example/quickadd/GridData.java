package com.example.quickadd;

import java.util.Random;

import android.util.Log;

public class GridData {
	
	public enum DifficultyLevel {
		EASY, MEDIUM, NASTY
	}
	
	private static final String TAG = "GRID-DATA";
	
	private int mGridSize;
	private int[][] mGridNumbers;
	private int[] mRowAdditions;
	private int[] mColumnAdditions;
	private int mTotal;
	private DifficultyLevel mDifficultyLevel;
	
	public GridData(int gridSize,DifficultyLevel difficultyLevel) {
		// TODO Auto-generated constructor stub
		this.mGridSize = gridSize;
		this.mGridNumbers = new int[gridSize][gridSize];
		this.mRowAdditions = new int[gridSize];
		this.mColumnAdditions = new int[gridSize];
		createNewGrid(difficultyLevel);
	}
	
	public void createNewGrid(DifficultyLevel difficultyLevel) {
		this.mDifficultyLevel = difficultyLevel;
		this.generateGrid();
	}
	
	/**
	 * Generates the grid according to the grid size and stores the result in mGridNumbers
	 */
	private void generateGrid() {
		int START = getStartNumberFromDifficulty();
	    int END = getEndNumberFromDifficulty();
	    int total = 0;
	    Random random = new Random();
	    for (int i = 0; i < this.mGridSize; i++){
	    	this.mRowAdditions[i] = 0;
	    	for (int j=0; j < this.mGridSize; j++) {
	    		this.mGridNumbers[i][j] =  showRandomInteger(START, END, random);
	    		total += this.mGridNumbers[i][j];
	    		this.mRowAdditions[i] += this.mGridNumbers[i][j];
	    	}
	    }
	    
	    this.generateColumnAdditions();
	    this.setTotal(total);
	    Log.i(TAG,"total:"+total);
	}
	
	private void generateColumnAdditions () {
		for (int i = 0; i < this.mGridSize; i++){
			this.mColumnAdditions[i] = 0;
	    	for (int j=0; j < this.mGridSize; j++) {
	    		this.mColumnAdditions[i] += this.mGridNumbers[j][i];
	    	}
	    }
	}
	
	private static int showRandomInteger(int aStart, int aEnd, Random aRandom){
	    if (aStart > aEnd) {
	      throw new IllegalArgumentException("Start cannot exceed End.");
	    }
	    //get the range, casting to long to avoid overflow problems
	    long range = (long)aEnd - (long)aStart + 1;
	    // compute a fraction of the range, 0 <= frac < range
	    long fraction = (long)(range * aRandom.nextDouble());
	    int randomNumber =  (int)(fraction + aStart);
	    //Log.i(TAG,"random number:"+randomNumber);
	    return randomNumber;
	}
	
	private int getStartNumberFromDifficulty() {
		switch (this.mDifficultyLevel) {
		case EASY:
			return 10;
		case MEDIUM:
			return 100;
		case NASTY:
			return 1000;
		}
		
		return 10;
	}
	
	private int getEndNumberFromDifficulty() {
		switch (this.mDifficultyLevel) {
		case EASY:
			return 99;
		case MEDIUM:
			return 999;
		case NASTY:
			return 9999;
		}
		
		return 99;
	}

	public int getTotal() {
		return mTotal;
	}

	public void setTotal(int mTotal) {
		this.mTotal = mTotal;
	}
	
	public int getGridSize() {
		return mGridSize;
	}

	public void setGridSize(int mSize) {
		this.mGridSize = mSize;
	}
	
	public int getGridRowAdditionAt(int pos) {
		return this.mRowAdditions[pos];
	}
	
	public int getGridColumnAdditionAt(int pos) {
		return this.mColumnAdditions[pos];
	}
	
	public int getGridNumber(int row, int column) {
		return mGridNumbers[row][column];
	}

}
