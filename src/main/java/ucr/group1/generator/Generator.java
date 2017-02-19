package ucr.group1.generator;
import java.util.Random;

/**
 * Created by Daniel on 8/2/2017.
 */
public class Generator {
    private Random random;

    /*
     * Builds a new Generator
     */
    public Generator(){
        random = new Random();
    }

    /*
     * Returns a random real number between the interval [0,1[
     */
    public double getProbability(){
        return random.nextDouble();
    }

    /*
     * Returns a random real number based in the normal distribution with mean and variance as parameters
     */
    public double getNormal(double mean, double variance){
        double Z = 0;
        for(int i = 0; i < 12; i++){
            Z += random.nextDouble();
        }
        Z -= 6;
        double X = Math.sqrt(variance);
        X *= Z;
        X += mean;
        return X;
    }

    /*
     * Returns a random real number between the interval [min,max[
     */
    public double getRandomUniform(double min, double max){
        double X = random.nextDouble();
        X *= (max - min);
        X += min;
        return X;
    }

    /*
     * Returns a random real number based in the exponential distribution with mean as parameter
     */
    public double getExponential(double mean){
        double r = random.nextDouble();
        double X = Math.log(r);
        X *= -1;
        X *= mean;
        return X;
    }
}