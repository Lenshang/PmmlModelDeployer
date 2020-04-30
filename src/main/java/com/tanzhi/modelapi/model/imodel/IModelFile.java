package com.tanzhi.modelapi.model.imodel;

import java.io.InputStream;
import java.util.Map;

public interface IModelFile {
    public String getFilePath();
    public void setFilePath(String filePath);

    public InputStream getStream();
    public void setStream(InputStream stream);
    
    public IPmmlProcess getPmmlProcess();
    public void setPmmlProcess(IPmmlProcess pmmlProcess);

    public Map<String,String> getOther();
    public void setOther(Map<String,String> other);
}