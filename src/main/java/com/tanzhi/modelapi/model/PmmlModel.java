package com.tanzhi.modelapi.model;

import java.io.InputStream;
import java.util.Map;

import com.tanzhi.modelapi.model.imodel.IModelFile;
import com.tanzhi.modelapi.model.imodel.IPmmlProcess;

import lombok.Data;

@Data
public class PmmlModel implements IModelFile {
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

    public static PmmlModel create(String filePath){
        PmmlModel r=new PmmlModel();
        r.setFilePath(filePath);
        return r;
    }

    public static PmmlModel create(InputStream stream){
        PmmlModel r=new PmmlModel();
        r.setStream(stream);
        return r;
    }
}