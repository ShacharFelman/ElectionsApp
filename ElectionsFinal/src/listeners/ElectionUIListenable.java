package listeners;

import java.io.*;
import java.time.LocalDate;
import java.time.*;
import java.util.*;

import exceptions.*;
import model.*;
import model.Set;
import model.ElectionManager.eBallotType;
import model.Party.eWing;

public interface ElectionUIListenable {

	public void loadElectionFromFile() throws FileNotFoundException, ClassNotFoundException, IOException;

	public boolean getIsElectionStarted();

	public boolean getIsElectionEnded();

	public void addBallotFromUser(eBallotType ballotType, String address);

	public void addCitizenFromUser(String name, String numId, int yearOfBirth, boolean fIsolation, int numOfDays)
			throws IDException, ElectorAgeException, ElectorAlreadyExistsException, YearNotValidException,
			BallotNotExistsException, ElectorNotMatchBallot, NegativeSickDaysNumberExcepetion;;

	public void addPartyFromUser(String name, LocalDate createDate, eWing politicalWing);

	public void addCandidateFromUser(String name, String numId, int yearOfBirth, String nameOfRegParty)
			throws IDException, YearNotValidException, CandidateWrongRankException, CandidateWrongAgeException,
			ElectorAlreadyExistsException, ElectorAgeException, BallotNotExistsException, ElectorNotMatchBallot, PartyNotExistsException;

	public Vector<String> getPartiesNameList();

	public int getPartyNextAvailableRank(String nameOfRegParty) throws PartyNotExistsException;
	
	public Set<Citizen> getAllCitizens();
	
	public Vector<Ballot<Citizen>> getAllRegularBallots();
	
	public Vector<Ballot<Sick>> getAllCoronaBallots();
	
	public Vector<Ballot<Soldier>> getAllMilitaryBallots();
	
	public Vector<Ballot<SickSoldier>> getAllCoronaMilitaryBallots();
	
	public Vector<Party> getAllParties();

	public void startElection() throws ElectionStartedException;

	public Vector<String> getAllElectorsNameID();
	
	public boolean getIsInIsolationByID(String name);
	
	public void setElectorHadVote(String electorString, boolean fVote, String nameOfParty);
	
	public Vector<Integer> getResults();
	
	public int getNumOfVotes();
	
	public int getElectionYear();
	
	public Month getElectionMonth();
	
	public void saveToFile() throws FileNotFoundException, IOException;
	
	
	
	

}
