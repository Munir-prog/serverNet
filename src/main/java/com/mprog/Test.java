package com.mprog;

import com.mprog.utill.PropertiesUtil;

import java.util.Map;

public class Test {
    public static void main(String[] args) {
        var hello = new java.util.HashMap<>(Map.of("Hello", 1,
                "What", 22,
                "That", 3));


        var that = hello.remove(null);
        System.out.println(that);
        System.out.println(hello);
    }
}
