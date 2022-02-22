import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Dati numarul de parinti: ");
        Integer n = scanner.nextInt();
        Random random = new Random();
        Writer writer = new Writer();
        Ceas ceas = new Ceas(writer);

        Integer[] deltas = new Integer[n];
        for (int i = 0; i < n; i++) {
            System.out.print("deltas[" + i + "] = ");
            deltas[i] = scanner.nextInt();
        }

        ceas.start();
        Thread[] mama = new Thread[n];
        for (int i = 0; i < n; i++) {
            mama[i] = new Parinte(i, ceas, deltas[i], writer);
            mama[i].start();
        }

        for (int i = 0; i < n; i++)
            mama[i].join();
        ceas.join();
    }
}
