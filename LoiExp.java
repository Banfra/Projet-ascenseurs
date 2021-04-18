import java.awt.Graphics;
import java.util.Random;


public class LoiExp{
    private double nombre;
    private double lambda;

    public LoiExp(double lambda){
        this.lambda = lambda;
        nombre = rndloiExp(lambda);
    }

    public double getNombre(){
        return nombre;
    }

    public void newNumber(){
        nombre = rndloiExp(lambda);
    }


    public static double rndloiExp(double lambda) {
        Random rand = new Random();
        double nb = Math.log(1-rand.nextDouble())/(-lambda);
        return nb;
    }

    public void paintComponent(Graphics g){

    }
}
