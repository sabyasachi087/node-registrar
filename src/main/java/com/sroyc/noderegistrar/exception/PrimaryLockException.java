package com.sroyc.noderegistrar.exception;

public class PrimaryLockException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1383036243196240083L;

	public PrimaryLockException(String message, Throwable cause) {
		super(message, cause);
	}

	public PrimaryLockException(String message) {
		super(message);
	}

	public PrimaryLockException(Throwable cause) {
		super(cause);
	}

}
