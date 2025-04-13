package org.kla.scheduler;

import org.kla.dto.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class TwoPhases implements Compute {
    int numberOfMachines; // number of machines
    int numberOfJobs; // number of jobs
    double lambda;
    public static double PHI = 1.618;
    List<JobTemplate> jobTemplates;

    public TwoPhases(int numberOfMachines, List<JobTemplate> jobTemplates) {
        this.numberOfMachines =numberOfMachines;
        this.numberOfJobs = jobTemplates.size();
        this.jobTemplates = jobTemplates;
        this.lambda = 2;

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

        ArrayList<Task> firstPhase = new ArrayList<>();
        ArrayList<Task> secondPhase = new ArrayList<>();

        for (Job job : jobs) {
            if(job.getTest()>job.getUpperLimit()){
                firstPhase.add(Task.makeTask(job, JobType.UPPER_LIMIT));
            } else{
                firstPhase.add(Task.makeTask(job, JobType.TEST));
                secondPhase.add(Task.makeTask(job, JobType.REDUCED_TIME));
            }
        }

        //Todo check if its decreasing
        firstPhase.sort(Comparator.comparing(Task::getCompletitionTime).reversed());
        secondPhase.sort(Comparator.comparing(Task::getCompletitionTime).reversed());

        for(Task task : firstPhase){
            Machine machine = machinesq.poll();
            machine.addTask(task);
            machinesq.add(machine);
        }

        ArrayList<Machine> machines = new ArrayList<>(machinesq);
        ComputationResult firstPhaseResult = new ComputationResult(machines);
        double firstPhaseComputationTime = firstPhaseResult.getComputationTime();
        for(Machine machine : machines){
            machine.setCurrentLoad(firstPhaseComputationTime);
        }

        PriorityQueue<Machine> machinesq2 = new PriorityQueue<>(numberOfMachines, new Machine.MachineComparator());

        machines.forEach(m -> machinesq2.add(m));

        for(Task task : secondPhase){
            Machine machine = machinesq2.poll();
            machine.addTask(task);
            machinesq2.add(machine);
        }

        ComputationResult secondPhaseResult = new ComputationResult(new ArrayList<>(machinesq2));
        return secondPhaseResult;

    }

}