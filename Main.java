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
    }
}
