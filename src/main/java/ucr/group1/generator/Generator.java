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
        double randomNumber = random.nextDouble();
        return randomNumber;
    }

    public double getNormal(float mean, float variance){
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

    public double getRandomUniform(float min, float max){
        double X = random.nextDouble();
        X *= (max - min);
        X += min;
        return X;
    }

    public double getExponential(float mean){
        double r = random.nextDouble();
        double X = Math.log(r);
        X *= -1;
        X *= mean;
        return mean;
    }
}
