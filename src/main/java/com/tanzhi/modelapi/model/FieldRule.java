package com.tanzhi.modelapi.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FieldRule {
    /**字段名称 */
    private String fieldName;
    /**字段类型 为str,number,double,null */
    private String fieldType;
    /**映射名称 默认留空 该参数字段会被最终映射成模型入参字段名 */
    private String mapName;
    /**是否为必须字段，若为true 如果传入参数不存在，则会抛出异常 */
    private Boolean notNull;
    /**获得字段名函数 如果mapName有值，则返回mapName值
     * @param _fieldName 字段名
     * @param _mapName 映射名
     * @return 输出字段名称
     */
    private Func.IFunc2<String,String,String> processFieldName=new Func.IFunc2<String,String,String>(){
        @Override
        public String invoke(String _fieldName,String _mapName) {
            if(_mapName==null || "".equals(_mapName)){
                return _fieldName;
            }
            return _mapName;
        }
    };
    /**
     * 获得字段值处理函数 默认为空
     * @param 传入值
     * @return 处理后传出值
     */
    private Func.IFunc1<String,String> processFieldValue=null;

    private FieldRule(String fieldName,String fieldType,Boolean notNull){
        this.fieldName=fieldName;
        this.fieldType=fieldType;
        this.notNull=notNull;
    }
    private FieldRule(String fieldName,String fieldType){
        this.fieldName=fieldName;
        this.fieldType=fieldType;
        this.notNull=false;
    }
    public static FieldRule Create(String fieldName,String fieldType,String paramName,Boolean notNull){
        FieldRule r=new FieldRule(fieldName, fieldType,notNull);
        r.setMapName(paramName);
        return r;
    }
    public static FieldRule Create(String fieldName,String fieldType,Boolean notNull){
        return new FieldRule(fieldName, fieldType,notNull);
    }
    public static FieldRule Create(String fieldName,String fieldType){
        return new FieldRule(fieldName, fieldType);
    }
    /**
     * 快速创建生成 FieldRule List
     * @param fieldParserArray 字段规则定义语法 规则: [字段名::字段类型::是否必须(0,1)::映射(转换)字段名称]
     * @return List<FieldRule>
     */
    public static List<FieldRule> CreateAll(String[] fieldParserArray){
        List<FieldRule> result=new ArrayList<>();
        for(String item : fieldParserArray){
            String[] strlist=item.split("::");
            if(strlist.length==4){
                result.add(FieldRule.Create(strlist[0], strlist[1],strlist[3],strlist[2].equals("1")));
            }
            else if(strlist.length==3){
                result.add(FieldRule.Create(strlist[0], strlist[1],strlist[2].equals("1")));
            }
            else if(strlist.length==2){
                result.add(FieldRule.Create(strlist[0], strlist[1]));
            }
            else{
                result.add(FieldRule.Create(item, "double"));//默认给double类型
            }
        }

        return result;
    }
}