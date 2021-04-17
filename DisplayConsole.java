import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class DisplayConsole extends Thread{
    private ArrayList<Ascenseur> lAscenseurs;
    private CopyOnWriteArrayList<Personne> lPersonnes;
    private ArrayList<ArrayList<String>> arrayToDisplay = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> oldarray = new ArrayList<ArrayList<String>>();
    DisplayConsole(){}

    public void init(){
        Immeuble immeuble = Immeuble.getInstance();
        lAscenseurs = immeuble.getAscenseurs();
        
        for(int i = 2; i<immeuble.getNbEtages()*2+2; i++){
            ArrayList<String> arr = new ArrayList<String>();
            if(i%2 == 0){
                arr.add(String.valueOf(i/2));
                while(arr.size() != immeuble.getAscenseurs().size()*2+1){
                    arr.add(String.valueOf(' '));
                }
                // for(int j = 0; j<immeuble.getAscenseurs().size(); j++){
                //     char[] str = {' ',' ',' '};
                //     for (char c : str) {
                //         arr.add(String.valueOf(c));
                //     }
                // }
            }
            else{
                while(arr.size() != immeuble.getAscenseurs().size()*2+1){
                    arr.add(String.valueOf(' '));
                }
                // for(int j = 0; j<immeuble.getAscenseurs().size(); j++){
                //     char[] str = {' ',' ',' ', ' '};
                //     for (char c : str) {
                //         arr.add(String.valueOf(c));
                //     }
                // }
            }
            arrayToDisplay.add(arr);
        }
        // for (ArrayList<String> arrayList : arrayToDisplay) {
        //     System.out.println(arrayList);
        // }

        // for(int i = arrayToDisplay.size()-1; i>=0; i--){
        //     System.out.println(arrayToDisplay.get(i));
        // }        
        
        
    }

    public void display(){
        Immeuble immeuble = Immeuble.getInstance();
        lAscenseurs = immeuble.getAscenseurs();
        lPersonnes = immeuble.getListPersonnes();
        

        for (Ascenseur ascenseur : lAscenseurs) {
            int etage = ascenseur.getEtageActuel();
            int id = ascenseur.getId();
            int nbPersonnes = ascenseur.getListPersonnes().size();
            arrayToDisplay.get(etage*2).set(id*2, String.valueOf(nbPersonnes));

        }
        
        if(!oldarray.equals(arrayToDisplay)){
            for(int i = arrayToDisplay.size()-1; i>=0; i--){
                System.out.println(arrayToDisplay.get(i));
            }
        }
        oldarray.clear();
        for (ArrayList<String> arraylist : arrayToDisplay) {
            oldarray.add(arraylist);
        }

        for (Ascenseur ascenseur : lAscenseurs) {
            int etage = ascenseur.getEtageActuel();
            int id = ascenseur.getId();
            int nbPersonnes = ascenseur.getListPersonnes().size();
            arrayToDisplay.get(etage*2).set(id*2, String.valueOf(' '));

        }
    }

    public void run(){
        while(true){
            display();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
