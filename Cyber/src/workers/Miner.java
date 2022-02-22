package workers;

import com.mohi.Main;
import domain.Tranzactie;
import domain.TranzactieAcceptata;
import utils.FileIO;

import java.util.List;

public class Miner implements Runnable{
    List<Tranzactie> queueSupervizor;
    List<TranzactieAcceptata> queueMiner;

    public Miner(List<Tranzactie> queueSupervizor, List<TranzactieAcceptata> queueMiner) {
        this.queueSupervizor = queueSupervizor;
        this.queueMiner = queueMiner;
    }
    @Override
    public void run() {
        while(!Main.KILL) {
            try {
                preiaTranzactie();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Minerul a plecat acasa!");
    }
    
    private void preiaTranzactie() throws InterruptedException {
        synchronized (queueMiner) {
            while (queueMiner.isEmpty()) {
                System.out.println("Minerul asteapta");
                queueMiner.wait();
            }
            if(!Main.KILL) {
                TranzactieAcceptata tranzactie = queueMiner.remove(0);
                System.out.println("Minerul a salvat tranzactia initiata de wallet " + tranzactie.getCodWalletUtilizator() + " cu valoarea " + tranzactie.getValoareTranzactie() + " catre wallet " + tranzactie.getGetCodWalletDestinatar() + ", validata de supervizorul " + tranzactie.getIdSupervizor());
                salveazaTranzactie(tranzactie);
            }
        }
    }

    private void salveazaTranzactie(TranzactieAcceptata tranzactie) {
        synchronized (Main.CERERI_FINALIZATE) {
            synchronized (queueMiner) {
                FileIO.writeTranzactii(tranzactie);
                queueMiner.notifyAll();
            }
            Main.CERERI_FINALIZATE++;
            if(Main.CERERI_FINALIZATE>=Main.N) {
                synchronized (Main.KILL) {
                    Main.KILL = true;
                }
                synchronized (queueSupervizor) {
                    queueSupervizor.notifyAll();
                }
            }
        }
    }
}
