import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

//classe main
public class Main {
    //fonction main du programme
    public static void main(String[] args) throws InterruptedException {
        int temps = 10; //temps de simulation
        Immeuble immeuble = new Immeuble(); //on crée un immeuble
        ArrayList<Ascenseur> ascenseurs = immeuble.getAscenseurs(); //on récupère les ascenseurs
        //pour chaque ascenseur, on crée un thread, on le lance et on le renomme
        for (Ascenseur ascenseur : ascenseurs) {
            Thread ascensceurThread = new Thread(ascenseur);
            ascensceurThread.start();
            ascensceurThread.setName("Ascenseur "+ascenseur);
        }

        //on crée un Thread pour l'immeuble et on le lance et le renomme
        Thread immeubleThread = new Thread(immeuble);
        immeubleThread.start();
        immeubleThread.setName("Immeuble");

        //on crée un thread pour l'affichage dans la console, on le lance et on le renomme
        DisplayConsole displayConsole = new DisplayConsole();
        displayConsole.init();
        displayConsole.start();
        displayConsole.setName("DisplayConsole");
        
        if(temps != 0){
            //on lance la simulation pendant 10 minutes
            LocalTime time = LocalTime.now();
            while(Duration.between(time, LocalTime.now()).toMinutes() != temps){ } //durée d'éxécution en minutes
            System.out.println("\n\n----- Fin du programme -----\n\n");
            System.exit(0);
        }
    }
}
