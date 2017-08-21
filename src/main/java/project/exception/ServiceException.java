package project.exception;

public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 8270375087160893585L;
	
	public ServiceException(Exception e) {
		super(e);
	}
	
	public ServiceException(String message, Exception e) {
		super(message, e);
	}
}
