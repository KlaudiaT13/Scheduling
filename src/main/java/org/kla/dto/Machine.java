package org.kla.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Comparator;

import static org.apache.commons.lang3.builder.ToStringStyle.NO_FIELD_NAMES_STYLE;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

public class Machine {
    private final int id;
    private double currentLoad = 0;
    private ArrayList<Task> tasks = new ArrayList<>();

    public Machine(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public double getCurrentLoad() {
        return currentLoad;
    }

    public void setCurrentLoad(double currentLoad) {
        this.currentLoad = currentLoad;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void addTask(Job job, JobType jobType) {
        Task task = Task.makeTask(job, jobType);
        tasks.add(task);
        currentLoad += task.completitionTime;

        if (jobType == JobType.TEST) {
            job.setTested();
        } else {
            job.setDone();
        }
    }

    public void addTask(Task task) {
        tasks.add(task);
        currentLoad += task.completitionTime;
    }

    public static class MachineComparator implements Comparator<Machine> {
        public int compare(Machine m1, Machine m2) {
            if (m1.currentLoad > m2.currentLoad)
                return 1;
            else if (m1.currentLoad < m2.currentLoad)
                return -1;
            return 0;
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("currentLoad", currentLoad)
                .append("tasks", tasks)
                .toString();
    }
}
