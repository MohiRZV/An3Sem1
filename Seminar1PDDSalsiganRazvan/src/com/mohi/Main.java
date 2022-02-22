package com.mohi;

public class Main {

    private static void display(int v[]){
        for (int i = 0; i < v.length; i++) {
            System.out.print(v[i]+" ");
        }
    }

    public static void main(String[] args) throws InterruptedException {
	    //to be read
        int a[] = {1,2,3,4,5,6,7,8,9,10};
	    int b[] = {1,2,3,4,5,6,7,8,9,10};
	    int c[] = new int[10];


        System.out.println("H 1");
        int p = 5;

        Thread[] t = new Worker[p];

//	    Thread t = new Worker();
//	    t.run(); - do not call
//	    t.start();
//	    t.join();

        int start = 0, end = 0;
        int chunk = a.length / p;
        int rest = a.length % p;

        for (int i = 0; i < p; i++) {
            end = start+chunk;
            if(rest>0){
                rest--;
                end++;
            }
            t[i] = new Worker(i,a,b,c,p,start,end);
            t[i].start();
            start=end;
        }

        for (int i = 0; i < p; i++) {
            t[i].join();
        }

        display(c);


        System.out.println("\nH 2");

    }
}
