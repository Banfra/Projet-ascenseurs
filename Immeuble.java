import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
//Cette classe représente l'immeuble dans son intégralité (Ascenseurs + Personnes)
public class Immeuble implements Runnable{
    private int nbAscenseurs = 3; //nombre d'ascenseurs
    private int nbEtages = 7; //nombre d'étages dans l'immeuble
    private int acceleration = 20; //coefficient d'acceleration afin d'augmenter la vitesse de simulation
    private ArrayList<Ascenseur> lAscenseurs = new ArrayList<Ascenseur>(); //liste des ascenseurs
    private CopyOnWriteArrayList<Personne> lPersonnes = new CopyOnWriteArrayList<Personne>(); //liste des personnes
    private CopyOnWriteArrayList<Long> lPerformances = new CopyOnWriteArrayList<Long>(); //liste des performances pour la mesure de performance
    private static Immeuble INSTANCE = null; //instance de l'immeuble pour le récupérer dans les autres classes

    //get et set nombre d'étages
    public int getNbEtages(){
        return nbEtages;
    }
    public void setNbEtages(int nb){
        nbEtages = nb;
    }

    //get liste des ascenseurs
    public ArrayList<Ascenseur> getAscenseurs(){
        return lAscenseurs;
    }

    //get le coefficient d'acceleration
    public int getAcceleration(){
        return acceleration;
    }

    // get liste des personnes
    public CopyOnWriteArrayList<Personne> getListPersonnes(){
        return lPersonnes;
    }
    //remove une personne de la liste
    public void removePersonne(Personne personne){
        lPersonnes.remove(personne);
    }

    //get liste des performances
    public CopyOnWriteArrayList<Long> getListPerformances(){
        return lPerformances;
    }
    //add une performance à la liste
    public void addPerformance(Long performance){
        lPerformances.add(performance);
    }

    //calcul de la performance : on fait la moyenne de toutes les performances obtenues pendant la simulation
    public long calculPerformance(){
        long somme = 0;
        for (Long performance : lPerformances) {
            somme += performance;
        }

        if(lPerformances.size() != 0){
            return somme/lPerformances.size();
        }
        else{
            return 0;
        }
    }

    //get instance de l'immeuble pour le récupérer dans les autres classes
    public static Immeuble getInstance() {
        return INSTANCE;
    }

    //constructeur
    Immeuble(){
        INSTANCE = this; //on veut pouvoir récupérer cet immeuble là et pas un autre
        //initialisation des ascenseurs de l'immeuble
        for(int i=0; i<nbAscenseurs;i++){
            lAscenseurs.add(new Ascenseur(i+1));
        }
        
    }

    //on lance le thread
    @Override
    public void run() {
        //toutes les minutes on :
        while(true){
            // TODO Auto-generated method stub
            LoiDePoisson ldp = new LoiDePoisson(0.5);//génère une valeur avec la loi de poisson
            System.out.println("Nombre de nouvelles personnes : " + ldp.getNombre());
            for(int i = 0; i<ldp.getNombre(); i++){ //pour chaque i de 0 à la valeur générée
                Personne personne = new Personne(); //on crée une personne
                Thread personneThread = new Thread(personne); //on crée thread de cette personne
                personneThread.start(); //on lance le thread de la personne
                personneThread.setName(personne.toString()); //on change le nom du thread avec le nom de la personne
                lPersonnes.add(personne); //on ajoute la personne à la liste des personnes
                
            }
            try {
                Thread.sleep(60000/acceleration); //on attend une minute
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
