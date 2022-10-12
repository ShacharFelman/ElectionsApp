package controller;

import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.Vector;

import javax.swing.JOptionPane;

import exceptions.*;
import listeners.*;
import model.*;
import model.ElectionManager.eBallotType;
import model.Party.eWing;
import view.ElectionUIViewable;

public class Controller implements ElectionModelListenable, ElectionUIListenable {

	static final String fileName = "LastElections.dat";

	private ElectionManager electionModel;
	private ElectionUIViewable electionViewUI;

	public Controller(ElectionManager theElection, ElectionUIViewable theView) {
		electionModel = theElection;
		electionViewUI = theView;

		electionModel.registerListener(this);
		electionViewUI.registerListener(this);

		createElectionRound();
	}

	public void createElectionRound() {
		int reply = JOptionPane.showConfirmDialog(null, "Do you want to load last elections from file?",
				"Elections - Create Election", JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION) {
			try {
				loadElectionFromFile();
				electionModel.registerListener(this);
			} catch (ClassNotFoundException | IOException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Elections - Create Election",
						JOptionPane.ERROR_MESSAGE);
			}
			electionViewUI.showView();
		} else if (reply == JOptionPane.NO_OPTION) {
			try {
				createHardCodedObjects(electionModel);
			} catch (CandidateWrongRankException | ElectorAlreadyExistsException | CandidateWrongAgeException
					| ElectorAgeException | BallotNotExistsException | ElectorNotMatchBallot | IDException
					| YearNotValidException | NegativeSickDaysNumberExcepetion e) {
				System.out.println(e.getMessage());
			}
			electionViewUI.showView();
		} else {
			System.exit(0);
		}
	}

	@Override
	public void loadElectionFromFile() throws FileNotFoundException, ClassNotFoundException, IOException {
		ObjectInputStream inFile;
		inFile = new ObjectInputStream(new FileInputStream((fileName)));
		electionModel = (ElectionManager) inFile.readObject();
		inFile.close();
	}

	public static void createHardCodedObjects(ElectionManager theElections) throws CandidateWrongRankException,
			ElectorAlreadyExistsException, CandidateWrongAgeException, ElectorAgeException, BallotNotExistsException,
			ElectorNotMatchBallot, IDException, YearNotValidException, NegativeSickDaysNumberExcepetion {
		theElections.addBallot(eBallotType.REGULAR, "Tel-Aviv");
		theElections.addBallot(eBallotType.REGULAR, "Ramat-Gan");
		theElections.addBallot(eBallotType.REGULAR, "Givat-Shmuel");
		//theElections.addBallot(eBallotType.REGULAR, "Kfar-Saba");
		//theElections.addBallot(eBallotType.REGULAR, "Hod-Hasharon");
		theElections.addBallot(eBallotType.CORONA, "Haifa");
		theElections.addBallot(eBallotType.CORONA, "Naharya");
		theElections.addBallot(eBallotType.CORONA, "Katzrin");
		//theElections.addBallot(eBallotType.CORONA, "Afula");
		//theElections.addBallot(eBallotType.CORONA, "Hadera");
		theElections.addBallot(eBallotType.MILITARY, "Oranit");
		theElections.addBallot(eBallotType.MILITARY, "Ariel");
		theElections.addBallot(eBallotType.MILITARY, "Schem");
		//theElections.addBallot(eBallotType.MILITARY, "Jenin");
		//theElections.addBallot(eBallotType.MILITARY, "Beirut");
		theElections.addBallot(eBallotType.CORONA_MILITARY, "Kirya");
		theElections.addBallot(eBallotType.CORONA_MILITARY, "Mahane-Natan");
		theElections.addBallot(eBallotType.CORONA_MILITARY, "Tzrifin");
		//theElections.addBallot(eBallotType.CORONA_MILITARY, "Ktziot");
		//theElections.addBallot(eBallotType.CORONA_MILITARY, "Ramon");

		Party p1 = new Party("AaAa", LocalDate.now().minusYears(10), eWing.LEFT);
		Party p2 = new Party("BbBb", LocalDate.now().minusYears(5), eWing.CENTER);
		Party p3 = new Party("CcCc", LocalDate.now().minusYears(1), eWing.RIGHT);
		theElections.addParty(p1);
		theElections.addParty(p2);
		theElections.addParty(p3);

		//theElections.addCandidate("Can11", "123123123", 1980, p1, 1);
		//theElections.addCandidate("Can12", "456456456", 1980, p1, 2);
		//theElections.addCandidate("Can21", "789789789", 1980, p2, 1);
		//theElections.addCandidate("Can22", "111222333", 1980, p2, 2);
		//theElections.addCandidate("Can31", "444555666", 1980, p3, 1);
		//theElections.addCandidate("Can32", "777888999", 1980, p3, 2);
		theElections.addCitizen("aaa", "111111111", 1990, false, 0);
		theElections.addCitizen("bbb", "222222222", 1991, true, 12);
		theElections.addCitizen("ccc", "333333333", 1992, true, 5);
		theElections.addCitizen("ddd", "444444444", 2002, false, 0);
		theElections.addCitizen("eee", "555555555", 1994, false, 0);
	}


	@Override
	public int getElectionYear() {
		return electionModel.getCurrentYear();
	}

	@Override
	public Month getElectionMonth() {
		return electionModel.getCurrentMonth();
	}

	
	@Override
	public boolean getIsElectionStarted() {
		return electionModel.isStarted();
	}

	@Override
	public boolean getIsElectionEnded() {
		return electionModel.isEnded();
	}
	
	@Override
	public void addBallotFromUser(eBallotType ballotType, String address) {
		electionModel.addBallot(ballotType, address);
	}

	@Override
	public void addCitizenFromUser(String name, String numId, int yearOfBirth, boolean fIsolation, int numOfDays)
			throws IDException, ElectorAgeException, ElectorAlreadyExistsException, YearNotValidException,
			BallotNotExistsException, ElectorNotMatchBallot, NegativeSickDaysNumberExcepetion {
		electionModel.addCitizen(name, numId, yearOfBirth, fIsolation, numOfDays);
	}

	@Override
	public void addPartyFromUser(String name, LocalDate createDate, eWing politicalWing) {
		electionModel.addParty(name, createDate, politicalWing);
	}

	@Override
	public void addCandidateFromUser(String name, String numId, int yearOfBirth, String nameOfRegParty)
			throws IDException, YearNotValidException, CandidateWrongRankException, CandidateWrongAgeException,
			ElectorAlreadyExistsException, ElectorAgeException, BallotNotExistsException, ElectorNotMatchBallot,
			PartyNotExistsException {
		electionModel.getPartyByName(nameOfRegParty);
		electionModel.addCandidate(name, numId, yearOfBirth, electionModel.getPartyByName(nameOfRegParty),
				getPartyNextAvailableRank(nameOfRegParty));
	}

	public int getPartyNextAvailableRank(String nameOfRegParty) throws PartyNotExistsException {
		return electionModel.getPartyNextAvailableRank(electionModel.getPartyByName(nameOfRegParty));
	}

	@Override
	public Vector<String> getPartiesNameList() {
		return electionModel.getPartiesNames();
	}

	@Override
	public Set<Citizen> getAllCitizens() {
		return electionModel.getAllElectors();
	}

	@Override
	public Vector<String> getAllElectorsNameID() {
		return electionModel.getAllElectorsNameID();
	}

	@Override
	public boolean getIsInIsolationByID(String electorID) {
		String onlyId = electorID.substring(electorID.indexOf('(') + 1, electorID.length() - 1);
		return electionModel.getIsInIsolationByID(onlyId);
	}

	@Override
	public Vector<Ballot<Citizen>> getAllRegularBallots() {
		return electionModel.getAllRegularBallots();
	}

	@Override
	public Vector<Ballot<Sick>> getAllCoronaBallots() {
		return electionModel.getAllCoronaBallots();
	}

	@Override
	public Vector<Ballot<Soldier>> getAllMilitaryBallots() {
		return electionModel.getAllMilitaryBallots();
	}

	@Override
	public Vector<Ballot<SickSoldier>> getAllCoronaMilitaryBallots() {
		return electionModel.getAllCoronaMilitaryBallots();
	}

	public Vector<Party> getAllParties() {
		return electionModel.getAllParties();
	}

	@Override
	public void startElection() throws ElectionStartedException {
		electionModel.startElection();
	}

	@Override
	public void updateElectionStarted() {
		electionViewUI.updateElectionStarted();
	}

	@Override
	public void setElectorHadVote(String electorString, boolean fVote, String nameOfParty) {
		String electorID = electorString.substring(electorString.indexOf('(') + 1, electorString.length() - 1);
		electionModel.setElectorVote(electionModel.getEllectorByID(electorID), fVote, electionModel.getPartyByName(nameOfParty));
	}

	public void updateElectorHadVoted(Citizen c) {
		electionViewUI.removeVoter(c.getName() + "(" + c.getNumId() + ")");
	}

	@Override
	public void updateElectionEnded() {
		electionViewUI.updateElectionEnded();
	}

	@Override
	public Vector<Integer> getResults() {
		return electionModel.calculateRes();
	}

	@Override
	public int getNumOfVotes() {
		return electionModel.getNumOfVotes();
	}
	
	@Override
	public void saveToFile() throws FileNotFoundException, IOException {
		ObjectOutputStream outFile;
		outFile = new ObjectOutputStream(new FileOutputStream((fileName)));
		outFile.writeObject(electionModel);
		outFile.close();
	}




}
