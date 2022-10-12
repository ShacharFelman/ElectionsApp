package exceptions;

public class PartyNotExistsException extends Exception  {

	public PartyNotExistsException() {
		super("Requested party does not exists");
	}
	
	public PartyNotExistsException(String errorMessage) {
		super(errorMessage);
	}
}
