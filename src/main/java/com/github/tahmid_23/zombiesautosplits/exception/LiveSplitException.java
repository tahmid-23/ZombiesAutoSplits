package com.github.tahmid_23.zombiesautosplits.exception;

public class LiveSplitException extends Exception {
    public LiveSplitException(String message) {
        super(message);
    }

    public LiveSplitException(String message, Throwable cause) {
        super(message, cause);
    }

    public LiveSplitException(Throwable cause) {
        super(cause);
    }

    public LiveSplitException() {
    }
}
