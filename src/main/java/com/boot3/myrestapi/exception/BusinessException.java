package com.boot3.myrestapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
public class BusinessException extends RuntimeException {

	@Serial
    private static final long serialVersionUID = 1L;
	private final String message;
	private final HttpStatus httpStatus;

	public BusinessException(String message) {
		//417
		this(message, HttpStatus.EXPECTATION_FAILED);
	}

	public BusinessException(String message, HttpStatus httpStatus) {
		this.message = message;
		this.httpStatus = httpStatus;
	}

}
