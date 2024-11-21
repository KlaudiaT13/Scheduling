import java.util.ArrayList;

public class Machine {
    private int id;
    double current_load;
    ArrayList<Job> jobs;

    public Machine(int id) {
        this.id = id;
        current_load = 0;
        jobs = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public double getCurrentLoad() {
        return current_load;
    }

    public void addLoad(double load) {
        current_load += load;
    }
}
