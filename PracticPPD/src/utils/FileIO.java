package utils;

import domain.CerereAcc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileIO {
    private static String file = "condica.txt";

    public static List<CerereAcc> readEntities() {
        List<CerereAcc> list = new ArrayList<>();
        try {
            File myObj = new File(file);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] parts = data.split(",");
                list.add(new CerereAcc(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return list;
    }

    public static void writeCerere(CerereAcc entity) {
        try {
            FileWriter myWriter = new FileWriter(file, true);
            myWriter.write(entity.getCodCerere()+","+entity.getIdentificatorParking()+","+entity.getIdFunctionar()+"\n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void nuke() {
        try {
            FileWriter myWriter = new FileWriter(file, false);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
