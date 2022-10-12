package exceptions;

import model.Citizen;

public class ElectorAlreadyExistsException extends Exception  {

	public ElectorAlreadyExistsException() {
		super("The citizen is already registered as elector");
	}
	
	public ElectorAlreadyExistsException(Citizen theCitizen) {
		super("The citizen is already registered as elector, Citizen Data =\n" + theCitizen.toString());
	}
	
	public ElectorAlreadyExistsException(String errorMessage) {
		super(errorMessage);
	}
}
