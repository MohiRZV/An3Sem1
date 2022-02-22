package com.mohi;

public class Main {

    static int func(int a, int b){
        if(b==0)
            return 1;
        int temp=func(a,b/2);
        if(b%2!=0)
            return temp*temp*a;
        else
            return temp*temp;
    }

    public static void main(String[] args) {
//        int n=841;
//        int x=1;
//        for(int i=0;i<50;i++){
//            int nx=(x+n/x)/2;
//            x=nx;
//        }
//        System.out.println(x);
        String input = "Bread$$##12.5$$##10";
        String[] chestii = input.split("\\$\\$##");
        System.out.println(chestii[0]);
        System.out.println(chestii[1]);
        System.out.println(chestii[2]);
    }
}
