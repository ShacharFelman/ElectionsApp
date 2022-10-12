package exceptions;

public class CandidateWrongRankException extends Exception {

	public CandidateWrongRankException(int currentCandidates) {
		super("the rank number must be in the correct order!\nThe next available rank is: " + currentCandidates);
	}
}