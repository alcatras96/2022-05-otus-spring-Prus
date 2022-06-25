package ru.otus.exception;

public class CsvReadException extends CsvException {

    public CsvReadException(String message) {
        super(message);
    }

    public CsvReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
