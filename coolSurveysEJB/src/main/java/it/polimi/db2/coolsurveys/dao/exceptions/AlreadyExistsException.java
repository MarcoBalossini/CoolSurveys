package it.polimi.db2.coolsurveys.dao.exceptions;

public class AlreadyExistsException extends Exception{
    public AlreadyExistsException(){
        super();
    }
    public AlreadyExistsException(String message) {super(message);}
}
