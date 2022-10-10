package com.database.database.repository.exception;

public class DuplicatedKeyException extends DatabaseException{
    public DuplicatedKeyException() {
    }

    public DuplicatedKeyException(String message) {
        super(message);
    }

    public DuplicatedKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedKeyException(Throwable cause) {
        super(cause);
    }
}
