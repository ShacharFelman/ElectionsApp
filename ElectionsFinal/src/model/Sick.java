package model;

import java.io.Serializable;

import exceptions.IDException;
import exceptions.YearNotValidException;

public class Sick extends Citizen implements CoronaSuitable, Serializable {
	
	protected int numOfDaysSick;
	
	public Sick(String name, String numId, int yearOfBirth, boolean fIsolation, int numOfDaysSick)
			throws IDException, YearNotValidException {
		super(name, numId, yearOfBirth, fIsolation);
		this.numOfDaysSick = numOfDaysSick;
	}

	public int getNumOfDaysSick() {
		return numOfDaysSick;
	}

	public void setNumOfDaysSick(int numOfDaysSick) {
		this.numOfDaysSick = numOfDaysSick;
	}

	public boolean coronaSuitUp () {
		return true;
	}
	
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append(super.toString());
		str.append("The citizen is sick " + numOfDaysSick + " dayes.\n");
		return str.toString();
	}
	
	public String toStringDetails() {
		StringBuffer str = new StringBuffer();
		str.append(super.toStringDetails());
		str.append("\nThe citizen is sick/isolated " + numOfDaysSick + " days.");
		return str.toString();
	}
}