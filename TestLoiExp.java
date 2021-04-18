import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class TestLoiExp extends JPanel{
    ArrayList<Integer> counted = new ArrayList<Integer>();
    private int nbExp = 100;

    public TestLoiExp(){        
    }

    public void test(){
        // for(int i=0; i<nbExp; i++){
        //     moyenne.add((double) 0);
        // }

        for(int i=0; i<1000; i++){
            counted.add(0);
        }

        for(int j=0; j<nbExp; j++){
            ArrayList<Double> array = new ArrayList<Double>();
            for(int i=0; i<1000; i++){
                double lambda = 0.016666666666666;
                LoiExp loiExp = new LoiExp(lambda);
                array.add(loiExp.getNombre());
            }
            for(int i=0; i<array.size();i++){
                counted.set((int) Math.round(array.get(i)), counted.get((int)Math.round(array.get(i)))+1);
            }
        }
    }

    public void paintComponent(Graphics g){
        // for (ArrayList<Double> loiExp : testloiexp) {
        //     for(int i=0; i<loiExp.size()-1;i++){
        //         g.setColor(Color.BLUE);
        //         g.drawLine(i, 500-(loiExp.get(i).intValue())/2, (i+1), 500-(loiExp.get(i+1).intValue())/2);
        //     }

        //     // for(int i=0; i<moyenne.size(); i++){
        //     //     g.setColor(Color.red);
        //     //     g.drawLine(i, 500, i, 500-(moyenne.get(i).intValue())/2);
        //     //     g.drawString(Double.toString(moyenne.get(i)), i*50, 525);
        //     // }
        //     )
        // }

        for (int i=0; i<counted.size(); i++) {
            g.setColor(Color.red);
            g.drawLine(i, 500, i, 500-counted.get(i)/4);
            if(i%60 == 0){
                g.setColor(Color.blue);
                g.drawString(Integer.toString(i), i, 525);
            }
        }
    }
}
