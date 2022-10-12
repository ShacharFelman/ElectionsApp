package exceptions;

import model.ElectionManager.eBallotType;

public class BallotNotExistsException extends Exception  {

	public BallotNotExistsException() {
		super("No matching ballot exists for the citizen.");
	}
	
	public BallotNotExistsException(eBallotType type) {
		super("No " + type.name() + " found for the citizen.");
	}
	
	public BallotNotExistsException(String errorMessage) {
		super(errorMessage);
	}
}
