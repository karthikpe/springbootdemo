package com.example.demo.exception;

/**
 * Custom service exception for Ufo sightings
 * 
 * @author kekambaram
 */
public class UfoServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public UfoServiceException(String msg) {
		super(msg);
	}
}
