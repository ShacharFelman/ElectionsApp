package exceptions;

import java.time.LocalDate;

public class YearNotValidException extends Exception {

	public YearNotValidException() {
		super("The year of birth must be logical");
	}
	
	public YearNotValidException(int year, int birthLimit) {
		super("The citizen age can be at most " + birthLimit + ", worng citizen's age = " + (LocalDate.now().getYear() - year));
	}
	
	public YearNotValidException(String errorMessage) {
		super(errorMessage);
	}

}
