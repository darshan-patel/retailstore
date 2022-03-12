package com.synechron.gateway.Exception;

import com.synechron.exception.ApplicationRuntimeException;

public class InvalidTokenStructureException extends ApplicationRuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidTokenStructureException() {
		super();
	}

	public InvalidTokenStructureException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidTokenStructureException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidTokenStructureException(String message) {
		super(message);
	}

	public InvalidTokenStructureException(Throwable cause) {
		super(cause);
	}
}
