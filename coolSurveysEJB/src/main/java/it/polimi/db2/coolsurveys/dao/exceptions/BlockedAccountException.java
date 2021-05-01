package it.polimi.db2.coolsurveys.dao.exceptions;

import java.time.LocalDateTime;

public class BlockedAccountException extends DAOException {
    public BlockedAccountException() {super();}
    public BlockedAccountException(LocalDateTime banTime) {super("User banned until " + banTime);}
}
