package com.example.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

	public static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler({ DataAccessException.class, JpaSystemException.class })
	public ModelAndView handleDatabaseException(Exception e) {
		ModelAndView modelAndView = new ModelAndView("hello");
		logger.error(e.getMessage());
		modelAndView.addObject("errorMessage", "A database error occurred: " + e.getMessage());
		return modelAndView;
	}

	@ExceptionHandler(NullPointerException.class)
	public ModelAndView handleNullPointerException(Exception e) {
		ModelAndView modelAndView = new ModelAndView("hello");
		logger.error(e.getMessage());
		modelAndView.addObject("errorMessage", "Null Pointer Exception: " + e.getMessage());
		return modelAndView;
	}

	@ExceptionHandler(RuntimeException.class)
	public ModelAndView handleRuntimeException(RuntimeException e) {
		ModelAndView modelAndView = new ModelAndView("hello");
		logger.error(e.getMessage());
		modelAndView.addObject("errorMessage", "A runtime exception occurred: " + e);
		return modelAndView;
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception e) {
		ModelAndView modelAndView = new ModelAndView("hello");
		logger.error("gobal{}exp", e.getMessage());
		modelAndView.addObject("errorMessage", "An unexpected error occurred: " + e.getMessage());
		return modelAndView;
	}
}
