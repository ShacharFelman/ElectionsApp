package exceptions;

public class NegativeSickDaysNumberExcepetion extends Exception  {

	public NegativeSickDaysNumberExcepetion() {
		super("The number of sick days must be a positive number.");
	}

	
	public NegativeSickDaysNumberExcepetion(String errorMessage) {
		super(errorMessage);
	}
}
