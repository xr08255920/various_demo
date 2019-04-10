package com.yejf.mockito;

public class MockitoTest implements  MockitoTestI {

    @Override
    public void someMethod(int i, String abc, String s) {
        System.out.println("someMethod do not thing");
    }

    @Override
    public String someMethod(String some_arg) {
        return "eq_"+some_arg;
    }
}
