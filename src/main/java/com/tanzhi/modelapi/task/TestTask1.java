package com.tanzhi.modelapi.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tanzhi.modelapi.core.PmmlBase;
import com.tanzhi.modelapi.model.FieldRule;
import com.tanzhi.modelapi.model.PmmlModel;
import com.tanzhi.modelapi.model.imodel.IModelFile;
import com.tanzhi.modelapi.utils.Utils;

import org.springframework.stereotype.Service;

/**
 * 测试任务
 */
@Service("task1")
public class TestTask1 extends PmmlBase {
    public TestTask1(){
        super();
        Map<String,IModelFile> models=new HashMap<String,IModelFile>();
        models.put("model1", PmmlModel.create("pmmlModel/demo-model.pmml"));

        List<FieldRule> rules=FieldRule.CreateAll(new String[]{
            "Sepal.Length::double::1",
            "Sepal.Width::double::1",
            "Petal.Length::double::1",
            "Petal.Width::double::1",
            "Species::double::1",
            "level::double::1",
            "level1::double::1",
        });
        init(models, rules);
    }

    @Override
    public String processSingleValue(String value, FieldRule field) {
        value = super.processSingleValue(value, field);
        //处理缺省值 用0
        if(Utils.isNullOrEmpty(value)){
            value="0";
        }
        return value;
    }
    @Override
    public Object AfterPredict(Map<String, Map<String, String>> results) {
        Map<String, String> model1=results.get("model1");

        return model1;
    }
}