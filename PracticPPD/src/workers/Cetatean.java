package workers;

import com.mohi.Main;
import domain.Cerere;

import java.util.List;
import java.util.Random;

public class Cetatean implements Runnable{
    List<Cerere> queueFunctionar;
    Random random = new Random();
    int id;
    public Cetatean(List<Cerere> queueFunctionar, int i) {
        this.queueFunctionar=queueFunctionar;
        this.id=i;
    }

    @Override
    public void run() {
        try{
            cere();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private Cerere genCerere() {
        int parkingId = random.nextInt(3)+1;
        Cerere cerere;
        synchronized (Main.LAST_CERERE_ID) {
            cerere = new Cerere(Main.LAST_CERERE_ID, parkingId);
            Main.LAST_CERERE_ID++;
        }
        return cerere;
    }

    private void cere() throws InterruptedException {
        int nrCereri = random.nextInt(3)+1;
        for(int i=0;i<nrCereri;i++) {
            Cerere cerere = genCerere();
            synchronized (queueFunctionar) {
                if (queueFunctionar.size() == Main.DIM_COADA) {
                    System.out.println("Queue functionar full");
                    queueFunctionar.wait();
                }
            }
            synchronized (queueFunctionar) {
                queueFunctionar.add(cerere);
                System.out.println("Cetateanul "+id+" a initiat cererea cu codul "+cerere.getCodCerere()+" pentru Parkingul "+cerere.getIdentificatorParking());
                queueFunctionar.notifyAll();
            }
        }
        synchronized (Main.CETATENI_FIN) {
            Main.CETATENI_FIN++;
        }
    }
}
