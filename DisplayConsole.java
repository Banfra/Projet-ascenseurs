import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

//cette classe sert à l'affichage de l'immeuble dans la console via des tableaux
public class DisplayConsole extends Thread{
    private ArrayList<Ascenseur> lAscenseurs; //liste des ascenseurs de l'immeuble
    private ArrayList<ArrayList<String>> arrayToDisplay = new ArrayList<ArrayList<String>>(); //tableau à afficher
    private ArrayList<ArrayList<String>> oldarray = new ArrayList<ArrayList<String>>(); //ancien tableau
    //constructeur
    DisplayConsole(){}

    //initialisation
    public void init(){
        Immeuble immeuble = Immeuble.getInstance();//on récupère une instance de l'immeuble
        lAscenseurs = immeuble.getAscenseurs();//on récupère la liste des ascenseurs de l'immeuble 
        
        //initialisation du tableau à afficher
        for(int i = 2; i<immeuble.getNbEtages()*2+1; i++){ //pour chaque ligne du tableau à afficher (les étages sont séparés par des lignes vides)
            ArrayList<String> arr = new ArrayList<String>(); //ligne du tableau
            if(i%2 == 0){ //toutes les 2 lignes
                arr.add(String.valueOf(i/2-1));//on affiche le numéro de l'étage
                while(arr.size() != immeuble.getAscenseurs().size()*2+1){ //on remplit le tableau en fonction du nombre d'ascenseurs
                    if(arr.size() == 1){ //arr[1] servira à indiquer le nombre de personnes présentes à l'étage actuel
                        arr.add(String.valueOf(0));
                    }
                    else{
                        arr.add(String.valueOf(' ')); //on remplit le reste du tableau avec des espaces
                    }
                }
            }
            else{//les lignes intermédiaires sont remplis avec des espaces
                while(arr.size() != immeuble.getAscenseurs().size()*2+1){
                    arr.add(String.valueOf(' '));
                }
            }
            arrayToDisplay.add(arr); //on ajoute chaque tableau au tableau final qu'on affichera
        } 
    }

    //fonction d'affichage
    public void display(){
        Immeuble immeuble = Immeuble.getInstance(); //on récupère une instance de l'immeuble
        lAscenseurs = immeuble.getAscenseurs(); //on récupère la liste des ascenseurs
        CopyOnWriteArrayList<Personne> lPersonnes = Immeuble.getInstance().getListPersonnes();  //on récupère la liste des personnes de l'immeuble    

        //pour chaque ascenseur, on affiche dans le tableau le nombre de personnes qu'il contient
        for (Ascenseur ascenseur : lAscenseurs) {
            int etage = ascenseur.getEtageActuel();
            int id = ascenseur.getId();
            int nbPersonnes = ascenseur.getListPersonnes().size();
            
            arrayToDisplay.get(etage*2).set(id*2, String.valueOf(nbPersonnes));
        }

        //on affiche le nombre de personnes présentes à chaque étage
        //on utilise donc l'étage actuel des personnes
        for (Personne personne : lPersonnes) {
            if(personne.getDansAscenseur() == false){
                arrayToDisplay.get(personne.getEtageActuel()*2).set(1, String.valueOf(Integer.parseInt(arrayToDisplay.get(personne.getEtageActuel()*2).get(1))+1));
            }
        }
        
        //on affiche le tableau uniquement lorsqu'il est modifié, ainsi que la mesure de performance (voir Immeuble.java)
        if(!oldarray.equals(arrayToDisplay)){
            for(int i = arrayToDisplay.size()-1; i>=0; i--){
                System.out.println(arrayToDisplay.get(i));
            }
            System.out.println("Mesure de performance : " + Immeuble.getInstance().calculPerformance());
            System.out.println(" ");
        }
        //on efface l'ancien tableau et on en refait une copie du tableau actuel
        oldarray.clear();
        oldarray = new ArrayList<ArrayList<String>>(arrayToDisplay);
        for(int i =0; i<oldarray.size(); i++){
            oldarray.set(i, new ArrayList<String>(arrayToDisplay.get(i)));
        }

        //on efface le tableau pour que les valeurs ne s'incrémentent pas à l'infini
        for (Ascenseur ascenseur : lAscenseurs) {
            int etage = ascenseur.getEtageActuel();
            int id = ascenseur.getId();

            arrayToDisplay.get(etage*2).set(id*2, String.valueOf(' '));

            for (Personne personne : lPersonnes) {
                arrayToDisplay.get(personne.getEtageActuel()*2).set(1, String.valueOf(0));
            }

        }
    }

    //on lance le thread
    public void run(){
        while(true){
            display(); //on affiche
            try {
                int acceleration = Immeuble.getInstance().getAcceleration(); //on récupère le coefficient d'acceleration pour tester plus vite
                sleep(1000/acceleration); //on attend une seconde entre chaque affichage
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
