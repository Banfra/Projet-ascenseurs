import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

public class Main {
    
    public static void main(String[] args) throws InterruptedException {
        Immeuble immeuble = new Immeuble();
        ArrayList<Ascenseur> ascenseurs = immeuble.getAscenseurs();
        for (Ascenseur ascenseur : ascenseurs) {
            Thread ascensceurThread = new Thread(ascenseur);
            ascensceurThread.start();
        }

        

        Thread immeubleThread = new Thread(immeuble);
        immeubleThread.start();

        DisplayConsole displayConsole = new DisplayConsole();
        displayConsole.init();
        displayConsole.start();
        
        LocalTime time = LocalTime.now();
        while(Duration.between(time, LocalTime.now()).toMinutes() != 10){ } //durée d'éxécution en minutes
        System.out.println("\n\n----- Fin du programme -----\n\n");
        System.exit(0);
    }
}
