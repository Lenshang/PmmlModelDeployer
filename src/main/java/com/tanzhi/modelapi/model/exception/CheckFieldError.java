package com.tanzhi.modelapi.model.exception;

public class CheckFieldError extends BaseApiException {

    /**
     *
     */
    private static final long serialVersionUID = -2709871104401965158L;
    
    @Override
    public String getCode() {
        return "1001";
    }

    @Override
    public String getMessage() {
        return "入模参数有误";
    }
}