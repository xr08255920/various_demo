package com.yejf.stack;

/**
 * Created by tony on 2019/2/28.
 */
public class A {
    public static void main(String[] args) {
        new Thread(() -> {
            a();
        }).start();

        a();


    }

    private static void a() {
        String name = "666";
        System.out.println("i am a");
        B.b();
    }
}
