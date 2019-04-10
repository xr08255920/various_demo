package com.yejf.mockito;

public class MockitoTest implements  MockitoTestI {
    private Person ppp;

    @Override
    public void someMethod(int i, String abc, String s) {
        System.out.println("someMethod do not thing");
    }

    @Override
    public String someMethod(String some_arg) {
        String s = "eq_" + some_arg;
        System.out.println("native method :"+s);
        return s;
    }

    @Override
    public void doSomething(Person capture) {
        capture.setName("mike");
    }

    @Override
    public String printPersonName() {
        return ppp.getName();
    }
}
