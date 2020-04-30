package com.tanzhi.modelapi.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tanzhi.modelapi.model.FieldRule;
import com.tanzhi.modelapi.model.PmmlModel;
import com.tanzhi.modelapi.model.PyModel;
import com.tanzhi.modelapi.model.exception.CheckFieldError;
import com.tanzhi.modelapi.model.exception.InnerException;
import com.tanzhi.modelapi.model.imodel.IModelFile;
import com.tanzhi.modelapi.model.imodel.IModelInvoker;
import com.tanzhi.modelapi.utils.Utils;

/**
 * Pmml任务处理基类
 * 运行顺序:Init()=>beforePredict()=>单个模型中beforePredict()=>predict=>单个模型中afterPredict()=>AfterPredict()
 * beforePredict执行顺序: Rules::getProcessFieldName()=>processSingleValue()>>Rule::getProcessFieldValue()=>判断字段值类型=>判断可否为空
 */
public abstract class PmmlBase {
    /**模型加载缓存存放 */
    Map<String,IModelInvoker> modelStore;
    /**模型对象存放 */
    Map<String,IModelFile> models;
    /**模型规则对象 */
    List<FieldRule> fieldRules;
    /**
     * 初始化一个PmmlTask
     * @param modelFileList 模型文件列表
     * @param fieldRules 模型规则
     */
    public PmmlBase(){
        this.modelStore=new HashMap<String,IModelInvoker>();
        this.models=new HashMap<String,IModelFile>();
    }

    public void init(Map<String,IModelFile> modelList,List<FieldRule> fieldRules){
        this.fieldRules=fieldRules;
        this.models=modelList;
        for(Map.Entry<String,IModelFile> entry : modelList.entrySet()){
            String name=entry.getKey();
            IModelFile model=entry.getValue();
            IModelInvoker invocker=null;
            if(model.getClass()==PmmlModel.class){
                if(model.getFilePath() != null ||(!"".equals(model.getFilePath()))){
                    invocker=new PmmlInvoker(model.getFilePath());
                }
                else if(model.getStream()!=null){
                    invocker=new PmmlInvoker(model.getStream());
                }
                else{
                    throw new InnerException();
                }
            }
            else if(model.getClass()==PyModel.class){
                invocker=new PyInvoker(model.getFilePath(), model.getOther().get("funcName"));
            }

            if(invocker!=null){
                this.modelStore.put(name, invocker);
            }
        }
    }

    /**
     * 获得模型处理对象
     * @param name 模型名称
     */
    public IModelInvoker getModel(String name){
        return this.modelStore.get(name);
    }

    /**
     * 处理单个字段值
     * @param value 值
     * @param rule 字段Rule
     * @return
     */
    public String processSingleValue(String value,FieldRule field){
        //单个处理字段值
        if(field.getProcessFieldValue()!=null){
            //如果有单字段规则处理值函数，先进行单字段规则处理值
            value=field.getProcessFieldValue().invoke(value);
        }
        return value;
    }

    /**
     * 预测前发生 用于处理入参 (一般情况下不建议重写此方法)
     * 
     * @param params
     * @return 处理后的参数结果
     * @throws Exception
     */
    public Map<String, String> beforePredict(Map<String, String> params){
        Map<String,String> result=new HashMap<>();
        for(FieldRule field:this.fieldRules){
            //验证字段是否为必须字段时，字段不存在 同时获取值(会同时兼容fieldName和mapName)
            String finalValue=params.get(field.getFieldName());
            finalValue=finalValue==null?params.get(field.getMapName()):finalValue;
            if(finalValue==null&&field.getNotNull()){
                throw new CheckFieldError();
            }
            
            String finalKey=field.getProcessFieldName().invoke(field.getFieldName(), field.getMapName());
            // String finalValue=this.getValueFromParams(field.getFieldName(),params);
            // finalValue=Utils.isNullOrEmpty(finalKey)?this.getValueFromParams(field.getMapName(),params):finalValue;
            //全局处理值
            finalValue=processSingleValue(finalValue,field);

            //如果处理之后字段依然是控制,跳过该字段
            if(!Utils.isNullOrEmpty(finalValue)){
                //处理字段类型
                String valueType=Utils.getValueType(finalValue);
                List<String> checkList=new ArrayList<>();
                if(field.getFieldType().equals("double")){
                    checkList.add("double");
                    checkList.add("number");
                }
                else if(field.getFieldType().equals("double")){
                    checkList.add("double");
                }
                else if(field.getFieldType().equals("str")){
                    checkList.add("double");
                    checkList.add("number");
                    checkList.add("str");
                    checkList.add("null");
                }

                if(!checkList.stream().anyMatch(item->item==valueType)){
                    //如果字段类型
                    if(field.getNotNull()){
                        throw new CheckFieldError();
                    }
                    else{
                        result.put(finalKey, "");
                    }
                }
                else{
                    result.put(finalKey, finalValue);
                }
            }
            // else if(!field.getNotNull()){
            //     result.put(finalKey, "");
            // }
            // else{
            //     throw new CheckFieldError();
            // }
        }
        return result;
    }

    /**
     * 预测完成后发生
     * @param results 模型预测结果
     * @return
     */
    public Object AfterPredict(Map<String,Map<String, String>> results){
        if(results.size()==1){
            return results.values().toArray()[0];
        }
        else{
            return results.values().toArray();
        }
    }

    /**
     * 模型执行处理方法 (大多数情况下不必重写此方法)
     * 
     * @return 计算结果
     * @throws Exception
     */
    public Object process(Map<String, String> params){
        //模型结果存放类
        Map<String,Map<String, String>> results=new HashMap<>();
        //全局先处理字段参数
        params=this.beforePredict(params);
        //分别处理每个模型
        for(Map.Entry<String,IModelFile> entry : this.models.entrySet()){
            String name=entry.getKey();
            IModelFile model=entry.getValue();
            IModelInvoker invoker=this.modelStore.get(name);
            Map<String,String> _params=model.getPmmlProcess().beforePredict(params);
            Map<String, String> _result=invoker.predict(_params);
            _result=model.getPmmlProcess().afterPredict(_result);
            results.put(name, _result);
        }

        return this.AfterPredict(results);
    }

    // private FieldRule findFieldFromFieldRules(String name){
    //     FieldRule r=null;
    //     for(FieldRule field:this.fieldRules){
    //         if(field.getFieldName()==name){
    //             r=field;
    //             break;
    //         }
    //         else if(field.getMapName()==name){
    //             r=field;
    //             break;
    //         }
    //     }
    //     return r;
    // }

    // private Map<String, String> ConvertResult(Map<FieldName, String> results){
    //     Map<String, String> r=new HashMap<>();
    //     for(Map.Entry<FieldName, String> entry : results.entrySet()){
    //         String k=entry.getKey().getValue().toString();
    //         String v=entry.getValue().toString();
    //         r.put(k, v);
    //     }
    //     return r;
    // }
}