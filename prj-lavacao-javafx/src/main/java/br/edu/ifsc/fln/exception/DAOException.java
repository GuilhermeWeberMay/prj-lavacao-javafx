package br.edu.ifsc.fln.exception;

public class DAOException extends Exception {
    public DAOException() {
    }
    public DAOException(String msg) {
        super(msg);
    }
    public DAOException(Exception cause) {
        super(cause);
    }
    public DAOException(String msg, Exception cause) {
        super(msg, cause);
    }
}
