import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JPanel;

public class Personne extends JPanel implements Runnable{
    private int etage_actuel;
    private int etage_arrivee;
    private String prenom;
    private File file = new File("./ressources/prenoms.txt");

    Personne(){
        Random random = new Random();

        etage_actuel = 0;
        etage_arrivee = 0;
        while(etage_arrivee == 0){
            etage_arrivee = random.nextInt(Immeuble.getInstance().getNbEtages());
        }
        
        try {
            Scanner scanner = new Scanner(file);
            int rand = new Random().nextInt(209309);
            int c=0;
            String str = "";

            while(scanner.hasNextLine() && c!=rand){
                str = scanner.nextLine();
                c++;
            }
            
            prenom = str;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int getEtageActuel(){
        return etage_actuel;
    }
    public void setEtageActuel(int etage){
        etage_actuel = etage;
    }

    public int getEtageArrivee(){
        return etage_arrivee;
    }
    public void setEtageArrivee(int etage){
        etage_arrivee = etage;
    }

    public void demandeAscenseur(Ascenseur ascenseur) throws InterruptedException{
        System.out.println(this + " demande Ascenseur " + ascenseur);
        ascenseur.addEtageSuivant(etage_actuel);
        while(ascenseur.getEtageActuel() != etage_actuel){
            Thread.sleep(10);
        }
        ascenseur.setDoorsOpen(true);
    }

    public void dansAscenseur(Ascenseur ascenseur) throws InterruptedException{
        System.out.println(this + " dans Ascenseur " + ascenseur);
        ascenseur.addEtageSuivant(etage_arrivee);
        System.out.println("Etage actuel : " + etage_actuel);
        while(ascenseur.getEtageActuel() != etage_arrivee){
            Thread.sleep(10);
            setEtageActuel(ascenseur.getEtageActuel());
        }
        setEtageActuel(etage_arrivee);
        System.out.println(this + " arrivée à l'étage " + etage_actuel);

        if(etage_arrivee == 0){
            Immeuble.getInstance().removePersonne(this);
            ascenseur.removePersonne(this);
        }
    }

    public Ascenseur choixAscenseur(){
        ArrayList<Ascenseur> ascenseurs = Immeuble.getInstance().getAscenseurs();
        Random rand = new Random();
        int number = rand.nextInt(ascenseurs.size());
        System.out.println(this + " Choisi l'ascenseur : "+ascenseurs.get(number));
        ascenseurs.get(number).addPersonne(this);
        return ascenseurs.get(number);
    }

    public void run(){
        
        try {
            System.out.println(prenom+" arrive : Etage de départ : " + etage_actuel + "  Etage d'arrivée : " + etage_arrivee);
            Ascenseur ascenseur = choixAscenseur();
            demandeAscenseur(ascenseur);
            dansAscenseur(ascenseur);
            Thread.sleep(10000);
            etage_arrivee = 0;
            ascenseur = choixAscenseur();
            demandeAscenseur(ascenseur);
            dansAscenseur(ascenseur);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return prenom;
    }

    @Override
    public void paintComponent(Graphics g) {
        // TODO Auto-generated method stub
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int height = Display.getInstance().getHeight();
        int width = Display.getInstance().getWidth();

        g2.setColor(Color.BLUE);
        Random random = new Random();
        int ovalx = width/2;
        int ovaly = height-(etage_actuel+1)*(height-50)/Immeuble.getInstance().getNbEtages()-50;
        g2.fillOval(ovalx, ovaly, 10, 10);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
