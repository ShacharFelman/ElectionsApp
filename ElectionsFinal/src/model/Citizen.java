package model;

import java.io.Serializable;
import java.time.LocalDate;

import exceptions.*;

public class Citizen implements Serializable  {

	protected String name;
	protected String numId;
	protected int yearOfBirth;
	protected Ballot<? extends Citizen> regBallot; // the Ballot of the person
	protected boolean fIsolation;
	protected boolean fHadVote;
	public static final int AGE_LIMIT = 130;

	public Citizen(String name, String numId, int yearOfBirth, boolean fIsolation)
			throws IDException, YearNotValidException { //
		this.name = name;
		if ((numId + "").length() != 9) {
			throw new IDException();
		}
		for(int i = 0 ; i<numId.length(); i++) {
			if (!(Character.isDigit(numId.charAt(i)))) {
				throw new IDException();
			}
		}
		if (LocalDate.now().getYear() - yearOfBirth > AGE_LIMIT) {
			throw new YearNotValidException(yearOfBirth, AGE_LIMIT);
		}
		this.yearOfBirth = yearOfBirth;
		this.numId = numId;
		this.fIsolation = fIsolation;
		this.fHadVote = false;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		LocalDate date = LocalDate.now(); // Create a date object
		return date.getYear() - this.yearOfBirth;
	}

	public String getNumId() {
		return numId;
	}

	public boolean isfIsolation() {
		return fIsolation;
	}

	public int getYearOfBirth() {
		return yearOfBirth;
	}

	public void setRegBallot(Ballot<? extends Citizen> regBallot) { // to do check at the manager
		this.regBallot = regBallot;
	}

	public Ballot<? extends Citizen> getRegBallot() {
		return regBallot;
	}

	public void setHadVote() {
		this.fHadVote = true;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Citizen))
			return false;

		Citizen temp = (Citizen) other;
		return numId.equals(temp.getNumId());
	}

	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append(this.getClass().getSimpleName() + " " + numId + ", Name: " + name + ", Age:  " + getAge()
				+ ". Votes in ballot #" + regBallot.getSerialNum() + "\n");
		return str.toString();
	}
	
	public String toStringShort() {
		StringBuffer str = new StringBuffer();
		str.append(name + ", ID: " + numId);
		return str.toString();

	}
	
	public String toStringDetails() {
		StringBuffer str = new StringBuffer();
		str.append(this.getClass().getSimpleName());
		str.append("\nAge: " + getAge() + ".");
		str.append("\nVotes in ballot #" + regBallot.getSerialNum() + ".");
		return str.toString();
	}

}