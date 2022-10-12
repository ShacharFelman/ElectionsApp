package model;

import java.util.*;

import exceptions.*;

import java.io.Serializable;
import java.time.*;

import model.Party.eWing;
import listeners.ElectionModelListenable;

public class ElectionManager implements Serializable {

	public enum eBallotType {
		REGULAR, CORONA, MILITARY, CORONA_MILITARY
	}

	private transient Vector<ElectionModelListenable> allListeners;

	private Set<Citizen> allElectors;
	private Vector<Party> allParties;
	private Vector<Ballot<Citizen>> allRegularBallots; // matrix of the 0 row regular , 1st row corona, 2nd row military
	private Vector<Ballot<Sick>> allCoronaBallots; // matrix of the 0 row regular , 1st row corona, 2nd row military
	private Vector<Ballot<Soldier>> allMilitaryBallots; // matrix of the 0 row regular , 1st row corona, 2nd row
														// military
	private Vector<Ballot<SickSoldier>> allCoronaMilitaryBallots; // matrix of the 0 row regular , 1st row corona, 2nd
																	// row military
	private int currentYear; // the current year of the election
	private Month currentMonth; // the current month of the election
	private boolean fElectionStarted;
	private boolean fElectionEnded;
	private int numOfVotes;

	public ElectionManager(int currentYear, Month currentMonth) {
		this.currentYear = currentYear;
		this.currentMonth = currentMonth;
		this.allElectors = new Set<Citizen>();
		this.allParties = new Vector<Party>();
		this.allRegularBallots = new Vector<Ballot<Citizen>>();
		this.allCoronaBallots = new Vector<Ballot<Sick>>();
		this.allMilitaryBallots = new Vector<Ballot<Soldier>>();
		this.allCoronaMilitaryBallots = new Vector<Ballot<SickSoldier>>();
		this.allListeners = new Vector<ElectionModelListenable>();

		this.fElectionStarted = false;
		this.fElectionEnded = false;
		this.numOfVotes = 0;
	}

	public void registerListener(ElectionModelListenable l) {
		if (allListeners == null) {
			allListeners = new Vector<ElectionModelListenable>();
		}
		allListeners.add(l);
	}

	public int getCurrentYear() {
		return currentYear;
	}

	public Month getCurrentMonth() {
		return currentMonth;
	}

	public int getNumOfVotes () {
		return this.numOfVotes;
	}
	
	public Vector<Party> getAllParties() {
		return allParties;
	}

	public Vector<String> getPartiesNames() {
		Vector<String> theNames = new Vector<String>();
		for (int i = 0; i < allParties.size(); i++) {
			theNames.add(allParties.get(i).getName());
		}
		return theNames;
	}

	public Set<Citizen> getAllElectors() {
		return allElectors;
	}

	public Vector<Ballot<Citizen>> getAllRegularBallots() {
		return allRegularBallots;
	}

	public Vector<Ballot<Sick>> getAllCoronaBallots() {
		return allCoronaBallots;
	}

	public Vector<Ballot<Soldier>> getAllMilitaryBallots() {
		return allMilitaryBallots;
	}

	public Vector<Ballot<SickSoldier>> getAllCoronaMilitaryBallots() {
		return allCoronaMilitaryBallots;
	}

	public int getCurrentElectors() {
		return allElectors.size();
	}

	public int getCurrentNummOfParties() {
		return allParties.size();
	}

	public Party getPartybyPos(int pos) throws PartyNotExistsException {
		if (pos < allParties.size()) {
			return allParties.get(pos);
		}
		throw new PartyNotExistsException();
	}

	public Citizen getEllectorByID(String electorID) {
		for (int i = 0; i < allElectors.size(); i++) {
			if (allElectors.getElement(i).getNumId().equals(electorID)) {
				return allElectors.getElement(i);
			}
		}
		return null;
	}

	public Party getPartyByName(String nameOfParty) {
		for (int i = 0; i < allParties.size(); i++) {
			if (allParties.get(i).getName().equals(nameOfParty))
				return allParties.get(i);
		}
		return null;
	}

	public int getPartyNextAvailableRank(Party theParty) {
		return theParty.getCurrentCandidates();
	}

	public boolean isStarted() {
		return fElectionStarted;
	}
	
	public boolean isEnded() {
		return fElectionEnded;
	}

	private void setEelectionStarted() {
		this.fElectionStarted = true;
		for (ElectionModelListenable l : allListeners) {
			l.updateElectionStarted();
		}
	}
	
	private void setEelectionEnded() {

		this.fElectionEnded = true;
		for (ElectionModelListenable l : allListeners) {
			l.updateElectionEnded();
		}
	}


//<==============================>
//<--------  Add ballot --------->
//<= Receive parameters from	=>
//<= controller and create		=>
//<= and add new ballot 		=>
//===============================>
	public void addBallot(eBallotType type, String theAddress) {
		if (type == eBallotType.REGULAR) {
			Ballot<Citizen> R = new Ballot<Citizen>(theAddress);
			allRegularBallots.add(R);
		}
		if (type == eBallotType.CORONA) {
			Ballot<Sick> C = new Ballot<Sick>(theAddress);
			allCoronaBallots.add(C);
		}
		if (type == eBallotType.MILITARY) {
			Ballot<Soldier> M = new Ballot<Soldier>(theAddress);
			allMilitaryBallots.add(M);
		}
		if (type == eBallotType.CORONA_MILITARY) {
			Ballot<SickSoldier> CM = new Ballot<SickSoldier>(theAddress);
			allCoronaMilitaryBallots.add(CM);
		}
	}

//<==============================>
//<---------- Add citizen ------->
//<= Receive parameters from	=>
//<= controller and create 		=> 
//<= and add new citizen 		=>
//===============================>
	public boolean addCitizen(String name, String numId, int yearOfBirth, boolean fIsolation, int numOfDays)
			throws IDException, ElectorAgeException, ElectorAlreadyExistsException, YearNotValidException,
			BallotNotExistsException, ElectorNotMatchBallot, NegativeSickDaysNumberExcepetion {
		// Declare random
		Random random = new Random();
		int randBallot;

		int age = LocalDate.now().getYear() - yearOfBirth;

		// Check if the citizen can be added as elector
		if (age < 18) {
			throw new ElectorAgeException();
		}

		if (fIsolation) {
			if (numOfDays <= 0) {
				throw new NegativeSickDaysNumberExcepetion();
			}

			if (age < 21) {
				SickSoldier newSickSoldier = new SickSoldier(name, numId, yearOfBirth, fIsolation, numOfDays);
				if (allElectors.contains(newSickSoldier)) {
					throw new ElectorAlreadyExistsException();
				}
				if (allCoronaMilitaryBallots.isEmpty()) {
					throw new BallotNotExistsException(eBallotType.CORONA_MILITARY);
				}

				randBallot = random.nextInt(allCoronaMilitaryBallots.size());
				allCoronaMilitaryBallots.get(randBallot).addElector(newSickSoldier);
				newSickSoldier.setRegBallot(allCoronaMilitaryBallots.get(randBallot));
				allElectors.add(newSickSoldier);
				return true;
			} else {
				Sick newSickCitizen = new Sick(name, numId, yearOfBirth, fIsolation, numOfDays);
				if (allElectors.contains(newSickCitizen)) {
					throw new ElectorAlreadyExistsException();
				}

				if (allCoronaBallots.isEmpty()) {
					throw new BallotNotExistsException(eBallotType.CORONA);
				}

				randBallot = random.nextInt(allCoronaBallots.size());
				allCoronaBallots.get(randBallot).addElector(newSickCitizen);
				newSickCitizen.setRegBallot(allCoronaBallots.get(randBallot));
				allElectors.add(newSickCitizen);
				return true;
			}
		} else {
			if (age < 21) {
				Soldier newSoldier = new Soldier(name, numId, yearOfBirth, fIsolation);
				if (allElectors.contains(newSoldier)) {
					throw new ElectorAlreadyExistsException();
				}

				if (allMilitaryBallots.isEmpty()) {
					throw new BallotNotExistsException(eBallotType.MILITARY);
				}

				randBallot = random.nextInt(allMilitaryBallots.size());
				allMilitaryBallots.get(randBallot).addElector(newSoldier);
				newSoldier.setRegBallot(allMilitaryBallots.get(randBallot));
				allElectors.add(newSoldier);
				return true;
			} else {
				Citizen newCitizen = new Citizen(name, numId, yearOfBirth, fIsolation);
				if (allElectors.contains(newCitizen)) {
					throw new ElectorAlreadyExistsException();
				}

				if (allRegularBallots.isEmpty()) {
					throw new BallotNotExistsException(eBallotType.REGULAR);
				}

				randBallot = random.nextInt(allRegularBallots.size());
				allRegularBallots.get(randBallot).addElector(newCitizen);
				newCitizen.setRegBallot(allRegularBallots.get(randBallot));
				allElectors.add(newCitizen);
				return true;
			}
		}
	}

//<==============================>
//<---------- Add party --------->
//<= Receive parameters from	=>
//<= controller and create 		=> 
//<= and add new party 			=>
//===============================>
	public void addParty(String name, LocalDate createDate, eWing politicalWing) {
		Party newParty = new Party(name, createDate, politicalWing);
		allParties.add(newParty);
	}

//<==========================>
//<-------- Add party ------->
//<= Receive party from		=>
//<= controller and add 	=> 
//<= party (for HardCoded)	=>
//===========================>
	public void addParty(Party existParty) {
		allParties.add(existParty);
	}

//<==============================>
//<-------- Add Candidate ------->
//<= Receive parameters from	=>
//<= controller and create 		=> 
//<= and add new candidate		=>
//===============================>
	public void addCandidate(String name, String numId, int yearOfBirth, Party regParty, int rankNum)
			throws IDException, YearNotValidException, CandidateWrongRankException, CandidateWrongAgeException,
			ElectorAlreadyExistsException, ElectorAgeException, BallotNotExistsException, ElectorNotMatchBallot {
		// Declare random
		Random random = new Random();
		int randBallot;

		Candidate newCandidate = new Candidate(name, numId, yearOfBirth, regParty);

		if (newCandidate.getAge() < 21) // check candidate age greater then 21
			throw new CandidateWrongAgeException();
		if (allElectors.contains(newCandidate))
			throw new ElectorAlreadyExistsException(newCandidate);

		if (allRegularBallots.isEmpty()) {
			throw new BallotNotExistsException(eBallotType.REGULAR);
		}

		randBallot = random.nextInt(allRegularBallots.size());
		allRegularBallots.get(randBallot).addElector(newCandidate);
		newCandidate.setRegBallot(allRegularBallots.get(randBallot));

		// Add the candidate to Electors array
		allElectors.add(newCandidate);
		newCandidate.regParty.addCandidate(newCandidate, rankNum);
	}

	public void startElection() throws ElectionStartedException {
		if (fElectionStarted) {
			throw new ElectionStartedException();
		}
		for (int i = 0; i < allRegularBallots.size(); i++) {
			allRegularBallots.get(i).initialResults(allParties.size());
		}
		for (int i = 0; i < allCoronaBallots.size(); i++) {
			allCoronaBallots.get(i).initialResults(allParties.size());
		}
		for (int i = 0; i < allMilitaryBallots.size(); i++) {
			allMilitaryBallots.get(i).initialResults(allParties.size());
		}
		for (int i = 0; i < allCoronaMilitaryBallots.size(); i++) {
			allCoronaMilitaryBallots.get(i).initialResults(allParties.size());
		}
		setEelectionStarted();
	}

	public boolean getIsInIsolationByID(String electorID) {
		for (int i = 0; i < allElectors.size(); i++) {
			if (allElectors.getElement(i).numId.equals(electorID))
				return allElectors.getElement(i).isfIsolation();
		}
		return false;
	}

	public Vector<String> getAllElectorsNameID() {
		Vector<String> allElectorsNameID = new Vector<String>();
		for (int i = 0; i < allElectors.size(); i++) {
			if (!allElectors.getElement(i).fHadVote) {
				allElectorsNameID.add(allElectors.getElement(i).name + "(" + allElectors.getElement(i).numId + ")");
			}
		}
		return allElectorsNameID;
	}

	public void setElectorVote(Citizen c, boolean fVote, Party selectedParty){
		c.setHadVote();
		if (fVote) {
			addVote(c, selectedParty);
		}
		for (ElectionModelListenable l : allListeners) {
			l.updateElectorHadVoted(c);
			checkIfEveryoneVote();
		}
	}

	public void addVote(Citizen c, Party selectedParty) {
		Ballot<? extends Citizen> b = c.getRegBallot();
		int partyNum = allParties.indexOf(selectedParty);
		b.addVoteToBallotBox(partyNum);
		numOfVotes++;
	}

	public void checkIfEveryoneVote() {
		for (int i = 0; i < allElectors.size(); i++) {
			if (!allElectors.getElement(i).fHadVote)
				return;
		}
		setEelectionEnded();
	}

	public Vector<Integer> calculateRes() {
		Vector<Integer> totalRes = new Vector<Integer>();
		// totalRes.setSize(allParties.size());
		for (int i = 0; i < allParties.size(); i++) {
			totalRes.add(Integer.valueOf(0));
		}

		for (int i = 0; i < allParties.size(); i++) {
			for (int j = 0; j < allRegularBallots.size(); j++) {
				totalRes.set(i, totalRes.get(i) + allRegularBallots.get(j).resForParty(i));
			}
			for (int j = 0; j < allCoronaBallots.size(); j++) {
				totalRes.set(i, totalRes.get(i) + allCoronaBallots.get(j).resForParty(i));
			}
			for (int j = 0; j < allMilitaryBallots.size(); j++) {
				totalRes.set(i, totalRes.get(i) + allMilitaryBallots.get(j).resForParty(i));
			}
			for (int j = 0; j < allCoronaMilitaryBallots.size(); j++) {
				totalRes.set(i, totalRes.get(i) + allCoronaMilitaryBallots.get(j).resForParty(i));
			}
		}

		return totalRes;
	}

	public String toStringBallots() {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < allRegularBallots.size(); i++) {
			str.append(allRegularBallots.get(i).toString() + "\n\n");
		}
		for (int i = 0; i < allCoronaBallots.size(); i++) {
			str.append(allCoronaBallots.get(i).toString().toString() + "\n\n");
		}
		for (int i = 0; i < allMilitaryBallots.size(); i++) {
			str.append(allMilitaryBallots.get(i).toString() + "\n\n");
		}
		for (int i = 0; i < allCoronaMilitaryBallots.size(); i++) {
			str.append(allCoronaMilitaryBallots.get(i).toString() + "\n\n");
		}
		return str.toString();
	}

	public String toStringCitizens() {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < allElectors.size(); i++) {
			if (allElectors.getElement(i) instanceof Candidate)
				str.append(((Candidate) allElectors.getElement(i)).toStringWithParty());
			else
				str.append(allElectors.getElement(i).toString());
		}
		return str.toString();
	}

	public String toStringParties() {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < allParties.size(); i++) {
			str.append((i + 1) + ")" + allParties.get(i).toString() + "\n\n");
		}
		return str.toString();
	}

	public String toStringPartiesNames() {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < allParties.size(); i++) {
			str.append((i + 1) + ")" + allParties.get(i).getName() + "\n");
		}
		return str.toString();
	}

	public String toStringAllRes() {
		StringBuffer str = new StringBuffer();
		Vector<Integer> sumOfRes = calculateRes();

		str.append("Final results:\n");
		str.append("--------------\n");
		for (int i = 0; i < allParties.size(); i++) {
			str.append("Party " + allParties.get(i).getName() + ": " + sumOfRes.get(i) + "/" + numOfVotes + " votes ("
					+ ((sumOfRes.get(i) * 100) / numOfVotes) + "%)\n");
		}
		return str.toString();
	}

	public String toStringBallotRes() {
		StringBuffer str = new StringBuffer();

		for (int i = 0; i < allParties.size(); i++) {
			str.append("Party " + allParties.get(i).getName() + ":\n");
			for (int j = 0; j < allRegularBallots.size(); j++) {
				str.append(eBallotType.REGULAR.name().charAt(0) + eBallotType.REGULAR.name().substring(1).toLowerCase()
						+ "  ballot #" + allRegularBallots.get(j).getSerialNum() + " - "
						+ allRegularBallots.get(j).address + ": " + allRegularBallots.get(j).resForParty(i) + "/"
						+ allRegularBallots.get(j).numOfVotes + " votes\n");
			}
			for (int j = 0; j < allCoronaBallots.size(); j++) {
				str.append(eBallotType.CORONA.name().charAt(0) + eBallotType.CORONA.name().substring(1).toLowerCase()
						+ "   ballot #" + allCoronaBallots.get(j).getSerialNum() + " - "
						+ allCoronaBallots.get(j).address + ": " + allCoronaBallots.get(j).resForParty(i) + "/"
						+ allCoronaBallots.get(j).numOfVotes + " votes\n");
			}
			for (int j = 0; j < allMilitaryBallots.size(); j++) {
				str.append(
						eBallotType.MILITARY.name().charAt(0) + eBallotType.MILITARY.name().substring(1).toLowerCase()
								+ " ballot #" + allMilitaryBallots.get(j).getSerialNum() + " - "
								+ allMilitaryBallots.get(j).address + ": " + allMilitaryBallots.get(j).resForParty(i)
								+ "/" + allMilitaryBallots.get(j).numOfVotes + " votes\n");
			}
			for (int j = 0; j < allCoronaMilitaryBallots.size(); j++) {
				str.append(eBallotType.CORONA_MILITARY.name().charAt(0)
						+ eBallotType.CORONA_MILITARY.name().substring(1).toLowerCase() + " ballot #"
						+ allCoronaMilitaryBallots.get(j).getSerialNum() + " - "
						+ allCoronaMilitaryBallots.get(j).address + ": "
						+ allCoronaMilitaryBallots.get(j).resForParty(i) + "/"
						+ allCoronaMilitaryBallots.get(j).numOfVotes + " votes\n");
			}
		}
		return str.toString();
	}

	public String toStringEletionHeader() {
		StringBuffer str = new StringBuffer();
		str.append("\n__________________________________\n");
		str.append("Welcome to " + currentMonth.name().charAt(0) + currentMonth.name().substring(1).toLowerCase() + " "
				+ currentYear + " Elections\n");
		str.append("__________________________________\n");
		return str.toString();
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append(toStringEletionHeader());
		str.append(toStringParties() + "\n");
		str.append(toStringBallots() + "\n");
		str.append(toStringCitizens() + "\n");

		if (fElectionStarted) {
			str.append(toStringAllRes() + "\n");
			str.append(toStringBallotRes() + "\n");
		}
		return str.toString();
	}

}
