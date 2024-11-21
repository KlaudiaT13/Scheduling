import java.util.UUID;

public class Job {
    UUID id;
    double upperLimit;
    double reducedComputationTime;
    double test;
    double tau;
    double ratio;
    boolean tested = false;
    boolean done = false;

    public Job(double upperLimit, double reducedComputationTime, double test) {
        this.upperLimit = upperLimit;
        this.reducedComputationTime = reducedComputationTime;
        this.test = test;
        this.tau = Math.min(upperLimit, test);
        this.ratio = upperLimit / test;
        this.id = UUID.randomUUID();
    }

    public double getUpperLimit() {
        return upperLimit;
    }

    public double getReducedComputationTime() {
        return reducedComputationTime;
    }

    public double getTest() {
        return test;
    }

    public boolean isTested() {
        return tested;
    }

    public void setTested(boolean tested) {
        this.tested = tested;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public double getRatio() {
        return ratio;
    }

    public double getTau(){
        return tau;
    }

    public UUID getId() {
        return id;
    }
}
