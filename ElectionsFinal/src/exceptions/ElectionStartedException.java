package exceptions;

public class ElectionStartedException extends Exception {

	public ElectionStartedException() {
		super("Election alredy startd, operation cannot be made cannot be made.");
	}
	
	public ElectionStartedException(String errorMessage) {
		super(errorMessage);
	}

}
