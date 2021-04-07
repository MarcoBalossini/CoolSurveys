package it.polimi.db2.coolSurveysWEB.auth.exceptions;

public class TokenException extends Exception{
    public TokenException() {
        super("Invalid Token");
    }

    public TokenException(String message) {
        super(message);
    }
}
