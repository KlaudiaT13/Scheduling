package org.kla.scheduler;

import org.kla.dto.ComputationResult;
import org.kla.dto.Job;
import org.kla.dto.JobTemplate;
import org.kla.dto.JobType;
import org.kla.dto.Machine;

import java.util.ArrayList;
import java.util.List;

public class OptOffline implements Compute {
    int numberOfMachines; // number of machines
    int numberOfJobs; // number of jobs
    double lambda;
    public static double PHI = 1.618;
    List<JobTemplate> jobTemplates;
    double tm;

    public OptOffline(int numberOfMachines, List<JobTemplate> jobTemplates) {
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

        int numJobs = jobs.size();
        int currentToIterate = numJobs - 1;
        int[] array = new int[numJobs];
        for (int i = 0; i < numJobs; i++) {
            array[i] = 0;
        }
        ComputationResult currentBest = computeForGivenAssignment(array);


        while(array[0] != -1){
            array = iterateArray(array.length-1, array);
//            System.out.println(array);
            ComputationResult temp = computeForGivenAssignment(array);
            if(temp.getComputationTime() < currentBest.getComputationTime()){
                currentBest = temp;
            }
        }
        return currentBest;
    }

    private int[] iterateArray(int index, int[] array){
        array[index] = (array[index] + 1) % numberOfMachines;
        if(array[index] == 0){
            if(index == 0){
                array[index] = -1;
                return array;
            } else{
                return iterateArray(index - 1, array);
            }
        } else{
            return array;
        }
    }

    private ComputationResult computeForGivenAssignment(int[] assignment){
        List<Machine> machines = new ArrayList<>();
        List<Job> jobs = jobTemplates.stream()
                .map(Job::of)
                .toList();
        for(int i = 0; i < numberOfMachines; i++){
            machines.add(new Machine(i));
        }
        for(int i = 0; i < assignment.length; i++){
            if(jobs.get(i).getTest() + jobs.get(i).getReducedComputationTime()> jobs.get(i).getUpperLimit()){
                machines.get(assignment[i]).addTask(jobs.get(i), JobType.UPPER_LIMIT);
            } else{
                machines.get(assignment[i]).addTask(jobs.get(i), JobType.TEST);
                machines.get(assignment[i]).addTask(jobs.get(i), JobType.REDUCED_TIME);
            }
        }
        return new ComputationResult(machines);
    }
}