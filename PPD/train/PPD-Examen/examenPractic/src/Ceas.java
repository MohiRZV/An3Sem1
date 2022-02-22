import java.io.IOException;

public class Ceas extends Thread {
    Integer timp;
    Writer writer;

    public Ceas(Writer writer) {
        this.timp = 0;
        this.writer = writer;
    }

    @Override
    public void run() {
        while (true) {
            timp++;
            try {
                writer.write("Este ceasul al " + timp + "-lea");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Este ceasul al " + timp + "-lea");
            synchronized (this) {
                this.notifyAll();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized Integer ceasul() {
        return timp;
    }
}
