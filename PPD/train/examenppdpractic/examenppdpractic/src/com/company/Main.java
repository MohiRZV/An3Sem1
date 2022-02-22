package com.company;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

class Cursant{
    public int id;
    public float medie;

    public Cursant(int id, float medie) {
        this.id = id;
        this.medie = medie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getMedie() {
        return medie;
    }

    public void setMedie(float medie) {
        this.medie = medie;
    }
}


public class Main {

    public static class LinkedList {
        private Node head;
        LinkedList() {
            head = null;
        }
        public static class Node{
            Cursant c;
            Node next;
            Node(Cursant curs) {
                this.c = curs;
                this.next = null;
            }
        }

        public void insereaza(Cursant c) {
            Node newNode = new Node(c);
            Node current = head;
            Node previous = null;
            //daca nu avem niciun nod introdus
            synchronized (this){
                if(current==null && previous==null){
                    head = newNode;
                    newNode.next = current;
                    return;
                }
            }
            while(current!=null){
                synchronized (current) {
                    previous = current;
                    current = current.next;
                }
            }
            //daca in lista e un singur nod
            if (previous == null) {
                synchronized (current) {
                    head = newNode;
                }
            }
            else {
                synchronized (previous) {
                    previous.next = newNode;
                    newNode.next = current;
                }
            }
        }

        public void afiseazaRezultateMaiMiciCa5(){
            synchronized (this) {
                Node current = head;
                while (current != null) {
                    if(current.c.medie<5)
                        System.out.println("Studentul "+ current.c.id +" are media " + current.c.medie);
                    current = current.next;
                }
            }
        }

        public void afiseazaTotiStudentii(){
            System.out.println("\n\n\nTOTI STUDENTII");
            Node current = head;
            while (current != null) {
                System.out.println("Studentul "+ current.c.id +" are media " + current.c.medie);
                current = current.next;
            }
        }

    }

    static class Catalog{
        boolean terminat;
        private LinkedList rezultat;
        boolean notificat = false;
        int a;
        Catalog(){
            rezultat = new LinkedList();
            terminat = false;
            a = 0;
        }
        public void afiseazaParticipantiiNoteMaiMiciDecat5(){
            rezultat.afiseazaRezultateMaiMiciCa5();
        }

        public void afiseazaTotiStudentii(){
            rezultat.afiseazaTotiStudentii();
        }

        public void adaugaCursantCatalog(Cursant cursant){
            rezultat.insereaza(cursant);
        }

        public boolean isNotificat() {
            return notificat;
        }

        public void setNotificat(boolean notificat) {
            this.notificat = notificat;
        }

        LinkedList getRezultat(){ return rezultat; }
        void setTerminat(boolean bool){ terminat = bool; }
        boolean isTerminat(){ return terminat; }
        void setA(int b) {
            a = b;
        }
        int getA() {
            return a;
        }
    }

    static class Secretara extends Thread{
        Catalog q;
        int start;
        int end;
        int numarTh;
        ArrayList<Cursant> cursanti;
        Secretara(Catalog q, ArrayList<Cursant> cursanti, int start, int end, int numarTh)
        {
            this.q = q;
            this.cursanti = cursanti;
            this.start = start;
            this.end = end;
            this.numarTh = numarTh;
        }

        public void run()
        {
            for(int i=start;i<end;i++){
                synchronized (q) {
                    q.adaugaCursantCatalog(cursanti.get(i));
                    if (cursanti.get(i).medie < 5.00) {
                        q.setNotificat(true);
                    }
                }
            }
            synchronized (q){
                q.setA(q.getA()+1);
            }
            if(q.getA()==numarTh)
                q.setTerminat(true);
        }
    }

    static class Manager extends Thread{
        Catalog q;
        Manager(Catalog q)
        {
            this.q = q;
        }

        public void run()
        {
            while(!q.isTerminat()) {
                synchronized (q) {
                    if(q.isNotificat()) {
                        System.out.println("\n\nSTUDENTI CU MEDII MAI MICI CA 5\n");
                        q.afiseazaParticipantiiNoteMaiMiciDecat5();
                        q.setNotificat(false);
                    }
                }
            }
            q.afiseazaTotiStudentii();
        }
    }

    private static ArrayList<Cursant> genereazaCursanti(){
        ArrayList<Cursant> cursanti = new ArrayList<>();
        Random r = new Random();
        for(int i = 1;i<=100;i++){
            Cursant c = new Cursant(i, (float)(r.nextInt(90)+10)/10);
            cursanti.add(c);
        }
        return cursanti;
    }

    public static void main(String[] args) {
        int numarThreaduri = 5;
        LinkedList lista = new LinkedList();
        ArrayList<Cursant> cursanti = genereazaCursanti();
        //cursanti.forEach(x -> System.out.println(x.id + " " + x.medie));
        ArrayList<Secretara> secretare = new ArrayList<>();
        Catalog c = new Catalog();
        int start, end, chunksize, rest;
        chunksize =  100 / numarThreaduri;
        rest = 100 % numarThreaduri;
        start = 0;
        end = chunksize;
        Manager m = new Manager(c);
        m.start();
        for(int i=0;i<numarThreaduri;i++) {
            if (rest > 0) {
                rest--;
                end++;
            }
            Secretara s = new Secretara(c, cursanti, start, end, numarThreaduri);
            secretare.add(s);
            s.start();
            start = end;
            end += chunksize;
        }
        for(int i=0;i<numarThreaduri;i++){
            try {
                secretare.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            m.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
