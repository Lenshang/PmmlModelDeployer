package com.tanzhi.modelapi.model;

public class Func {
    public interface IFunc<R> {
        public R invoke();
    }

    public interface IFunc1<T1,R>{
        public R invoke(T1 param);
    }

    public interface IFunc2<T1,T2,R>{
        public R invoke(T1 param1,T2 param2);
    }

    public interface IFunc3<T1,T2,T3,R>{
        public R invoke(T1 param1,T2 param2,T3 param3);
    }
    
    public interface IFunc4<T1,T2,T3,T4,R>{
        public R invoke(T1 param1,T2 param2,T3 param3,T4 param4);
    }
}
