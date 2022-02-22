package com.mohi;

import domain.Tranzactie;
import domain.TranzactieAcceptata;
import utils.FileIO;
import utils.Generator;
import workers.Miner;
import workers.Supervizor;
import workers.Utilizator;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static Integer N = 50;
    public static Integer M = 4;
    public static Boolean KILL = false;
    public static Integer DIM_COADA = 20;
    public static Integer CERERI_FINALIZATE = 0;

    public static void main(String[] args) {
        FileIO.nuke();
        Generator.initWallets();
        List<Tranzactie> queueSupervizor = new ArrayList<>();
        List<TranzactieAcceptata> queueMiner = new ArrayList<>();

        // create supervizori
        for(int i=1;i<=M;i++) {
            Supervizor consumer = new Supervizor(queueSupervizor, queueMiner, i);
            new Thread(consumer, "Consumer Thread "+i).start();
        }

        // create utilizator
        for(int i=1;i<=N;i++) {
            Utilizator producer = new Utilizator(queueSupervizor, i);
            new Thread(producer, "Producer Thread "+i).start();
        }

        // create miner
        Miner miner = new Miner(queueSupervizor, queueMiner);
        Thread min = new Thread(miner, "Miner Thread");
        min.start();
    }
}
