import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Display implements Runnable{
    private int height = 600;
    private int width = 800;
    private static JFrame frame;
    // private static JPanel panel;
    private static Display INSTANCE = null;
    private ReentrantLock lock = new ReentrantLock();
    CopyOnWriteArrayList<Personne> lPersonnes;
    ArrayList<Ascenseur> lAscenseurs;

    Display(){}
    
    public void init(){
        frame = new JFrame();
        frame.setTitle("Simulation ascenseurs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        INSTANCE = this;
        lAscenseurs = Immeuble.getInstance().getAscenseurs();

        // createPanel();

    }

    public static Display getInstance(){
        return INSTANCE;
    }

    public int getHeight(){
        return height;
    }
    public int getWidth(){
        return width;
    }

    // public void createPanel() {
    //     panel = new JPanel();
    //     panel.setSize(800,500);
    //     panel.setBackground(Color.WHITE);
    //     frame.add(panel);
    // }

    public void DisplayElements(){
        try{
            lock.lock();
            lPersonnes = Immeuble.getInstance().getListPersonnes();

            for (Personne personne : lPersonnes) {
                frame.add(personne);
            }
            frame.revalidate();
            frame.repaint();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        finally{
            lock.unlock();
        }
    }

    public void DisplayImmeuble(){
        Immeuble immeuble = Immeuble.getInstance();
        // frame.add(immeuble);
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            DisplayImmeuble();
            while(true){
                DisplayElements();
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
