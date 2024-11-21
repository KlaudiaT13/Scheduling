import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Algorithmer {
    int m; // number of machines
    int n; // number of jobs
    double lambda;
    public static double PHI = 1.618;


    public Algorithmer(int m, int n, double lambda) {
        this.m = m;
        this.n = n;
        this.lambda = lambda;
    }
    //3. paper: non-preemptive setting
    public ArrayList<Double>[] greedyDouble(){
        ArrayList<Double>[] results = new ArrayList[m];

        return results;
    }


    public ArrayList<Integer>[] SBS(double tm, double lambda){

        List<Job> jobs = Generator.generateJobs(lambda, n);
        ArrayList<Machine> machines = new ArrayList<>();
        for(int i = 0; i < m; i++){
            machines.add(new Machine(i));
        }


        ArrayList<Integer> b = new ArrayList<>();
        ArrayList<Integer> s = new ArrayList<>();
        ArrayList<Integer> s1 = new ArrayList<>();
        ArrayList<Integer> s2 = new ArrayList<>();

        for(int i = 0; i < n; i++){
            if((jobs.get(i).getUpperLimit()/jobs.get(i).getTest()) >= tm){
                b.add(i);
            } else{
                s.add(i);
            }
        }
        //TODO did it sort right? should be from low to high
        jobs.sort(Comparator.comparing(Job::getTau));

        //take m jobs from s and place them in s1 (if there are enough)
        int tempm = m;
        for(int i = 0; i < s.size(); i++){
            if(tempm > 0){
                s1.add(s.get(i));
                tempm--;
            } else{
                s2.add(s.get(i));
            }
        }
        //step 5
        for(int i = 0; i < s1.size(); i++){
            machines.get(i);
        }








        return null;
    }

    double min(double a, double b){
        return Math.min(a, b);
    }

//    public int findLeastLoadedMachine(){
//
//    }

}