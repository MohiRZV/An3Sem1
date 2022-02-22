package com.mohi;

import java.math.BigInteger;
import java.util.ArrayDeque;

class Stack<E>{}

class A{
    static int x;
    public A(int x){}

    public void set(Integer x){}
    public void set(Float x){}
}

public class Main {
public static <T extends Number> T getObj(T arg){
    return arg;
}
    public static void main(String[] args) {
	// write your code here
        A a=new A(2);
        a.x = 100;
        int x = new Integer(1);
        new A(new Integer(1));
        Integer m = getObj(1);
        BigInteger x = getObj(BigInteger.valueOf(2));
    }
}
