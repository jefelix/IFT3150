package ca.umontreal.ift3150.js.exception;

public class FileNotFoundInProjectException extends Exception{

	private static final long serialVersionUID = 531213446813428675L;

	public FileNotFoundInProjectException() {
		super();
	}
	
	public FileNotFoundInProjectException(String message){
		super(message);
	}
}
