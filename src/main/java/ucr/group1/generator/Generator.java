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

    /**
     * @return Random real number between the interval [0,1[
     */
    public double getProbability(){
        return random.nextDouble();
    }

    /**
     * @return          Random real number based in the normal distribution
     * @param mean      The mean of the normal distribution
     * @param variance  The variance of the normal distribution
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

    /**
     * @return      Random real number between an interval
     * @param min   The minimum value of the interval (inclusive)
     * @param max   The maximum value of the interval (exclusive)
     */
    public double getRandomUniform(double min, double max){
        double X = random.nextDouble();
        X *= (max - min);
        X += min;
        return X;
    }

    /**
     * @return      Random real number based in the exponential distribution
     * @param mean  The mean of the exponential distribution
     */
    public double getExponential(double mean){
        double r = random.nextDouble();
        double X = Math.log(r);
        X *= -1;
        X *= mean;
        return X;
    }
}