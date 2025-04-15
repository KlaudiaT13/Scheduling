package org.kla.scheduler;

import org.kla.dto.ComputationResult;
import org.kla.dto.Job;
import org.kla.dto.JobTemplate;
import org.kla.dto.JobType;
import org.kla.dto.Machine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Sbs implements Compute {
    int numberOfMachines; // number of machines
    int numberOfJobs; // number of jobs
    double lambda;
    public static double PHI = 1.618;
    List<JobTemplate> jobTemplates;
    double tm;

    public Sbs(int numberOfMachines, List<JobTemplate> jobTemplates) {
        this.numberOfMachines =numberOfMachines;
        this.numberOfJobs = jobTemplates.size();
        this.jobTemplates = jobTemplates;
        this.lambda = 2;
        tm = tmValue();
    }

    @Override
    public ComputationResult compute() {
        List<Job> jobs = jobTemplates.stream()
                .map(Job::of)
                .toList();

        PriorityQueue<Machine> machinesq = new PriorityQueue<>(numberOfMachines, new Machine.MachineComparator());

        for (int i = 0; i < numberOfMachines; i++) {
            machinesq.add(new Machine(i));
        }

        ArrayList<Job> b = new ArrayList<>();
        ArrayList<Job> s = new ArrayList<>();
        ArrayList<Job> s1 = new ArrayList<>();
        ArrayList<Job> s2 = new ArrayList<>();

        for(Job job: jobs) {
            if ((job.getRatio()) >= this.tm) {
                b.add(job);
            } else {
                s.add(job);
            }
        }

        s.sort(Comparator.comparing(Job::getTau));

        //take numberOfMachines jobs from s and place them in s1 (if there are enough)
        int tempm = numberOfMachines;
        for (int i = 0; i < s.size(); i++) {
            if (tempm > 0) {
                s1.add(s.get(i));
                tempm--;
            } else {
                s2.add(s.get(i));
            }
        }
        //step 5
        for (int i = 0; i < s1.size(); i++) {
            Machine machine = machinesq.poll();
            if (s1.get(i).getRatio() >= PHI) {
                machine.addTask(s1.get(i), JobType.TEST);
                machine.addTask(s1.get(i), JobType.REDUCED_TIME);
            } else {
                machine.addTask(s1.get(i), JobType.UPPER_LIMIT);
            }
            machinesq.add(machine);
        }
        for (int i = 0; i < b.size(); i++) {
            Machine machine = machinesq.poll();
            machine.addTask(b.get(i), JobType.TEST);
            machine.addTask(b.get(i), JobType.REDUCED_TIME);
            machinesq.add(machine);
        }

        for (int i = 0; i < s2.size(); i++) {
            Machine machine = machinesq.poll();
            machine.addTask(s2.get(i), JobType.UPPER_LIMIT);
            machinesq.add(machine);
        }

        return new ComputationResult(new ArrayList<>(machinesq));
    }

    private double tmValue(){
        double m = (double) numberOfMachines;
        return ((3 + Math.sqrt(5)) * m - 2 + Math.sqrt((38 + 6 * Math.sqrt(5)) * m * m - 4 * (11 + Math.sqrt(5)) * m + 12))/(6 * m - 2);
    }
}