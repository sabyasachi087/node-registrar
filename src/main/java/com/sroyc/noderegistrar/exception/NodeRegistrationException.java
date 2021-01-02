package com.sroyc.noderegistrar.exception;

public class NodeRegistrationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4373178608436919041L;

	public NodeRegistrationException(String message, Throwable cause) {
		super(message, cause);
	}

	public NodeRegistrationException(String message) {
		super(message);
	}

	public NodeRegistrationException(Throwable cause) {
		super(cause);
	}

}
