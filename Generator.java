import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generator {
    int n; // number of jobs to assign
    double fraction = 0.1; //for dependent testing times; fraction of upper limit TODO does 1/10 make sense?

    public Generator(int n) {
        this.n = n;
    }

     public static Job generateJob(double lambda){
        double upper_limit = generateUpperLimit(lambda);
        return new Job(upper_limit, generateReducedComputationTime(lambda, upper_limit), generateIndependentTest(lambda));
    }

    public static List<Job> generateJobs(double lambda, int n){
        List<Job> jobs = new ArrayList<>();
        for(int i = 0; i < n; i++){
            jobs.add(generateJob(lambda));
        }
        return jobs;
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

    public static double generateUpperLimit(double lambda) {
        return randomNumberInExponentialDistribution(lambda);
    }

    public static double generateIndependentTest(double lambda) {
        return randomNumberInExponentialDistribution(lambda);
    }

    public static double generateReducedComputationTime(double lambda, double upperLimit) {
        return Math.min(upperLimit, randomNumberInExponentialDistribution(lambda));
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

    public static double randomNumberInExponentialDistribution(double lambda) {
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

    public ArrayList<Integer> castToInteger(ArrayList<Double> list){
        int length = list.size();
        ArrayList<Integer> integers = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            integers.add(list.get(i).intValue());
        }
        return integers;
    }

}
