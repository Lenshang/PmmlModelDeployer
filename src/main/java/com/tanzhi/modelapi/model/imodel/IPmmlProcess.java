package com.tanzhi.modelapi.model.imodel;

import java.util.Map;

public interface IPmmlProcess {
    public Map<String, String> afterPredict(Map<String, String> predictResult);
    public Map<String, String> beforePredict(Map<String, String> params);
}