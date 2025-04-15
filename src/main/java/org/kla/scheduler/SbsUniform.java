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

public class SbsUniform implements Compute {
    int numberOfMachines; // number of machines
    int numberOfJobs; // number of jobs
    double lambda;
    public static double PHI = 1.618;
    List<JobTemplate> jobTemplates;
    double tm;

    public SbsUniform(int numberOfMachines, List<JobTemplate> jobTemplates) {
        this.numberOfMachines =numberOfMachines;
        this.numberOfJobs = jobTemplates.size();
        this.jobTemplates = jobTemplates;
        this.lambda = 2;
        tm = tmUniformValue();
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
        ArrayList<Job> jobList = new ArrayList<>(jobs);
        jobList.sort(Comparator.comparing(Job::getUpperLimit).reversed());

        for(Job job : jobList) {
            Machine machine = machinesq.poll();
            if(job.getUpperLimit() >= this.tm){
                machine.addTask(job, JobType.TEST);
                machine.addTask(job, JobType.REDUCED_TIME);
            } else{
                machine.addTask(job, JobType.UPPER_LIMIT);
            }

            machinesq.add(machine);
        }
        return new ComputationResult(new ArrayList<>(machinesq));
    }

    private double tmUniformValue(){
        double m = (double) numberOfMachines;
        return (2 * m - 1 + Math.sqrt(16 * m * m - 14 * m + 3)) / (2 * m);
    }
}