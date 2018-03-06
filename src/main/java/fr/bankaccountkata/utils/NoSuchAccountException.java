package fr.bankaccountkata.utils;

public class NoSuchAccountException extends Exception {

    String message;

    public NoSuchAccountException(String message) {
        super(message);
        this.message = message;
    }
}
