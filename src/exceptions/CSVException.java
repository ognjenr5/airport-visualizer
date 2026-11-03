package exceptions;

public class CSVException extends MyIOException{

	public CSVException(String msg) {
		super("CSV - " + msg);
	}

}
