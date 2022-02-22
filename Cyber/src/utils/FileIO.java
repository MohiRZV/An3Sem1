package utils;

import domain.TranzactieAcceptata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileIO {
    private static String blockchainFile = "blockchain.txt";

    public static List<TranzactieAcceptata> readTranzactii() {
        List<TranzactieAcceptata> list = new ArrayList<>();
        try {
            File myObj = new File(blockchainFile);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] parts = data.split(",");
                list.add(new TranzactieAcceptata(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3])));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return list;
    }

    public static void writeTranzactii(TranzactieAcceptata tranzactie) {
        try {
            FileWriter myWriter = new FileWriter(blockchainFile, true);
            myWriter.write(tranzactie.getCodWalletUtilizator()+","+tranzactie.getValoareTranzactie()+","+tranzactie.getGetCodWalletDestinatar()+","+tranzactie.getIdSupervizor()+"\n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void nuke() {
        try {
            FileWriter myWriter = new FileWriter(blockchainFile, false);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
