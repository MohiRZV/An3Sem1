package workers;

import com.mohi.Main;
import domain.Tranzactie;
import domain.TranzactieAcceptata;
import utils.FileIO;

import java.util.List;

public class Supervizor implements Runnable {
    List<Tranzactie> queueSupervizor;
    List<TranzactieAcceptata> queueMiner;
    int id;

    public Supervizor(List<Tranzactie> queueSupervizor, List<TranzactieAcceptata> queueMiner, int i) {
        this.queueSupervizor = queueSupervizor;
        this.queueMiner = queueMiner;
        this.id = i;
    }

    @Override
    public void run() {
        while (!Main.KILL) {
            try {
                consume();
                // add some delay
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Supervizorul " + id + " a plecat acasa!");

    }

    private void consume() throws InterruptedException {
        synchronized (queueSupervizor) {
            while (queueSupervizor.isEmpty() && !Main.KILL) {
                System.out.println("Supervizorul asteapta!");
                queueSupervizor.wait();
            }
        }
        synchronized (Main.KILL) {
            synchronized (queueSupervizor) {
                if (!Main.KILL) {
                    Tranzactie tranzactie = queueSupervizor.remove(0);
                    System.out.println("Tranzactia initiata de wallet " + tranzactie.getCodWalletUtilizator() + " a fost preluata de catre supervizorul " + id + "\t" + tranzactie);
                    queueSupervizor.notifyAll();
                    synchronized (Main.CERERI_FINALIZATE) {
                        Main.CERERI_FINALIZATE++;
                        if (Main.CERERI_FINALIZATE >= Main.N) {
                            Main.KILL = true;
                        }
                    }

                    synchronized (queueMiner) {
                        int buget = valid(tranzactie);
                        if(buget >= tranzactie.getValoareTranzactie()) {
                            queueMiner.add(new TranzactieAcceptata(tranzactie.getCodWalletUtilizator(), tranzactie.getValoareTranzactie(), tranzactie.getGetCodWalletDestinatar(), id));
                            queueMiner.notifyAll();
                            System.out.println("Tranzactia initiata de wallet "+tranzactie.getCodWalletUtilizator()+" cu valoarea "+tranzactie.getValoareTranzactie()+" catre wallet "+tranzactie.getGetCodWalletDestinatar()+" a fost acceptata de supervizorul "+id+", sold total inainte de operatie: "+buget);
                        } else {
                            System.out.println("Tranzactia initiata de wallet "+tranzactie.getCodWalletUtilizator()+" cu valoarea "+tranzactie.getValoareTranzactie()+" catre wallet "+tranzactie.getGetCodWalletDestinatar()+" a fost refuzata de supervizorul "+id+", sold total inainte de operatie: "+buget);
                            synchronized (Main.CERERI_FINALIZATE) {
                                Main.CERERI_FINALIZATE++;
                                if(Main.CERERI_FINALIZATE>=Main.N) {
                                    synchronized (Main.KILL) {
                                        Main.KILL = true;
                                    }
                                    synchronized (queueSupervizor) {
                                        queueSupervizor.notifyAll();
                                    }
                                    queueMiner.notifyAll();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private int valid(Tranzactie tranzactie) {
        List<TranzactieAcceptata> tranzactii = FileIO.readTranzactii();
        int disponibil = 0;
        int cheltuit = 0;

        for(TranzactieAcceptata tr : tranzactii) {
            if(tr.getCodWalletUtilizator()==tranzactie.getCodWalletUtilizator())
                cheltuit+=tr.getValoareTranzactie();
            if(tr.getGetCodWalletDestinatar()==tranzactie.getCodWalletUtilizator())
                disponibil+=tr.getValoareTranzactie();
        }

        return disponibil-cheltuit;
    }
}

