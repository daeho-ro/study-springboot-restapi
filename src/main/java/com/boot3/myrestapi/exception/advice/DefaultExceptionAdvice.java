package com.boot3.myrestapi.exception.advice;

import com.boot3.myrestapi.exception.BusinessException;
import com.boot3.myrestapi.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class DefaultExceptionAdvice {

	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<Object> handleException(BusinessException e) {
		Map<String, Object> result = new HashMap<>();
		result.put("message", "[안내] " + e.getMessage());
		result.put("httpStatus", e.getHttpStatus().value());

		return new ResponseEntity<>(result, e.getHttpStatus());
	}
	
	@ExceptionHandler(SystemException.class)
	protected ResponseEntity<Object> handleException(SystemException e) {
		Map<String, Object> result = new HashMap<>();
		result.put("message", "[시스템 오류] " + e.getMessage());
		result.put("httpStatus", e.getHttpStatus().value());

		return new ResponseEntity<>(result, e.getHttpStatus());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	protected ResponseEntity<Object> handleException(HttpMessageNotReadableException e) {
		Map<String, Object> result = new HashMap<>();
		result.put("message", e.getMessage());
		result.put("httpStatus", HttpStatus.BAD_REQUEST.value());

		return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceAccessException.class)
	protected ResponseEntity<Object> handleException(ResourceAccessException e) {
		Map<String, Object> result = new HashMap<>();
		result.put("message", "[연결 오류] 서버와 연결에 실패했습니다.");
		result.put("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());

		return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<Object> handleException(AccessDeniedException e) {
		Map<String, Object> result = new HashMap<>();
		result.put("message", e.getMessage());
		result.put("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());

		return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleException(Exception e) {
		System.out.println(">>>>>> exception = " + e);
		log.error(e.getMessage(), e);
		e.printStackTrace();

		Map<String, Object> result = new HashMap<>();
		result.put("message", "예상치 못한 문제가 발생했습니다.\n관리자에게 연락 하시기 바랍니다.");
		result.put("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());

		return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
