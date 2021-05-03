import java.util.concurrent.CopyOnWriteArrayList;

//Cette classe représente un ascenseur, il est instancié dans la classe Immeuble
public class Ascenseur implements Runnable{
    private int id = 0;
    private int vitesseAscenseur = 10; //Secondes que met l'ascenseur pour 1 étage
    private Ordonnancement ordonnancement = Ordonnancement.SSTF; //Voir Ordonnancement.java pour plus de détails
    private boolean doorsOpen = true; //variable représentant l'ouverture des portes (true pour ouvertes, false pour fermées)
    private boolean moving = false; //variable permettant d'informer si l'ascenseur est en mouvement
    private Idle idle = Idle.INF; //Voir Idle.java pour plus de détails

    private int etage_actuel; //etage actuel
    private CopyOnWriteArrayList<Integer> etages_suivant = new CopyOnWriteArrayList<Integer>(); //Liste des prochains étages où l'ascenseur doit se rendre
    private CopyOnWriteArrayList<Personne> listPersonnes = new CopyOnWriteArrayList<Personne>(); //Liste des personnes dans l'ascenseur
    private CopyOnWriteArrayList<Personne> waitingPersonnes = new CopyOnWriteArrayList<Personne>(); //Liste des personnes qui attendent l'ascenseur

    //Constructeurs
    Ascenseur(){
        etage_actuel = 0;
    }
    Ascenseur(int id){
        etage_actuel = 0;
        this.id = id;
    }

    //get ID
    public int getId(){
        return id;
    }

    //get et set etage_actuel
    public int getEtageActuel(){
        return etage_actuel;
    }
    public void setEtageActuel(int etage){
        etage_actuel = etage;
    }

    //get l'étage suivant
    public int getEtageSuivant(Ordonnancement ordonnancement){
        if(ordonnancement == Ordonnancement.FCFS){ //First Come First Serve
            return etages_suivant.get(0); //retourne le premier éléments de la liste des étages suivants
        }
        else if(ordonnancement == Ordonnancement.SSTF){ //Shortest-Seek-Time-First
            //Ici on retourne l'étage le plus proche de l'étage actuel
            //(le premier si 2 étages sont à la même distance de l'étage actuel)
            int etage_choisi = -1;
            for (Integer etage : etages_suivant) {
                if(etage_choisi == -1 || Math.abs(etage - etage_actuel) < Math.abs(etage_choisi - etage_actuel)){
                    etage_choisi = etage;
                }
            }
            if(etage_choisi != -1){ //On ne veut pas retourner -1 sinon le programme bug
                return etage_choisi;
            }
        }
        return etage_actuel;
    }
    //On ajoute un étage dans la liste des étages suivants
    public void addEtageSuivant(int etage){
        etages_suivant.add(etage);
    }

    //get et set doorsOpen
    public boolean getDoorsOpen(){
        return doorsOpen;
    }
    public void setDoorsOpen(boolean bool){
        doorsOpen = bool;
    }

    //get liste des personnes dans l'ascenseur
    public CopyOnWriteArrayList<Personne> getListPersonnes(){
        return listPersonnes;
    }
    //add une personne dans l'ascenseur
    public void addPersonne(Personne personne){
        listPersonnes.add(personne);
    }
    //remove une personne de l'ascenseur
    public void removePersonne(Personne personne){
        listPersonnes.remove(personne);
    }

    //get liste des personnes qui attendent l'ascenseur
    public CopyOnWriteArrayList<Personne> getWaitingPersonnes(){
        return waitingPersonnes;
    }
    //add une personne qui attend l'ascenseur
    public void addWaitingPersonne(Personne personne){
        waitingPersonnes.add(personne);
    }
    //remove une personne qui attend l'ascenseur
    public void removeWaitingPersonne(Personne personne){
        waitingPersonnes.remove(personne);
    }

    //get moving
    public boolean getMoving(){
        return moving;
    }

    //get idle
    public Idle getIdle(){
        return idle;
    }

    //fonction permettant à l'ascenseur de se déplacer à l'étage passé en paramètre
    public void move(int etage) throws InterruptedException{
        this.moving = true; //on informe que l'ascenseur se déplace
        int acceleration = Immeuble.getInstance().getAcceleration();//vitesse de l'ascenseur
        setDoorsOpen(false); //fermeture des portes

        //tant qu'on n'est pas arrivé à l'étage indiqué
        while(etage != etage_actuel){
            //en Shortest-Seek-Time-First, on cherche à aller à l'étage le plus proche à tout moment
            if(ordonnancement == Ordonnancement.SSTF){
                int newEtage = getEtageSuivant(ordonnancement);
                if(newEtage != etage){
                    move(newEtage); //on se déplace à cet étage avant de se déplacer à l'étage passé en paramètre
                }
            }
            Thread.sleep(vitesseAscenseur*1000/acceleration); //l'ascenseur met un certain temps pour se déplacer
            if(etage > etage_actuel){
                etage_actuel++; //l'ascenseur monte
            }
            else if(etage < etage_actuel){
                etage_actuel--; //l'ascenseur descend
            }
            
        }
        
        //on enlève l'étage d'arrivée de la liste des étages suivants
        for (Integer thisetage : etages_suivant) {
            if(thisetage == etage){
                etages_suivant.remove(thisetage);
            }
        }

        setDoorsOpen(true); //ouverture des portes
        this.moving = false; //l'ascenseur ne bouge plus

        //on attend 100ms pour que le programme soit fluide,
        //et pour que les gens puissent monter dans l'ascenseur
        Thread.sleep(100);
    }

    //quand l'ascenseur n'a plus rien à faire, il est au ralenti (idle)
    public void idle() throws InterruptedException{
        switch(this.idle){
            case STAY: //reste à son étage actuel
                break;

            case INF: //descend d'un étage s'il n'est pas à l'étage 0
                if(etage_actuel > 0){
                    move(etage_actuel-1);
                }
                break;

            case SUP: //monte d'un étage s'il n'est pas au dernier étage
                if(etage_actuel < Immeuble.getInstance().getNbEtages()-1){
                    move(etage_actuel+1);
                }
                break;

            case MIDDLE: //se déplace à l'étage au milieu de l'immeuble
                move((int)Immeuble.getInstance().getNbEtages()/2);
                break;
            default:
                break;
        }
    }

    //on lance le thread
    public void run(){
        try {
            System.out.println("Ascenseur run()");
            while(true){
                if(etages_suivant.size() != 0){ //si la liste des étages suivants n'est pas vide
                    move(getEtageSuivant(ordonnancement));//on se déplace à l'étage suivant en fonction de l'ordonnancement de l'ascenseur
                    if(etages_suivant.size() == 0){ //si la liste redevient vide ensuite,
                        idle();//l'ascenseur est au ralenti
                    }
                }
            }
        } catch (InterruptedException e) { //gestion des exceptions
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "Ascenseur "+id;
    }
}
