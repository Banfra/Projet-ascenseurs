import java.util.Random;

//classe générant des nombres avec la loi de poisson
public class LoiDePoisson {
    private int nombre; //nombre généré
    private double lambda; //paramètre lambda

    //constructeur, on génère un nombre
    public LoiDePoisson(double lambda){
        this.lambda = lambda;
        nombre = rndpoiss(lambda);
    }

    //get nombre généré
    public int getNombre(){
        return nombre;
    }

    //si on veut généré un nouveau nombre
    public void newNumber(){
        nombre = rndpoiss(lambda);
    }

    //fonction générant un nombre
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
