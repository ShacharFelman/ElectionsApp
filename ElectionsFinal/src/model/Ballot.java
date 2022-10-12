package model;

import java.io.Serializable;
import java.util.Vector;

import exceptions.*;

public class Ballot <T extends Citizen> implements Serializable  {

	private int serialNum; // Unique number ID
	protected String address;
	protected Vector<T>regElectors;
	protected int numOfVotes;
	protected double percOfVotes;
	protected Vector<Integer>results;  

	public static int numOfBallots = 1000;

	public Ballot(String address) {
		this.address = address;
		this.serialNum = numOfBallots++;
		this.regElectors= new Vector<T>(); // change to vector 
		this.results = new Vector<Integer>();
		this.numOfVotes = 0;
	}

	public int getSerialNum() {
		return serialNum;
	}

	public String getAddress() {
		return address;
	}

	public int getCurrentElectors() {
		return regElectors.size();
	}

	public int getNumOfVotes() {
		return numOfVotes;
	}

	public double getPercOfVotes() {
		return percOfVotes;
	}

	public int resForParty(int numOfParty) {
		return results.get(numOfParty); //  we ask for party number 3 ==> we get how much vote for this party ! + we change to vector 
	}

	public Vector<Integer> getResults() {
		return results;
	}
	
	private void setPercOfVote() {
		percOfVotes = (numOfVotes * 100) / regElectors.size();
	}

	public void addElector(T newElector) throws ElectorAgeException, ElectorNotMatchBallot {
		// Check if citizens is adult and can vote
		if (newElector.getAge() < 18) {
			throw new ElectorAgeException();
		}
			regElectors.add(newElector);
	}

	public void addVoteToBallotBox(int partyNum) {
		results.set(partyNum, results.get(partyNum)+1); // add to the place partyNum the new vote --> partyNum+1 ; 
		numOfVotes++;
		setPercOfVote();
	}

	public void initialResults(int numOfParties) {
		for (int i = 0; i < numOfParties; i++) {
			results.add(Integer.valueOf(0));
		}
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append(this.getClass().getSimpleName() + " box #" + serialNum + " at " + address + ":\n");
		if (regElectors.size() == 0) {
			str.append("No electors are regitered to the ballot.");
		} else {
			str.append("There " + (regElectors.size() == 1 ? "is " : "are ") + regElectors.size() + " elector"
					+ (regElectors.size() == 1 ? "" : "s") + " registered to the ballot.\n");
		}
		str.append(numOfVotes + "/" + regElectors.size() + " voted (" + percOfVotes + "%)");
		return str.toString();
	}
	
	public String toStringShort() {
		StringBuffer str = new StringBuffer();
		str.append(this.getClass().getSimpleName() + " box #" + serialNum);
		return str.toString();
	}
	
	public String toStringDetails() {
		StringBuffer str = new StringBuffer();
		str.append("Address " + address + ".");
		if (regElectors.size() == 0) {
			str.append("\nNo electors are regitered to the ballot.");
		} else {
			str.append("\n" + regElectors.size() + " elector" + (regElectors.size() == 1 ? "" : "s") + " registered to the ballot.");
		}
		str.append("\n" + numOfVotes + "/" + regElectors.size() + " voted (" + percOfVotes + "%).");
		return str.toString();
	}

	@Override
	public boolean equals(Object other) { 
		if (!(other instanceof Ballot)) {
			return false;
		}
		Ballot<T> theBallot = (Ballot<T>) other;
		 return (this.serialNum == theBallot.serialNum) && (this.regElectors.equals(theBallot.regElectors))
				&& (this.address.equals(theBallot.address)) && (this.numOfVotes == theBallot.numOfVotes)
				&& (this.results.equals(theBallot.results));
	}

}
