import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Personne implements Runnable{
    private int etage_actuel;
    private int etage_arrivee;
    private String prenom;
    private File file = new File("./ressources/prenoms.txt");
    private LocalTime t_demandeAscenseur;
    private LocalTime t_arriveeEtage;

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
            scanner.close();
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
        t_demandeAscenseur = LocalTime.now();
        ascenseur.addEtageSuivant(etage_actuel);
        while(ascenseur.getEtageActuel() != etage_actuel){
            Thread.sleep(10);
        }
        ascenseur.setDoorsOpen(true);
    }

    public void dansAscenseur(Ascenseur ascenseur) throws InterruptedException{
        System.out.println(this + " dans Ascenseur " + ascenseur);
        ascenseur.addPersonne(this);
        ascenseur.addEtageSuivant(etage_arrivee);
        // System.out.println("Etage actuel : " + etage_actuel);
        while(ascenseur.getEtageActuel() != etage_arrivee){
            Thread.sleep(10);
            setEtageActuel(ascenseur.getEtageActuel());
        }
        setEtageActuel(etage_arrivee);
        System.out.println(this + " arrivée à l'étage " + etage_actuel);

        t_arriveeEtage = LocalTime.now();

        Long mesure_performance = Duration.between(t_demandeAscenseur, t_arriveeEtage).toMillis();
        mesure_performance = mesure_performance/1000*Immeuble.getInstance().getAcceleration();
        Immeuble.getInstance().addPerformance(mesure_performance);

        if(etage_arrivee == 0){
            Immeuble.getInstance().removePersonne(this);
        }
        ascenseur.removePersonne(this);
    }

    public Ascenseur choixAscenseur(){
        ArrayList<Ascenseur> ascenseurs = Immeuble.getInstance().getAscenseurs();
        Random rand = new Random();
        int number = rand.nextInt(ascenseurs.size());
        System.out.println(this + " Choisi l'ascenseur : "+ascenseurs.get(number));
        return ascenseurs.get(number);
    }

    public void run(){
        
        try {
            System.out.println(prenom+" arrive : Etage de départ : " + etage_actuel + "  Etage d'arrivée : " + etage_arrivee);
            Ascenseur ascenseur = choixAscenseur();
            demandeAscenseur(ascenseur);
            dansAscenseur(ascenseur);
            LoiExp loiexp = new LoiExp(0.016666666666666);
            System.out.println(this + " va travailler pendant " + (int)loiexp.getNombre() + " minutes");
            int acceleration = Immeuble.getInstance().getAcceleration();
            Thread.sleep((long) (loiexp.getNombre()*60000/acceleration));
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
}

    
