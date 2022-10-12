package exceptions;

public class IDException extends Exception {

	public IDException() {
		super("ID must be 9 digits!");
	}
	
	public IDException(int numID) {
		super("ID must be 9 digits, wrong ID = " + numID);
	}
	
	public IDException(String errorMessage) {
		super(errorMessage);
	}

}
