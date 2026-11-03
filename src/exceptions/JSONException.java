package exceptions;

public class JSONException extends MyIOException{
	
	public JSONException(String msg) {
		super("JSON - " + msg);
	}
}
