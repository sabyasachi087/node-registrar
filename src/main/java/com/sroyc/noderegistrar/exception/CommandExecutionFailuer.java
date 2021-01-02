package com.sroyc.noderegistrar.exception;

public class CommandExecutionFailuer extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2102643039622021542L;

	public CommandExecutionFailuer(String message, Throwable cause) {
		super(message, cause);
	}

	public CommandExecutionFailuer(String message) {
		super(message);
	}

	public CommandExecutionFailuer(Throwable cause) {
		super(cause);
	}

}
