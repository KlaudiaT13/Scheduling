package org.kla.scheduler;

import org.kla.dto.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class UpgradeBbsUniform implements Compute {
    int numberOfMachines; // number of machines
    int numberOfJobs; // number of jobs
    public static double PHI = 1.618;
    List<JobTemplate> jobTemplates;
    double tm;


    public UpgradeBbsUniform(int numberOfMachines, List<JobTemplate> jobTemplates) {
        this.numberOfMachines =numberOfMachines;
        this.numberOfJobs = jobTemplates.size();
        this.jobTemplates = jobTemplates;
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

        b.sort(Comparator.comparing(Job::getTau)); //nonsense? should be maybe upper limit,
//        b.sort(Comparator.comparing(Job::));
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

        double estimatedTimeNotDividedYet;
        if(b2.isEmpty()){

            for (int i = 0; i < b1.size(); i++) {
                ArrayList<Job> b1copy = new ArrayList<Job>();
                for (int j = i + 1; j < b1.size(); j++) {
                    b1copy.add(b1.get(j));
                }
                estimatedTimeNotDividedYet = estimatedTime(b1copy, b2, s, true);
                Machine machine1 = machinesq.poll();
                Machine machine2 = machinesq.poll();


                if (b1.get(i).getRatio() >= PHI) {
                    Machine machine = whereToSchedule(machine1, machine2, estimatedTimeNotDividedYet, b1.get(i).getTest() + b1.get(i).getReducedComputationTime());
                    machine.addTask(b1.get(i), JobType.TEST);
                    machine.addTask(b1.get(i), JobType.REDUCED_TIME);
                } else {
                    Machine machine = whereToSchedule(machine1, machine2, estimatedTimeNotDividedYet, b1.get(i).getUpperLimit());
                    machine.addTask(b1.get(i), JobType.UPPER_LIMIT);
                }


                machinesq.add(machine1);
                machinesq.add(machine2);
            }
        } else{
            for (int i = 0; i < b1.size(); i++) {
                ArrayList<Job> b1copy = new ArrayList<Job>();
                for (int j = i + 1; j < b1.size(); j++) {
                    b1copy.add(b1.get(j));
                }
                estimatedTimeNotDividedYet = estimatedTime(b1copy, b2, s, false);

                Machine machine1 = machinesq.poll();
                Machine machine2 = machinesq.poll();
                Machine machine = whereToSchedule(machine1, machine2, estimatedTimeNotDividedYet, b1.get(i).getTest() + b1.get(i).getReducedComputationTime());

                machine.addTask(b1.get(i), JobType.TEST);
                machine.addTask(b1.get(i), JobType.REDUCED_TIME);

                machinesq.add(machine1);
                machinesq.add(machine2);
            }

            for (int i = 0; i < b2.size(); i++) {
                ArrayList<Job> b1copy = new ArrayList<Job>();
                ArrayList<Job> b2copy = new ArrayList<Job>();
                for (int j = i + 1; j < b2.size(); j++) {
                    b2copy.add(b2.get(j));
                }
                estimatedTimeNotDividedYet = estimatedTime(b1copy, b2copy, s, false);

                Machine machine1 = machinesq.poll();
                Machine machine2 = machinesq.poll();
                Machine machine = whereToSchedule(machine1, machine2, estimatedTimeNotDividedYet, b2.get(i).getTest() + b2.get(i).getReducedComputationTime());

                machine.addTask(b2.get(i), JobType.TEST);
                machine.addTask(b2.get(i), JobType.REDUCED_TIME);

                machinesq.add(machine1);
                machinesq.add(machine2);
            }
        }

        for (int i = 0; i < s.size(); i++) {
            ArrayList<Job> b1copy = new ArrayList<Job>();
            ArrayList<Job> b2copy = new ArrayList<Job>();
            ArrayList<Job> scopy = new ArrayList<Job>();
            for (int j = i + 1; j < s.size(); j++){
                scopy.add(s.get(j));
            }

            estimatedTimeNotDividedYet = estimatedTime(b1copy, b2copy, scopy, false);

            Machine machine1 = machinesq.poll();
            Machine machine2 = machinesq.poll();
            Machine machine = whereToSchedule(machine1, machine2, estimatedTimeNotDividedYet, s.get(i).getUpperLimit());

            machine.addTask(s.get(i), JobType.UPPER_LIMIT);

            machinesq.add(machine1);
            machinesq.add(machine2);
        }
        return new ComputationResult(new ArrayList<>(machinesq));
    }

    private double tmUniformValue(){
        double m = (double) numberOfMachines;
        if(numberOfMachines == 2){
            return ((9+3*Math.sqrt(37))/14);
        } else{
            return ((7*m-4 + Math.sqrt(97*m*m-68*m+16))/(2*(4*m-1)));
        }
    }

private Machine whereToSchedule(Machine machine1, Machine machine2, double estimatedTime, double timeOfNewTask){
    double estimatedOverhead = 0;
    double currentTime;
    if(machine1.getCurrentLoad()<machine2.getCurrentLoad()) {
        //here M2 has overhead
        currentTime = machine1.getCurrentLoad();
        ArrayList<Task> tasks = machine2.getTasks();
        double timeM2 = 0;
        for (Task task : tasks) {
            if (timeM2 <= currentTime) {
                timeM2 = timeM2 + task.getCompletitionTime();
            } else {
                if (task.getJobType().equals(JobType.TEST) || task.getJobType().equals(JobType.UPPER_LIMIT)) {
                    timeM2 = timeM2 + task.getCompletitionTime();
                } else {
                    timeM2 = timeM2 + task.getUpperLimit()/2;
                }
            }
        }
        estimatedOverhead = timeM2 - currentTime;
        double fractionOfOverhead = 0.5;
        //we can only use currentTime and timeM2, NOT getCurrentLoad
        double timeM1 = currentTime;
        double differenceIfTaskAddedToM2 = Math.abs(timeM2 + timeOfNewTask - timeM1);
        double differenceIfTaskAddedToM1 = Math.abs(timeM1 + timeOfNewTask - timeM2);
        //difference should be as close as possible to fractionOfOverhead * estimatedOverhead
        if(Math.abs(differenceIfTaskAddedToM2 - fractionOfOverhead * estimatedTime) < Math.abs(differenceIfTaskAddedToM1 - fractionOfOverhead * estimatedTime)){
            return machine2;
        } else{
            return machine1;
        }



    } else{
        currentTime = machine2.getCurrentLoad();
        ArrayList<Task> tasks = machine1.getTasks();
        double timeM1 = 0;
        for (Task task : tasks) {
            if (timeM1 <= currentTime) {
                timeM1 = timeM1 + task.getCompletitionTime();
            } else {
                if (task.getJobType().equals(JobType.TEST) || task.getJobType().equals(JobType.UPPER_LIMIT)) {
                    timeM1 = timeM1 + task.getCompletitionTime();
                } else {
                    timeM1 = timeM1 + task.getUpperLimit()/2;
                }
            }
        }
        estimatedOverhead = timeM1 - currentTime;
        double fractionOfOverhead = 0.25;
        //we can only use currentTime and timeM2, NOT getCurrentLoad
        double timeM2 = currentTime;
        double differenceIfTaskAddedToM2 = Math.abs(timeM2 + timeOfNewTask - timeM1);
        double differenceIfTaskAddedToM1 = Math.abs(timeM1 + timeOfNewTask - timeM2);
        //difference should be as close as possible to fractionOfOverhead * estimatedOverhead
        if(Math.abs(differenceIfTaskAddedToM2 - fractionOfOverhead * estimatedOverhead) < Math.abs(differenceIfTaskAddedToM1 - fractionOfOverhead * estimatedOverhead)){
            return machine2;
        } else{
            return machine1;
        }
    }

}

private double estimatedTime(ArrayList<Job> b1, ArrayList<Job> b2, ArrayList<Job> s, boolean b2empty){
        double time = 0;
        if(b2empty){
            for(Job job: b1){
                if(job.getRatio()>=PHI){time = time + 1 + job.getUpperLimit()/2;}
                else{time = time + job.getUpperLimit();}
            }
        } else{
            for(Job job: b1){
                time = time + 1 + job.getUpperLimit()/2;
            }
            for(Job job: b2){
                time = time + 1 + job.getUpperLimit()/2;
            }
        }
        for(Job job: s){
            time = time + job.getUpperLimit();
        }
        return time;
}

}