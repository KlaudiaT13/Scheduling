package org.kla.scheduler;

import org.kla.dto.*;

import java.util.*;

public class Algorithmer {
    int numberOfMachines; // number of machines
    int numberOfJobs; // number of jobs
    double lambda;
    public static double PHI = 1.618;
    List<JobTemplate> jobTemplates;
//    List<Machine> machines;


    public Algorithmer(int numberOfMachines, List<JobTemplate> jobTemplates, double lambda) {
        this.numberOfMachines =numberOfMachines;
        this.numberOfJobs = jobTemplates.size();
        this.lambda = lambda;
        this.jobTemplates = jobTemplates;
    }

    //3. paper: non-preemptive setting
    public ComputationResult greedyDouble() {
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

    //non-preemptive
    public ComputationResult sbs() {
        double tm = tmValue();
        List<Job> jobs = jobTemplates.stream()
                .map(Job::of)
                .toList();

//        ArrayList<Machine> machines = new ArrayList<>();
//        for(int i = 0; i < numberOfMachines; i++){
//            machines.add(new Machine(i));
//        }

        PriorityQueue<Machine> machinesq = new PriorityQueue<>(numberOfMachines, new Machine.MachineComparator());
//        machinesq.addAll(machines);

        for (int i = 0; i < numberOfMachines; i++) {
            machinesq.add(new Machine(i));
        }

        ArrayList<Job> b = new ArrayList<>();
        ArrayList<Job> s = new ArrayList<>();
        ArrayList<Job> s1 = new ArrayList<>();
        ArrayList<Job> s2 = new ArrayList<>();


        for(Job job: jobs) {
            if ((job.getRatio()) >= tm) {
                b.add(job);
            } else {
                s.add(job);
            }
        }
        /*for (int i = 0; i < numberOfJobs; i++) {
            if ((jobs.get(i).getRatio()) >= tm) {
                b.add(jobs.get(i));
            } else {
                s.add(jobs.get(i));
            }
        }*/
        //TODO did it sort right? should be from low to high
        s.sort(Comparator.comparing(Job::getTau)); //TODO try to restore random order in s2, maybe important

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

        Machine lastMachine = null;
        for (Machine m : machinesq) {
            lastMachine = m;
        }

        System.out.println("========= RESULT ");
        System.out.println(lastMachine.getCurrentLoad());

        return new ComputationResult(new ArrayList<>(machinesq));
    }

    //ToDo non-preemptive SBS uniform case (just change TM for TMuniform)




//    public int findLeastLoadedMachine(){
//
//    }


    private double tmValue(){
        double m = (double) numberOfMachines;
        return ((3 + Math.sqrt(5)) * m - 2 + Math.sqrt((38 + 6 * Math.sqrt(5)) * m * m - 4 * (11 + Math.sqrt(5)) * m + 12))/(6 * m - 2);
    }

    private double tmUniformValue(){
        double m = (double) numberOfMachines;
        return (2 * m - 1 + Math.sqrt(16 * m * m - 14 * m + 3)) / (2 * m);
    }

    public ComputationResult optimalOfflineAlg(){
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