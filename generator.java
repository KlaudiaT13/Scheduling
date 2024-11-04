import java.util.ArrayList;
import java.util.Random;

public class generator {
    int m; //number of machines
    int n; // number of jobs to assign
    double fraction = 0.1; //for dependent testing times; fraction of upper limit TODO does 1/10 make sense?

    public generator(int m, int n) {
        this.m = m;
        this.n = n;
    }
    public ArrayList<Double> generateUpperLimits(int n, double lambda) {
        ArrayList<Double> upperLimits = batchRandomNumberInExponentialDistribution(n, lambda);
        return upperLimits;
    }

    public ArrayList<Double> generateIndependentTests(int n, double lambda) {
        ArrayList<Double> independentTests = batchRandomNumberInExponentialDistribution(n, lambda);
        return independentTests;
    }

    public ArrayList<Double> generateReducedComputationTimes(int n, double lambda, ArrayList<Double> upperLimits) {
        //TODO how to make sure that it's lower than corresponding upper limit? Should I replace too big value with upper Limit? (meaning: test couldn't reduce computation time)
        ArrayList<Double> computationTimes = batchRandomNumberInExponentialDistribution(n, lambda);
        for (int i = 0; i < n; i++) {
            if(upperLimits.get(i) < computationTimes.get(i)) {
                computationTimes.set(i, upperLimits.get(i));
            }
        }
        return computationTimes;
    }

    public ArrayList<Double> generateUniformTests(int n) {
        ArrayList<Double> testTimes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            testTimes.set(i, 1.0);
        }
        return testTimes;
    }

    //It's fraction of upper limit
    public ArrayList<Double> generateDependentTests(int n, double lambda, ArrayList<Double> upperLimits) {
        ArrayList<Double> dependentTests = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            dependentTests.set(i, upperLimits.get(i)*fraction);
        }
        return dependentTests;
    }

    public Double randomNumberInExponentialDistribution(double lambda) {
        Random rand = new Random();
        return Math.log(1-rand.nextDouble())/(-lambda);
    }

    public ArrayList<Double> batchRandomNumberInExponentialDistribution(int n, double lambda) {
        ArrayList<Double> batchNumbers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            batchNumbers.add(randomNumberInExponentialDistribution(lambda));
        }
        return batchNumbers;
    }

}
