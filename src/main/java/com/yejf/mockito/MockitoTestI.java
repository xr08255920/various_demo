package com.yejf.mockito;

public interface MockitoTestI {
    void someMethod(int i, String abc, String s);

    String someMethod(String some_arg);

    void doSomething(Person capture);

    String printPersonName();
}
