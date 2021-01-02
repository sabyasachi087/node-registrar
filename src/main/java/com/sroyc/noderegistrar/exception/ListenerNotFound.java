package com.sroyc.noderegistrar.exception;

public class ListenerNotFound extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4404242524398242407L;

	public ListenerNotFound(String message, Throwable cause) {
		super(message, cause);
	}

	public ListenerNotFound(String message) {
		super(message);
	}

	public ListenerNotFound(Throwable cause) {
		super(cause);
	}

}
