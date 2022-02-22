package workers;

import com.mohi.Main;
import domain.Cerere;
import domain.CerereAcc;
import utils.FileIO;

import java.util.List;

public class Centralizator implements Runnable {
    List<Cerere> queueFunctionar;
    List<CerereAcc> queueCentralizator;

    public Centralizator(List<Cerere> queueFunctionar, List<CerereAcc> queueCentralizator) {
        this.queueFunctionar = queueFunctionar;
        this.queueCentralizator = queueCentralizator;
    }

    @Override
    public void run() {
        while (!Main.KILL) {
            try {
                scrieCondica();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("Centralizatorul a plecat acasa!");
    }

    private void scrieCondica() throws InterruptedException {
        CerereAcc cerereAcc;
        synchronized (queueCentralizator) {
            while (queueCentralizator.isEmpty()) {
                System.out.println("Centralizatorul asteapta!");
                queueCentralizator.wait();
            }
            cerereAcc = queueCentralizator.remove(0);

            FileIO.writeCerere(cerereAcc);
            /*synchronized (Main.CERERI_FINALIZATE) {
                Main.CERERI_FINALIZATE++;
                if(Main.CETATENI_FIN==Main.N && Main.CERERI_FINALIZATE>=Main.CERERI_APROBATE)
                    Main.KILL=true;
            }*/

            System.out.println("Centralizatorul a salvat in condica cererea cu codul " + cerereAcc.getCodCerere() + " pentru parkingul " + cerereAcc.getIdentificatorParking());

            queueCentralizator.notifyAll();
        }
    }

}
