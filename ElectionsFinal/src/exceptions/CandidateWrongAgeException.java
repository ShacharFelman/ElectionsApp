package exceptions;

public class CandidateWrongAgeException extends Exception {

	public CandidateWrongAgeException() {
		super("The age of the the candidate must be greater then 21! ");
	}
}
