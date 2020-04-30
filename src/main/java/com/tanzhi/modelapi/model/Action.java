package com.tanzhi.modelapi.model;

public class Action {
    public interface IAct{
        public void invoke();
    }

    public interface IAct1<T>{
        public void invoke(T param);
    }

    public interface IAct2<T1,T2>{
        public void invoke(T1 param1,T2 param2);
    }

    public interface IAct3<T1,T2,T3>{
        public void invoke(T1 param1,T2 param2,T3 param3);
    }

    public interface IAct4<T1,T2,T3,T4>{
        public void invoke(T1 param1,T2 param2,T3 param3,T4 param4);
    }
}