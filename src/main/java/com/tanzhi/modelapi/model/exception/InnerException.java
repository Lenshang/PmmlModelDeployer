package com.tanzhi.modelapi.model.exception;

public class InnerException extends BaseApiException {

    /**
     *
     */
    private static final long serialVersionUID = 1551017087343691252L;
    @Override
    public String getCode() {
        return "1002";
    }

    @Override
    public String getMessage() {
        return "内部错误未取得结果数据";
    }
}