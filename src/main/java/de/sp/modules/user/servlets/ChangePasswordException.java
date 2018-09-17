package de.sp.modules.user.servlets;


public class ChangePasswordException extends Exception {

    private String message;

    public ChangePasswordException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
