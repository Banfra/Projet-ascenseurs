import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.*;

public class TestLoiPoisson extends JPanel{
    private static ArrayList<ArrayList<Integer>> testldp = new ArrayList<ArrayList<Integer>>();
    private static ArrayList<Integer> moyenne = new ArrayList<Integer>();
    private int nbExp = 1000;

    public TestLoiPoisson(){        
    }

    public void test(){
        for(int i=0; i<nbExp; i++){
            moyenne.add(0);
        }

        for(int j=0; j<nbExp; j++){
            ArrayList<Integer> array = new ArrayList<Integer>();
            ArrayList<Integer> counted = new ArrayList<Integer>();
            for(int i=0; i<1000; i++){
                LoiDePoisson ldp = new LoiDePoisson(0.5);
                array.add(ldp.getNombre());
            }

            for(int i=0; i<10; i++){
                counted.add(Collections.frequency(array, i));
            }

            System.out.println(counted);
            testldp.add(counted);
            for(int i=0; i<counted.size();i++) {
                int somme = moyenne.get(i);
                somme += counted.get(i);
                moyenne.set(i, somme);
            }
        }

        for (int i=0; i<moyenne.size(); i++) {
            moyenne.set(i, moyenne.get(i)/nbExp);
        }

    }

    public void paintComponent(Graphics g){
        for (ArrayList<Integer> ldp : testldp) {
            for(int i=0; i<ldp.size()-1;i++){
                g.setColor(Color.BLUE);
                g.drawLine(i*50, 500-(ldp.get(i))/2, (i+1)*50, 500-(ldp.get(i+1))/2);
            }

            for(int i=0; i<moyenne.size(); i++){
                g.setColor(Color.red);
                g.drawLine(i*50, 500, i*50, 500-(moyenne.get(i))/2);
                g.drawString(Integer.toString(moyenne.get(i)), i*50, 525);
            }
        }
    }

    
}
