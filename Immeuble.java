import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
public class Immeuble implements Runnable{
    private int nbAscenseurs = 1;
    private int nbEtages = 7;
    private int acceleration = 50;
    private ArrayList<Ascenseur> lAscenseurs = new ArrayList<Ascenseur>();
    private CopyOnWriteArrayList<Personne> lPersonnes = new CopyOnWriteArrayList<Personne>();
    private CopyOnWriteArrayList<Long> lPerformances = new CopyOnWriteArrayList<Long>();
    private static Immeuble INSTANCE = null;


    public int getNbEtages(){
        return nbEtages;
    }
    public void setNbEtages(int nb){
        nbEtages = nb;
    }

    public ArrayList<Ascenseur> getAscenseurs(){
        return lAscenseurs;
    }

    public int getAcceleration(){
        return acceleration;
    }


    public CopyOnWriteArrayList<Personne> getListPersonnes(){
        return lPersonnes;
    }

    public void removePersonne(Personne personne){
        lPersonnes.remove(personne);
    }

    
    public CopyOnWriteArrayList<Long> getListPerformances(){
        return lPerformances;
    }

    public void addPerformance(Long performance){
        lPerformances.add(performance);
    }

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

    public static Immeuble getInstance() {
        return INSTANCE;
    }

    Immeuble(){
        INSTANCE = this;

        for(int i=0; i<nbAscenseurs;i++){
            lAscenseurs.add(new Ascenseur(i+1));
        }
        
    }

    @Override
    public void run() {
        while(true){
            // TODO Auto-generated method stub
            LoiDePoisson ldp = new LoiDePoisson(0.5);
            System.out.println("Nombre de nouvelles personnes : " + ldp.getNombre());
            for(int i = 0; i<ldp.getNombre(); i++){
                Personne personne = new Personne();
                Thread personneThread = new Thread(personne);
                personneThread.start();
                lPersonnes.add(personne);
                
            }
            try {
                Thread.sleep(60000/acceleration);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
