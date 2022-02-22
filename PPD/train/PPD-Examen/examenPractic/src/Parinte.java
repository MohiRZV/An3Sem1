import java.io.IOException;

public class Parinte extends Thread {
    Integer ordine;
    Ceas ceas;
    Integer delta;
    Writer writer;

    public Parinte(Integer ordine, Ceas ceas, Integer delta, Writer writer) {
        this.ordine = ordine;
        this.ceas = ceas;
        this.delta = delta;
        this.writer = writer;
    }

    @Override
    public void run() {
        while (true) {
            Integer timp = ceas.ceasul();
            if (timp != 0 && timp % delta == 0) {
                System.out.println("La timpul " + timp + " parintele " + ordine + " si-a hranit copilul");
                try {
                    writer.write("La timpul " + timp + " parintele " + ordine + " si-a hranit copilul");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            synchronized (ceas) {
                try {
                    ceas.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
