package com.yejf;

import java.io.UnsupportedEncodingException;

/**
 * Created by tony on 2019/3/6.
 */
public class Encoding {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String a = "臭傻白万岁";
        printBin(a.getBytes());
//        printHex(a.getBytes("UTF-8"));
//        printHex(a.toCharArray());

        // byte[] ----> int ---（对照系统unicode编码表）-->
    }

    public static void printInt(int i){
        System.out.println(i);
    }
    public static void printBytes(byte[] bytes){
        for (int i = 0; i < bytes.length; i++) {

            System.out.print(bytes[i]+" ");

        }
        System.out.println();
    }

    public static void printChars(char[] bytes){
        for (int i = 0; i < bytes.length; i++) {

            System.out.print((int)bytes[i]+" ");

        }
        System.out.println();
    }
    public static void printHex(char[] bytes){
        for (int i = 0; i < bytes.length; i++) {

            System.out.print(Integer.toBinaryString(bytes[i]&0xff)+" ");

        }
        System.out.println();
    }
    public static void printHex(byte[] bytes){
        for (int i = 0; i < bytes.length; i++) {

            System.out.print(Integer.toBinaryString(bytes[i]&0xff)+" ");

        }
        System.out.println();
    }

    public static void printBin(byte[] bytes){
        for (int i = 0; i < bytes.length; i++) {

            System.out.println(Integer.toBinaryString(bytes[i])+" "
                    +Integer.toBinaryString(bytes[i]&0xff)
                    +" "+Integer.toBinaryString(bytes[i]&0xff^0)
            );
        }
    }


    //字符码  表

    //
}
