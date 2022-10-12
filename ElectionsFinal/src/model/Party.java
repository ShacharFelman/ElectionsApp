package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Vector;

import exceptions.*;

public class Party implements Serializable  {

	public enum eWing {
		LEFT, CENTER, RIGHT
	}

	private String name;
	private LocalDate createDate;
	// private Candidate[] candidates;
	private Vector<Candidate> candidates;
	private eWing politicalWing;

//	private int maxCandidates;
//	private int currentCandidates;
//	private int numOfCandidates;
// to do 
	public Party(String name, LocalDate createDate, eWing politicalWing) {
		this.name = name;
		this.createDate = createDate;
		this.politicalWing = politicalWing;
		// this.currentCandidates = 0;
		// this.numOfCandidates = 0;
		// this.maxCandidates = 1;
		// this.candidates = new Candidate[maxCandidates];
		this.candidates = new Vector<Candidate>();

	}

	public String getName() {
		return this.name;
	}

	public LocalDate getCreateDate() {
		return this.createDate;
	}

	public eWing getPoliticalWing() {
		return this.politicalWing;
	}

	public int getCurrentCandidates() {
		return candidates.size();
	}

	/*
	 * public int getCandidatePosByID(long numID) { for (int i = 0; i <
	 * currentCandidates; i++) { if (candidates[i].getNumId() == numID) { return
	 * i+1; } } return 0; }
	 */

	public void addCandidate(Candidate newCandidate, int rankNum) throws CandidateWrongRankException {
		// Add the candidate
//		if (rankNum != candidates.size()) {
//			throw new CandidateWrongRankException(candidates.size());
//		}
		candidates.add(newCandidate);
	}

	public boolean equals(Object other) {
		if (!(other instanceof Party)) {
			return false;
		}
		Party theParty = (Party) other;
		return (this.candidates.equals(theParty.candidates)) && (theParty.createDate == this.createDate)
				&& (theParty.name.equals(this.name));
	}
	
	public int getCandidatePosByID(String numID) {
		for (int i = 0; i < candidates.size(); i++) {
			if (candidates.get(i).getNumId().equals(numID)) {
				return i+1;
			}
		}
		return 0;
	}
	
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append(this.getClass().getSimpleName() + " " + name + " was founded on "
				+ createDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)) + " belongs to the "
				+ politicalWing + " wing.\n");
		if (candidates.size() == 0) {
			str.append("No candidates are regitered to the party.");
		} else {
			str.append("Candidtaes:\n");
			for (int i = 0; i < candidates.size(); i++) {
				if (candidates.get(i) != null) {
					str.append("\t" + (i + 1) + ") " + candidates.get(i).toString());
				}
			}
		}
		return str.toString();
	}
	
	public String toStringShort() {
		StringBuffer str = new StringBuffer();
		str.append(this.getClass().getSimpleName() + " " + name);
		return str.toString();
	}
	
	public String toStringDetails() {
		StringBuffer str = new StringBuffer();
		str.append("Creation date: " + createDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
		str.append("\nPolitical wing: "+ politicalWing);
		if (candidates.size() == 0) {
			str.append("\nNo candidates are regitered to the party.");
		} else {
			str.append("\nCandidtaes:\n");
			for (int i = 0; i < candidates.size(); i++) {
				if (candidates.get(i) != null) {
					str.append("\t" + (i + 1) + ") " + candidates.get(i).toString());
				}
			}
		}
		return str.toString();
	}
	
}
