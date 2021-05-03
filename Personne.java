import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

//classe représentant une personne
public class Personne implements Runnable{
    private int etage_actuel; //etage actuel
    private int etage_arrivee; //etage d'arrivée
    private String prenom; //prenom de la personne
    private File file = new File("./ressources/prenoms.txt"); //fichiers de prénoms
    private LocalTime t_demandeAscenseur; //temps lorsque la personne demande un ascenseur
    private LocalTime t_arriveeEtage; //temps lorsque la personne arrive à un étage
    private boolean dansAscenseur = false; //variable indiquand si la persone est dans un ascenseur

    //constructeur
    Personne(){
        Random random = new Random();

        etage_actuel = 0; //la personne arrive à l'étage 0
        etage_arrivee = 0;
        //l'étage d'arrivée est défini aléatoirement
        while(etage_arrivee == 0){
            etage_arrivee = random.nextInt(Immeuble.getInstance().getNbEtages());
        }
        
        try {
            //on génère un prénom pour la personne grace au fichier de prenoms
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

    //get et set etage_actuel
    public int getEtageActuel(){
        return etage_actuel;
    }
    public void setEtageActuel(int etage){
        etage_actuel = etage;
    }

    //get et set etage_arrivee
    public int getEtageArrivee(){
        return etage_arrivee;
    }
    public void setEtageArrivee(int etage){
        etage_arrivee = etage;
    }

    //get dansAscenseur
    public boolean getDansAscenseur(){
        return this.dansAscenseur;
    }

    //La personne demande un ascenseur
    public void demandeAscenseur(Ascenseur ascenseur) throws InterruptedException{
        System.out.println(this + " demande Ascenseur " + ascenseur);
        t_demandeAscenseur = LocalTime.now(); //premier temps pour la mesure de performance
        ascenseur.addEtageSuivant(etage_actuel);//la personne appuye sur le bouton pour appeler l'ascenseur
        ascenseur.addWaitingPersonne(this);//on ajoute la personne à la liste des personnes qui attendent l'ascenseur
        //on attend que l'ascenseur arrive à l'étage et ouvre ses portes
        while(ascenseur.getEtageActuel() != etage_actuel || (ascenseur.getDoorsOpen() == false && ascenseur.getEtageActuel() == etage_actuel)){
            Thread.sleep(10);
        }
    }

    //la personne rentre dans l'ascenseur
    public void dansAscenseur(Ascenseur ascenseur) throws InterruptedException{
        //lorsqu'il y a plusieurs personnes, elles rentrent dans l'ordre dans lequel elles sont arrivées
        for (Personne personne : ascenseur.getWaitingPersonnes()) {
            if(personne == this){
                ascenseur.removeWaitingPersonne(personne);
                break;
            }
            else if(personne != this && personne.getEtageActuel() == ascenseur.getEtageActuel()){;
                System.out.println(personne + " wait");
                Thread.sleep(10);
            }
        }
        System.out.println(this + " dans Ascenseur " + ascenseur);
        this.dansAscenseur = true; //on insique que la personne est dans l'ascenseur
        ascenseur.addPersonne(this); //on ajoute la personne dans la liste des personnes présentes dans l'ascenseur
        ascenseur.addEtageSuivant(etage_arrivee); //on ajoute l'étage d'arrivée dans les prochains étages de l'ascenseur
        //on attend que l'ascenseur arrive à l'étage d'arrivée de la personne et ouvre ses portes
        while(ascenseur.getEtageActuel() != etage_arrivee || (ascenseur.getDoorsOpen() == false && ascenseur.getEtageActuel() == etage_arrivee)){
            Thread.sleep(10);
            setEtageActuel(ascenseur.getEtageActuel());
        }
        setEtageActuel(etage_arrivee); //on actualise l'étage de la personne
        System.out.println(this + " arrivée à l'étage " + etage_actuel);

        t_arriveeEtage = LocalTime.now(); //temps d'arrivée pour le calcul de la mesure

        //Calcul de la mesure de performance :
        //on calcule la différence entre le temps de la demande de l'ascenseur et celui de l'arrivée à destination
        Long mesure_performance = Duration.between(t_demandeAscenseur, t_arriveeEtage).toMillis();
        mesure_performance = mesure_performance/1000*Immeuble.getInstance().getAcceleration();
        Immeuble.getInstance().addPerformance(mesure_performance); //et on l'ajoute à la liste des performances de l'immeuble
    
        ascenseur.removePersonne(this); //la personne sort de l'ascenseur
        this.dansAscenseur = false;

        //si la personne revient à l'étage 0, elle sort de l'immeuble
        if(etage_arrivee == 0){
            Immeuble.getInstance().removePersonne(this);
        }
    }

    //fonction de choix d'ascenseur
    public Ascenseur choixAscenseur(){
        Ascenseur ascenseur_choisi = new Ascenseur(); //initialisation de la variable ascenseur_choisi
        ArrayList<Ascenseur> ascenseurs = Immeuble.getInstance().getAscenseurs(); //on récupère la liste des ascenseurs
        ArrayList<Ascenseur> ascenseurs_disponibles = new ArrayList<Ascenseur>(); //on crée une liste des ascenseurs disponibles (ceux qui ne se déplace pas)

        //si un ascenseur ne se déplace pas, on l'ajoute à la listes des ascenserus disponibles
        for (Ascenseur ascenseur : ascenseurs) {
            if(ascenseur.getMoving() == false){
                ascenseurs_disponibles.add(ascenseur);
            }
        }

        //S'il y a des ascenseurs disponibles, on appelle celui qui est le plus proche de l'étage de la personne
        if(ascenseurs_disponibles.size() != 0){
            int etage_ascenseur_choisi = -1;
            for (Ascenseur ascenseur : ascenseurs_disponibles) {
                if(etage_ascenseur_choisi < 0 || Math.abs(etage_ascenseur_choisi - this.etage_actuel) > Math.abs(ascenseur.getEtageActuel() - this.etage_actuel)){
                    etage_ascenseur_choisi = ascenseur.getEtageActuel();
                    ascenseur_choisi = ascenseur;
                }
            }
        }
        //sinon on en choisi un aléatoirement
        else{
            Random rand = new Random();
            int number = rand.nextInt(ascenseurs.size());
            ascenseur_choisi = ascenseurs.get(number);
        }
        System.out.println(this + " Choisi l'ascenseur : "+ascenseur_choisi);
        return ascenseur_choisi;
    }

    //on lance le thread
    public void run(){
        //la personne va se comporter de cette façon :
        try {
            System.out.println(prenom+" arrive : Etage de départ : " + etage_actuel + "  Etage d'arrivée : " + etage_arrivee);
            Ascenseur ascenseur = choixAscenseur();//choisi un ascenseur
            demandeAscenseur(ascenseur); //demande l'ascenseur choisi
            dansAscenseur(ascenseur); //monte dans l'ascenseur choisi

            //la personne va attendre un temps aléatoire généré via une loi exponentielle
            LoiExp loiexp = new LoiExp(0.016666666666666);
            System.out.println(this + " va travailler pendant " + (int)loiexp.getNombre() + " minutes");
            int acceleration = Immeuble.getInstance().getAcceleration();
            Thread.sleep((long) (loiexp.getNombre()*60000/acceleration));

            //quand la personne a fini de travailler, elle veut revenir à l'étage 0
            etage_arrivee = 0;
            ascenseur = choixAscenseur(); //choisi un ascenseur
            demandeAscenseur(ascenseur); //demande l'ascenseur choisi
            dansAscenseur(ascenseur); //monte dans l'ascenseur choisi
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

    
