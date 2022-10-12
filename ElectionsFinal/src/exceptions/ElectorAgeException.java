package exceptions;

public class ElectorAgeException extends Exception {

	public ElectorAgeException() {
		super("Elector's age must be 18 or greater!");
	}
	
	public ElectorAgeException(int age) {
		super("Elector's age must be 18 or greater, Elector's age = " + age);
	}
	
	public ElectorAgeException(String errorMessage) {
		super(errorMessage);
	}

}
