package com.tanzhi.modelapi.model.exception;

public class BaseApiException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = -3555343671836874872L;
    private String code = "1002";
    public String getCode(){
        return this.code;
    }
}