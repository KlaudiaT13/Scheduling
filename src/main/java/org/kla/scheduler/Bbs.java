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

public class Bbs implements Compute {
    int numberOfMachines; // number of machines
    int numberOfJobs; // number of jobs\
    public static double PHI = 1.618;
    List<JobTemplate> jobTemplates;
    double tm;

    public Bbs(int numberOfMachines, List<JobTemplate> jobTemplates) {
        this.numberOfMachines =numberOfMachines;
        this.numberOfJobs = jobTemplates.size();
        this.jobTemplates = jobTemplates;
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
        ArrayList<Job> b1 = new ArrayList<>();
        ArrayList<Job> b2 = new ArrayList<>();

        for(Job job: jobs) {
            if ((job.getRatio()) >= this.tm) {
                b.add(job);
            } else {
                s.add(job);
            }
        }

        b.sort(Comparator.comparing(Job::getTau));
        s.sort(Comparator.comparing(Job::getTau)); //<- here, difference from sbs, aside from different tm

        int tempm = numberOfMachines;
        for (int i = 0; i < b.size(); i++) {
            if (tempm > 0) {
                b1.add(b.get(i));
                tempm--;
            } else {
                b2.add(b.get(i));
            }
        }

        for (int i = 0; i < b1.size(); i++) {
            Machine machine = machinesq.poll();
            if (b1.get(i).getRatio() >= PHI) {
                machine.addTask(b1.get(i), JobType.TEST);
                machine.addTask(b1.get(i), JobType.REDUCED_TIME);
            } else {
                machine.addTask(b1.get(i), JobType.UPPER_LIMIT);
            }
            machinesq.add(machine);
        }

        for (int i = 0; i < b2.size(); i++) {
            Machine machine = machinesq.poll();
            machine.addTask(b2.get(i), JobType.TEST);
            machine.addTask(b2.get(i), JobType.REDUCED_TIME);
            machinesq.add(machine);
        }

        for (int i = 0; i < s.size(); i++) {
            Machine machine = machinesq.poll();
            machine.addTask(s.get(i), JobType.UPPER_LIMIT);
            machinesq.add(machine);
        }
       return new ComputationResult(new ArrayList<>(machinesq));
    }

    private double tmValue(){
        double m = (double) numberOfMachines;
        if(numberOfMachines == 2){
            return ((3 * PHI + 6 + Math.sqrt(45 * PHI + 213))/14);
        }else{
            return ((3 * m * PHI + 4 * m - 4)/(4 * m -1));
        }
    }

}