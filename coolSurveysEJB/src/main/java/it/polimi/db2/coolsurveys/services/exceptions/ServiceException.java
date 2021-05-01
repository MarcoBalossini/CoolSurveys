package it.polimi.db2.coolsurveys.services.exceptions;

public abstract class ServiceException extends Exception {
    public ServiceException() {super();}
    public ServiceException(String message) {super(message);}
}
