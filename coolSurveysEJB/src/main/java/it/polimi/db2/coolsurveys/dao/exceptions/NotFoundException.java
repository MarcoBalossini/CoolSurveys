package it.polimi.db2.coolsurveys.dao.exceptions;

public class NotFoundException extends DAOException{
    public NotFoundException() {
        super();
    }
    public NotFoundException(String message) { super(message);}
}
