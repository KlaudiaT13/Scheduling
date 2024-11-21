import java.util.ArrayList;
//works for integers
public class Visualizer {
    int m; //number of machines
    ArrayList<Integer>[] distributed_jobs; //stores lengths of jobs distributed through all machines

    public Visualizer(int m, ArrayList<Integer>[] distributed_jobs) {
        this.m = m;
        this.distributed_jobs = distributed_jobs;
    }

    public void visualizeOneJob(int length) {
        for (int i = 0; i < length; i++) {
//            if(i == (length)/2){
//                System.out.println(length);
//            }
            System.out.print("_");
        }
        System.out.print("|");
    }

    public void visualizeOneMachine(int length, int machine_id) {
        System.out.print("|");
        for (int i = 0; i < length; i++) {
            visualizeOneJob(distributed_jobs[machine_id].get(i));
        }
        System.out.println();

    }

    public void visualizeEverything(){
        System.out.println("Start of visualization");
        for (int i = 0; i < distributed_jobs.length; i++) {
            visualizeOneMachine(distributed_jobs[i].size(), i);
        }
    }
}
