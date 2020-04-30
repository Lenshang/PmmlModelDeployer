package com.tanzhi.modelapi.model;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.tanzhi.modelapi.model.imodel.IModelFile;
import com.tanzhi.modelapi.model.imodel.IPmmlProcess;

import lombok.Data;

@Data
public class PyModel implements IModelFile {
    private String filePath;
    private InputStream stream;
    private Map<String,String> other;
    private IPmmlProcess pmmlProcess=new IPmmlProcess(){
        @Override
        public Map<String, String> afterPredict(Map<String, String> predictResult) {
            return predictResult;
        }

        @Override
        public Map<String, String> beforePredict(Map<String, String> params) {
            return params;
        }
    };

    public static PyModel create(String filePath,String funcName){
        PyModel r=new PyModel();
        r.setFilePath(filePath);
        Map<String,String> otherInfo=new HashMap<>();
        otherInfo.put("funcName", funcName);
        r.setOther(otherInfo);
        return r;
    }
}