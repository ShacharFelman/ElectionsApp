package exceptions;

import model.ElectionManager.eBallotType;

public class ElectorNotMatchBallot extends Exception  {

	public ElectorNotMatchBallot() {
		super("The citizen is not match the ballot type.");
	}
	
	public ElectorNotMatchBallot(eBallotType type) {
		super("The citizen is not match to " + type.name() + " ballot." );
	}
	
	public ElectorNotMatchBallot(String errorMessage) {
		super(errorMessage);
	}
}
