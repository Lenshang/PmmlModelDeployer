package com.tanzhi.modelapi.model.imodel;

import java.util.Map;

public interface IModelInvoker {
    public Map<String, String> predict(Map<String, String> paramsMap);
}