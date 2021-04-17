import java.util.Random;

public class LoiDePoisson {
    private int nombre;
    private double lambda;

    public LoiDePoisson(double lambda){
        this.lambda = lambda;
        nombre = rndpoiss(lambda);
    }

    public int getNombre(){
        return nombre;
    }

    public void newNumber(){
        nombre = rndpoiss(lambda);
    }

    public int rndpoiss(double lambda){
        int resultat = 0;
        Random random = new Random();
        double r = random.nextDouble();
        double expLambda = Math.exp(-lambda);
        while(r > expLambda){
            resultat++;
            r *= random.nextDouble();
        }
        return resultat;
    }
}
