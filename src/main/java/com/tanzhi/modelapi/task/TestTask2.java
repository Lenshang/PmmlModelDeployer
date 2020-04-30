package com.tanzhi.modelapi.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tanzhi.modelapi.core.PmmlBase;
import com.tanzhi.modelapi.model.FieldRule;
import com.tanzhi.modelapi.model.PyModel;
import com.tanzhi.modelapi.model.imodel.IModelFile;
import com.tanzhi.modelapi.utils.Utils;

import org.springframework.stereotype.Service;

/**
 * 测试PYTHON模型算法任务
 */
@Service("pytask")
public class TestTask2 extends PmmlBase {
    public TestTask2(){
        super();
        Map<String,IModelFile> models=new HashMap<String,IModelFile>();
        models.put("model1", PyModel.create("pyModel/testpymodel.py","calculate_score"));

        List<FieldRule> rules=FieldRule.CreateAll(new String[]{
            "testvalue1::double",
            "testvalue1::double"
        });
        init(models, rules);
    }

    @Override
    public String processSingleValue(String value, FieldRule field) {
        value = super.processSingleValue(value, field);
        //传入参数若为空时用-1代替
        if(Utils.isNullOrEmpty(value)){
            value="-1";
        }
        return value;
    }

    @Override
    public Object AfterPredict(Map<String, Map<String, String>> results) {
        Map<String, String> model1=results.get("model1");

        return model1.get("target");
    }
}