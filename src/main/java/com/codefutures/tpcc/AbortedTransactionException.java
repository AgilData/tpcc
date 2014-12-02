package com.codefutures.tpcc;

public class AbortedTransactionException extends Exception {
	public AbortedTransactionException() {
		super();
	}
	
	public AbortedTransactionException(String message) {
		super(message);
	}
}
