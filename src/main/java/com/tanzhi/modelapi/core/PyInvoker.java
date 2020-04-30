package com.tanzhi.modelapi.core;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.tanzhi.modelapi.model.Action.IAct2;
import com.tanzhi.modelapi.model.imodel.IModelInvoker;
import com.tanzhi.modelapi.utils.Utils;

import org.python.core.PyDictionary;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class PyInvoker implements IModelInvoker {
    private PythonInterpreter interpreter;
    private PyFunction pyFunction;
    private String file;
    public PyInvoker(String pyFile,String funcName){
        this.file=pyFile;
        interpreter = new PythonInterpreter();
        InputStream is = null;
        try {
            if (this.file != null) {
                is = PmmlInvoker.class.getClassLoader().getResourceAsStream(this.file);
                if(is==null){
                    is = new FileInputStream(this.file);
                }
                interpreter.execfile(is);
                pyFunction = interpreter.get(funcName, PyFunction.class);
        
                System.out.println(String.format("Py模型[%s]读取成功", pyFile));
            }
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
    }

    @Override
    public Map<String, String> predict(Map<String, String> paramsMap) {
        PyObject pyobj = pyFunction.__call__(this.paramConvert(paramsMap));

        Map<String,String> result=new HashMap<>();
        result.put("target", pyobj.toString());
        return result;
    }

    private PyDictionary paramConvert(Map<String, String> paramsMap){
        PyDictionary result=new PyDictionary();
        Utils.mapForeach(paramsMap, new IAct2<String,String>() {
            @Override
            public void invoke(String key, String value) {
                result.put(key, value);
            }
        });
        
        return result;
    }
}