package com.tanzhi.modelapi.controller;

import java.util.Map;

import com.tanzhi.modelapi.core.PmmlBase;
import com.tanzhi.modelapi.model.ApiResult;
import com.tanzhi.modelapi.model.exception.BaseApiException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api")
public class MainController {
    
    @Autowired
    private Map<String,PmmlBase> PmmlMap;
    
    @RequestMapping(value = "/{name}",method = RequestMethod.POST)
    public ApiResult index(@PathVariable("name") String name,@RequestBody Map<String,String> paramsMap) {
        try{
            PmmlBase task=PmmlMap.get(name);
            if(task==null){
                return ApiResult.Failure(String.format("RestApi:%s 接口不存在",name),"1002");
            }
            Object r=task.process(paramsMap);
            return ApiResult.OK(r);
        }
        catch(BaseApiException e){
            return ApiResult.Failure(e.getMessage(),e.getCode());
        }
        catch(Exception e){
            return ApiResult.Failure(e.getMessage(),"1002");
        }
    }
}