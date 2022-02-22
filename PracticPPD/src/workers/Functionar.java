package workers;

import com.mohi.Main;
import domain.Cerere;
import domain.CerereAcc;

import java.util.List;

public class Functionar implements Runnable {
    List<Cerere> queueFunctionar;
    List<CerereAcc> queueCentralizator;
    int id;

    public Functionar(List<Cerere> queueFunctionar, List<CerereAcc> queueCentralizator, int i) {
        this.queueFunctionar = queueFunctionar;
        this.queueCentralizator = queueCentralizator;
        this.id = i;
    }

    @Override
    public void run() {
        while (!Main.KILL) {
            try {
                preiaCerere();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("Functionarul " + id + " a plecat acasa!");
    }

    private synchronized int locuriDisponibile(Cerere cerere) {
        return Main.locuriInital[cerere.getIdentificatorParking() - 1] - Main.locuriOcupate[cerere.getIdentificatorParking() - 1];
    }

    private void preiaCerere() throws InterruptedException {
        Cerere cerere;
        synchronized (queueFunctionar) {
            while (queueFunctionar.isEmpty()) {
                System.out.println("Functionarul asteapta!");
                queueFunctionar.wait();
            }
            cerere = queueFunctionar.remove(0);
        }
        System.out.println("Functionarul " + id + " a preluat cererea cu codul " + cerere.getCodCerere() + " pentru Parkingul " + cerere.getIdentificatorParking());

        synchronized (Main.locuriOcupate) {
            int locuri = locuriDisponibile(cerere);
            if (locuri > 0) {
                Main.locuriOcupate[cerere.getIdentificatorParking() - 1]++;
                synchronized (queueCentralizator) {
                    if (queueCentralizator.size() == Main.DIM_COADA) {
                        System.out.println("Queue centralizator full");
                        queueCentralizator.wait();
                    }
                    queueCentralizator.add(new CerereAcc(cerere.getCodCerere(), cerere.getIdentificatorParking(), id));
                    synchronized (Main.CERERI_APROBATE) {
                        Main.CERERI_APROBATE++;
                    }
                    queueCentralizator.notifyAll();
                }
                System.out.println("Functionarul " + id + " a aprobat cererea cu codul " + cerere.getCodCerere() + " pentru Parkingul " + cerere.getIdentificatorParking() + " cu locuri disponibile " + locuri + " din totalul " + Main.locuriInital[cerere.getIdentificatorParking() - 1]);
            } else {
                System.out.println("Functionarul " + id + " a respins cererea cu codul " + cerere.getCodCerere() + " pentru Parkingul " + cerere.getIdentificatorParking() + " cu locuri disponibile " + locuri + " din totalul " + Main.locuriInital[cerere.getIdentificatorParking() - 1]);
            }
        }

        synchronized (queueFunctionar) {
            queueFunctionar.notifyAll();
        }

    }

}
