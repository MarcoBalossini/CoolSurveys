package it.polimi.db2.coolsurveys.dao.exceptions;

public abstract class DAOException extends Exception {
    public DAOException(String message) {super(message);}
    public DAOException() {super();}
}
