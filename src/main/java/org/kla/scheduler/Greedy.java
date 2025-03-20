package org.kla.scheduler;

import org.kla.dto.ComputationResult;
import org.kla.dto.Job;
import org.kla.dto.JobTemplate;
import org.kla.dto.JobType;
import org.kla.dto.Machine;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Greedy implements Compute {
    int numberOfMachines; // number of machines
    int numberOfJobs; // number of jobs
    double lambda;
    public static double PHI = 1.618;
    List<JobTemplate> jobTemplates;
    double tm;

    public Greedy(int numberOfMachines, List<JobTemplate> jobTemplates) {
        this.numberOfMachines =numberOfMachines;
        this.numberOfJobs = jobTemplates.size();
        this.jobTemplates = jobTemplates;
        this.lambda = 2;
    }

    @Override
    public ComputationResult compute() {
        PriorityQueue<Machine> machinesq = new PriorityQueue<>(numberOfMachines, new Machine.MachineComparator());
        for (int i = 0; i < numberOfMachines; i++) {
            machinesq.add(new Machine(i));
        }

        List<Job> jobs = jobTemplates.stream()
                .map(Job::of)
                .toList();

        Machine leastLoaded;

        for(Job job: jobs){
            leastLoaded = machinesq.peek();
            if(job.getRatio()>=PHI){
                leastLoaded.addTask(job, JobType.TEST);
                leastLoaded.addTask(job, JobType.REDUCED_TIME);
            } else{
                leastLoaded.addTask(job, JobType.UPPER_LIMIT);
            }
        }

        return new ComputationResult(new ArrayList<>(machinesq));
    }

}