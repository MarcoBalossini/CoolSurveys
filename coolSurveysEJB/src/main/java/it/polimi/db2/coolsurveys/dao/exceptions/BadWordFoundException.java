package it.polimi.db2.coolsurveys.dao.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class BadWordFoundException extends DAOException {
}
