import java.util.ArrayList;

public class Summarizer {
    int m; //number of machines
    int n; //number of jobs
    ArrayList[] results;

    public Summarizer(int m, int n) {
        this.m = m;
        this.n = n;
    }

    public void getResults(ArrayList[] results) {
        this.results = results;
    }

}
