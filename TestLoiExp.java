import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;

import java.awt.*;

public class TestLoiExp extends JPanel{
    private static ArrayList<ArrayList<Double>> testloiexp = new ArrayList<ArrayList<Double>>();
    private static ArrayList<Double> moyenne = new ArrayList<Double>();
    private int nbExp = 1000;

    public TestLoiExp(){        
    }

    public void test(){
        for(int i=0; i<nbExp; i++){
            moyenne.add((double) 0);
        }

        for(int j=0; j<nbExp; j++){
            ArrayList<Double> array = new ArrayList<Double>();
            ArrayList<Double> counted = new ArrayList<Double>();
            for(int i=0; i<1000; i++){
                LoiExp loiExp = new LoiExp(60);
                array.add(loiExp.getNombre());
            }

            for(int i=0; i<10; i++){
                counted.add((double) Collections.frequency(array, i));
            }

            System.out.println(counted);
            testloiexp.add(counted);
            for(int i=0; i<counted.size();i++) {
                Double somme = moyenne.get(i);
                somme += counted.get(i);
                moyenne.set(i, somme);
            }
        }

        for (int i=0; i<moyenne.size(); i++) {
            moyenne.set(i, moyenne.get(i)/nbExp);
        }

    }

    public void paintComponent(Graphics g){
        for (ArrayList<Double> loiExp : testloiexp) {
            for(int i=0; i<loiExp.size()-1;i++){
                g.setColor(Color.BLUE);
                g.drawLine(i*50, 500-(loiExp.get(i).intValue())/2, (i+1)*50, 500-(loiExp.get(i+1).intValue())/2);
            }

            for(int i=0; i<moyenne.size(); i++){
                g.setColor(Color.red);
                g.drawLine(i*50, 500, i*50, 500-(moyenne.get(i).intValue())/2);
                g.drawString(Double.toString(moyenne.get(i)), i*50, 525);
            }
        }
    }
}
