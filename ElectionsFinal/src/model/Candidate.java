package model;

import java.io.Serializable;

import exceptions.*;

public class Candidate extends Citizen implements Serializable  {
	
	protected Party regParty;

	public Candidate(String name, String numId, int yearOfBirth, Party regParty)
			throws IDException, YearNotValidException {
		super(name, numId, yearOfBirth, false);
		this.regParty = regParty;
	}

	public Party getRegParty() {
		return regParty;
	}

	public boolean setRegParty(Party newParty) {
		if (regParty != null) {
			return false;
		}
		this.regParty = newParty;
		return true;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Candidate))
			return false;

		Candidate temp = (Candidate) other;
		return numId.equals(temp.getNumId());
	}
	
	public String toStringWithParty() {
		int candidatePos = regParty.getCandidatePosByID(this.numId);
		return super.toString() + (candidatePos > 0 ? "Candidate at " + regParty.getName() + " , number " + candidatePos + " place.\n\n" : "");
	}
}
