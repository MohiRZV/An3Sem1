package com.mohi;

public class Worker extends Thread {
    Cont c1;
    Worker(Cont c){
        this.c1=c;
    }

    @Override
//    synchronized
    public void run() {
        //synchronized (c1){}
//        synchronized(c1) {
        c1.depuneLEI();
//        }
        //c1.sold+=1;
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //synchronized (c1) {
        //c1.retrageLEI();
        //}
    }
}


