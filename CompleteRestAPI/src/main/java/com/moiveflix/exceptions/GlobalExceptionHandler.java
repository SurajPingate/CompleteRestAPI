package com.moiveflix.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(FileExistsException.class)
	ProblemDetail handlerFileExistsException(FileExistsException ex) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
	}
	
	@ExceptionHandler(EmptyFileException.class)
	ProblemDetail handlerEmptyFileException(EmptyFileException ex) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
	}
	
	@ExceptionHandler(MovieNotFoundException.class)
	ProblemDetail handlerMovieNotFoundException(MovieNotFoundException ex) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
	}
}
