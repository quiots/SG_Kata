package org.squiot.bank.exception;

public class NegativeAmountException extends Exception {

    public NegativeAmountException(String message){
        super(message);
    }
}
