import java.util.Random;

//classe générant un nombre via une loi exponentielle
public class LoiExp{
    private double nombre; //nombre généré
    private double lambda; //paramètre lambda

    //constructeur générant un nombre
    public LoiExp(double lambda){
        this.lambda = lambda;
        nombre = rndloiExp(lambda);
    }

    //get nombre généré
    public double getNombre(){
        return nombre;
    }

    //si on veut généré un nouveau nombre
    public void newNumber(){
        nombre = rndloiExp(lambda);
    }

    //fonction générant un nombre
    public static double rndloiExp(double lambda) {
        Random rand = new Random();
        double nb = Math.log(1-rand.nextDouble())/(-lambda);
        return nb;
    }
}
