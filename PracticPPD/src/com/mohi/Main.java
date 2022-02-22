package com.mohi;

import domain.Cerere;
import domain.CerereAcc;
import utils.FileIO;
import workers.Centralizator;
import workers.Cetatean;
import workers.Functionar;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static Integer N = 50;
    public static Integer M = 4;
    public static Boolean KILL = false;
    public static Integer DIM_COADA = 20;
    public static Integer CERERI_FINALIZATE = 0;
    public static Integer CERERI_APROBATE = 0;
    public static Integer CETATENI_FIN = 0;

    public static Integer LAST_CERERE_ID=1;

    public static Integer[] locuriInital = new Integer[]{5,7,10};
    public static Integer[] locuriOcupate = new Integer[]{0,0,0};

    public static void main(String[] args) {
        FileIO.nuke();
        List<Cerere> queueFunctionar = new ArrayList<>();
        List<CerereAcc> queueCentralizator = new ArrayList<>();

        // create functionari
        for(int i=1;i<=M;i++) {
            Functionar consumer = new Functionar(queueFunctionar, queueCentralizator, i);
            new Thread(consumer, "Consumer Thread "+i).start();
        }

        // create cetateni
        for(int i=1;i<=N;i++) {
            Cetatean producer = new Cetatean(queueFunctionar, i);
            new Thread(producer, "Producer Thread "+i).start();
        }

        // create centralizator
        Centralizator centralizator = new Centralizator(queueFunctionar, queueCentralizator);
        Thread cent = new Thread(centralizator, "Centralizator Thread");
        cent.start();
    }
}
