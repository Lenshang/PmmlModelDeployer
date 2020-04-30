package com.tanzhi.modelapi.model;
import java.util.Date;

import lombok.*;

@Data
public class ApiResult {
    private Object prob;
    private Date date=new Date();
    private Boolean isSuccess=true;
    private String message;
    private String code="0000";

    public static ApiResult Create(Object prob,String msg,Boolean isSuccess,Date date,String code){
        ApiResult r=new ApiResult();
        r.setProb(prob);
        r.setMessage(msg);
        r.setIsSuccess(isSuccess);
        r.setDate(date);
        r.setCode(code);
        return r;
    }

    public static ApiResult OK(Object content,String msg){
        return ApiResult.Create(content, msg, true, new Date(),"0000");
    }
    public static ApiResult OK(Object content){
        return ApiResult.OK(content,null);
    }
    public static ApiResult OK(){
        return ApiResult.OK(null);
    }

    public static ApiResult Failure(Object content,String msg){
        return ApiResult.Create(content, msg, false, new Date(),"1002");
    }
    public static ApiResult Failure(String msg,String code){
        return ApiResult.Create(null, msg, false, new Date(), code);
    }
    public static ApiResult Failure(Object content){
        return ApiResult.Failure(content,null);
    }
    public static ApiResult Failure(String msg){
        return ApiResult.Failure(msg,null);
    }
    public static ApiResult Failure(){
        return ApiResult.Failure(null);
    }
}