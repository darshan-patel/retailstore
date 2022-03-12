package com.synechron.exception;
/**
 * Application generic runtime/un-checked exception.
 *
 */
public class ApplicationRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ApplicationRuntimeException() {
		super();
	}

	public ApplicationRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ApplicationRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationRuntimeException(String message) {
		super(message);
	}

	public ApplicationRuntimeException(Throwable cause) {
		super(cause);
	}
}
