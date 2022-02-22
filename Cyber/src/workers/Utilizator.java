package workers;

import com.mohi.Main;
import domain.Tranzactie;

import java.util.List;
import java.util.Random;

public class Utilizator implements Runnable{
    List<Tranzactie> queueSupervizor;
    int id;
    public Utilizator(List<Tranzactie> queueSupervizor, int i) {
        this.queueSupervizor=queueSupervizor;
        this.id=i;
    }

    @Override
    public void run() {
        try {
            produce();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Tranzactie genTranzactie() {
        Random random = new Random();
        int val = random.nextInt(200)+1;
        int codWalletDest = random.nextInt(70);
        return new Tranzactie(id, val, codWalletDest);
    }

    private void produce() throws InterruptedException{
        Tranzactie tranzactie = genTranzactie();
        synchronized (queueSupervizor) {
            if (queueSupervizor.size() == Main.DIM_COADA) {
                System.out.println("Queue is full");
                queueSupervizor.wait();
            }
        }
        synchronized (queueSupervizor) {
            System.out.println("Utilizator "+id+": "+tranzactie);
            queueSupervizor.add(tranzactie);
            queueSupervizor.notifyAll();
        }
    }
}
