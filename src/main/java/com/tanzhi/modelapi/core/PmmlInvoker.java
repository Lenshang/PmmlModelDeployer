package com.tanzhi.modelapi.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;

import com.tanzhi.modelapi.model.imodel.IModelInvoker;

import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.FieldValue;
import org.jpmml.evaluator.InputField;
import org.jpmml.evaluator.ModelEvaluator;
import org.jpmml.evaluator.ModelEvaluatorFactory;

import org.jpmml.model.PMMLUtil;
import org.xml.sax.SAXException;
/**
 * 读取pmml 获取模型
 * @author biantech
 *
 */
public class PmmlInvoker implements IModelInvoker{
    private ModelEvaluator<?> modelEvaluator;
    public String filePath;
    // 通过文件读取模型
    public PmmlInvoker(String pmmlFileName) {
        PMML pmml = null;
        InputStream is = null;
        this.filePath=pmmlFileName;
        try {
            if (pmmlFileName != null) {
                is = PmmlInvoker.class.getClassLoader().getResourceAsStream(pmmlFileName);
                if(is==null){
                    is = new FileInputStream(pmmlFileName);
                }
                pmml = PMMLUtil.unmarshal(is);
            }
            this.modelEvaluator = ModelEvaluatorFactory.newInstance().newModelEvaluator(pmml);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(is!=null)
                    is.close();
            } catch (Exception localIOException3) {
                localIOException3.printStackTrace();
            }
        }
        this.modelEvaluator.verify();
        System.out.println(String.format("模型[%s]读取成功",pmmlFileName));
    }

    // 通过输入流读取模型
    public PmmlInvoker(InputStream is) {
        PMML pmml;
        try {
            pmml = PMMLUtil.unmarshal(is);
            try {
                is.close();
            } catch (IOException localIOException) {

            }
            this.modelEvaluator = ModelEvaluatorFactory.newInstance().newModelEvaluator(pmml);
        } catch (SAXException e) {
            pmml = null;
        } catch (JAXBException e) {
            pmml = null;
        } finally {
            try {
                is.close();
            } catch (IOException localIOException3) {
            }
        }
        this.modelEvaluator.verify();
    }

    public Map<String, String> predict(Map<String, String> paramsMap) {
        List<InputField> inputFields = this.modelEvaluator.getInputFields();
        Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();
        for (InputField inputField : inputFields){
            FieldName inputFieldName = inputField.getName();
            Object rawValue = paramsMap.get(inputFieldName.getValue());
            FieldValue inputFieldValue = inputField.prepare(rawValue);
            arguments.put(inputFieldName, inputFieldValue);
        }


        Map<FieldName, ?> _r=this.modelEvaluator.evaluate(arguments);
        Map<String, String> result=new HashMap<>();
        for(Map.Entry<FieldName, ?> entry : _r.entrySet()){
            String k=entry.getKey().getValue().toString();
            Object v=entry.getValue();
            result.put(k, v.toString());
        }
        return result;
    }

    // private Map<FieldName, String> ConvertParams(Map<String, String> paramsMap){
    //     Map<FieldName, String> r=new HashMap<>();
    //     for(Map.Entry<String, String> entry : paramsMap.entrySet()){
    //         r.put(FieldName.create(entry.getKey()), entry.getValue());
    //     }
    //     return r;
    // }
}