import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

import java.awt.*;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
public class Immeuble extends JPanel implements Runnable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int nbAscenseurs = 2;
    private int nbEtages = 7;
    private ArrayList<Ascenseur> lAscenseurs = new ArrayList<Ascenseur>();
    private CopyOnWriteArrayList<Personne> lPersonnes = new CopyOnWriteArrayList<Personne>();
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

    public CopyOnWriteArrayList<Personne> getListPersonnes(){
        return lPersonnes;
    }

    public void removePersonne(Personne personne){
        lPersonnes.remove(personne);
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
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        int height = Display.getInstance().getHeight();
        int width = Display.getInstance().getWidth();

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.drawLine(50, 0, 50, height-50);
        g2.drawLine(width-50, 0, width-50, height-50);
        for(int i=0; i<nbEtages+1;i++){
            g2.drawLine(50, height-i*(height-50)/nbEtages -50, width-50, height-i*(height-50)/nbEtages -50);
            g2.drawString(String.valueOf(i), 25, height-i*(height-50)/nbEtages);
        }
        setOpaque(true);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
