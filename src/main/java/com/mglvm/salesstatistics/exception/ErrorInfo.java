package com.mglvm.salesstatistics.exception;
/**
 * Created by Vinoth Kumar on 21/12/2017.
 */
public class ErrorInfo {
    private int errorCode;
    private String errorMessage;

    public ErrorInfo(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
