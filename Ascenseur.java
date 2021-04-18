import java.util.concurrent.CopyOnWriteArrayList;

public class Ascenseur implements Runnable{
    private int id = 0;
    private int vitesseAscenseur = 10; //Secondes que met l'ascenseur pour 1 étage
    private Ordonnancement ordonnancement = Ordonnancement.FCFS;
    private boolean doorsOpen = false;
    private Idle idle = Idle.STAY;

    private int etage_actuel;
    private CopyOnWriteArrayList<Integer> etages_suivant = new CopyOnWriteArrayList<Integer>();
    private CopyOnWriteArrayList<Personne> listPersonnes = new CopyOnWriteArrayList<Personne>();

    Ascenseur(){
        etage_actuel = 0;
    }
    Ascenseur(int id){
        etage_actuel = 0;
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public int getEtageActuel(){
        return etage_actuel;
    }
    public void setEtageActuel(int etage){
        etage_actuel = etage;
    }

    public int getEtageSuivant(Ordonnancement ordonnancement){
        if(ordonnancement == Ordonnancement.FCFS){
            return etages_suivant.get(0);
        }
        else if(ordonnancement == Ordonnancement.SSTF){
            int etage_choisi = -1;
            for (Integer etage : etages_suivant) {
                if(Math.abs(etage - etage_actuel) < Math.abs(etage_choisi - etage_actuel) || etage_choisi == -1){
                    etage_choisi = etage;
                }
            }
            return etage_choisi;
        }
        return etage_actuel;
    }
    public void addEtageSuivant(int etage){
        etages_suivant.add(etage);
    }

    public boolean getDoorsOpen(){
        return doorsOpen;
    }
    public void setDoorsOpen(boolean bool){
        doorsOpen = bool;
    }

    public CopyOnWriteArrayList<Personne> getListPersonnes(){
        return listPersonnes;
    }

    public void addPersonne(Personne personne){
        listPersonnes.add(personne);
    }

    public void removePersonne(Personne personne){
        listPersonnes.remove(personne);
    }

    public Idle getIdle(){
        return idle;
    }

    public void move(int etage) throws InterruptedException{
        doorsOpen = false;
        while(etage != etage_actuel){
            if(ordonnancement == Ordonnancement.SSTF){
                int newEtage = getEtageSuivant(ordonnancement);
                if(newEtage != etage){
                    move(newEtage);
                }
            }
            int acceleration = Immeuble.getInstance().getAcceleration();
            Thread.sleep(vitesseAscenseur*1000/acceleration);
            if(etage > etage_actuel){
                etage_actuel++;
            }
            else if(etage < etage_actuel){
                etage_actuel--;
            }
            // System.out.println("Etage actuel ascenseur : "+ etage_actuel);

            for (Integer thisetage : etages_suivant) {
                if(thisetage == etage_actuel){
                    // System.out.println("Remove etage "+ thisetage);
                    etages_suivant.remove(thisetage);
                }
            }
            
        }

        for (Integer thisetage : etages_suivant) {
            if(thisetage == etage){
                // System.out.println("Remove etage "+ thisetage);
                etages_suivant.remove(thisetage);
            }
        }

        doorsOpen = true;

        
        
    }

    public void idle() throws InterruptedException{
        switch(idle){
            case STAY:
                break;

            case INF:
                if(etage_actuel > 0){
                    move(etage_actuel-1);
                }
                break;

            case SUP:
                if(etage_actuel < Immeuble.getInstance().getNbEtages()-1){
                    move(etage_actuel+1);
                }
                break;

            case MIDDLE:
                move((int)Immeuble.getInstance().getNbEtages()/2);
                break;
            default:
        }
    }

    public void run(){
        try {
            System.out.println("Ascenseur run()");
            while(true){
                if(etages_suivant.size() != 0){
                    // System.out.println("Move");
                    move(getEtageSuivant(ordonnancement));
                    if(etages_suivant.size() == 0){
                        idle();
                    }
                }
            }
        } catch (InterruptedException e) {
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
