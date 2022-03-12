package com.synechron.gateway.Exception;

import com.synechron.exception.ApplicationRuntimeException;

public class MissingAuthException extends ApplicationRuntimeException {

	private static final long serialVersionUID = 1L;

	public MissingAuthException() {
		super();
	}

	public MissingAuthException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MissingAuthException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingAuthException(String message) {
		super(message);
	}

	public MissingAuthException(Throwable cause) {
		super(cause);
	}
}
