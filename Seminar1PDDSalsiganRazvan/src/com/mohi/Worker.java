package com.mohi;

public class Worker extends Thread{

    private int no;
    private int a[];
    private int b[];
    private int c[];
    private int p;
    private int start;
    private int end;

//    public Worker(int i, int a[], int b[], int c[], int p){
    public Worker(int i, int a[], int b[], int c[], int p, int start, int end){
        this.no = i;
        this.a = a;
        this.b = b;
        this.c = c;
        this.p = p;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        System.out.println("Hello "+getName()+" "+no);
//        for (int i = no; i < a.length; i+=p) {
//            c[i] = a[i]+b[i];
//        }
        for (int i = start; i < end; i++) {
            c[i] = a[i]+b[i];
//            c[i] = (int)Math.sqrt(Math.pow(a[i],4)+Math.pow(b[i],4));
        }

    }
}
