import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class DisplayConsole extends Thread{
    private ArrayList<Ascenseur> lAscenseurs;
    private ArrayList<ArrayList<String>> arrayToDisplay = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> oldarray = new ArrayList<ArrayList<String>>();
    DisplayConsole(){}

    public void init(){
        Immeuble immeuble = Immeuble.getInstance();
        lAscenseurs = immeuble.getAscenseurs();
        
        for(int i = 2; i<immeuble.getNbEtages()*2+1; i++){
            ArrayList<String> arr = new ArrayList<String>();
            if(i%2 == 0){
                arr.add(String.valueOf(i/2-1));
                while(arr.size() != immeuble.getAscenseurs().size()*2+1){
                    if(arr.size() == 1){
                        arr.add(String.valueOf(0));
                    }
                    else{
                        arr.add(String.valueOf(' '));
                    }
                }
            }
            else{
                while(arr.size() != immeuble.getAscenseurs().size()*2+1){
                    arr.add(String.valueOf(' '));
                }
            }
            arrayToDisplay.add(arr);
        } 
    }

    public void display(){
        Immeuble immeuble = Immeuble.getInstance();
        lAscenseurs = immeuble.getAscenseurs();        

        for (Ascenseur ascenseur : lAscenseurs) {
            int etage = ascenseur.getEtageActuel();
            int id = ascenseur.getId();
            int nbPersonnes = ascenseur.getListPersonnes().size();
            CopyOnWriteArrayList<Personne> lPersonnes = Immeuble.getInstance().getListPersonnes();

            arrayToDisplay.get(etage*2).set(id*2, String.valueOf(nbPersonnes));
            for (Personne personne : lPersonnes) {
                arrayToDisplay.get(personne.getEtageActuel()*2).set(1, String.valueOf(Integer.parseInt(arrayToDisplay.get(personne.getEtageActuel()*2).get(1))+1));
            }

        }
        
        if(!oldarray.equals(arrayToDisplay)){
            for(int i = arrayToDisplay.size()-1; i>=0; i--){
                System.out.println(arrayToDisplay.get(i));
            }
            System.out.println("Mesure de performance : " + Immeuble.getInstance().calculPerformance());
            System.out.println(" ");
        }
        oldarray.clear();
        oldarray = new ArrayList<ArrayList<String>>(arrayToDisplay);
        for(int i =0; i<oldarray.size(); i++){
            oldarray.set(i, new ArrayList<String>(arrayToDisplay.get(i)));
        }

        for (Ascenseur ascenseur : lAscenseurs) {
            int etage = ascenseur.getEtageActuel();
            int id = ascenseur.getId();
            int nbPersonnes = ascenseur.getListPersonnes().size();
            CopyOnWriteArrayList<Personne> lPersonnes = Immeuble.getInstance().getListPersonnes();

            arrayToDisplay.get(etage*2).set(id*2, String.valueOf(' '));

            for (Personne personne : lPersonnes) {
                arrayToDisplay.get(personne.getEtageActuel()*2).set(1, String.valueOf(0));
            }

        }
    }

    public void run(){
        while(true){
            display();
            try {
                int acceleration = Immeuble.getInstance().getAcceleration();
                sleep(1000/acceleration);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
