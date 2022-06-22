package ru.otus.exception;

public abstract class CsvException extends RuntimeException {

    public CsvException(String message) {
        super(message);
    }

    public CsvException(String message, Throwable cause) {
        super(message, cause);
    }
}
