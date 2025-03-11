package org.kla.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ComputationResult {
    Double computationTime;
    List<Job> jobs;
    List<Machine> machines;

    public ComputationResult(List<Job> jobs, List<Machine> machines) {
        this.jobs = jobs;
        this.machines = machines;
        this.computationTime = calculateComputationTime(machines);
    }

    public ComputationResult(List<Machine> machines) {
        this.machines = machines;
        this.computationTime = calculateComputationTime(machines);
    }

    public Double getComputationTime() {
        return computationTime;
    }

    public List<Machine> getMachines() {
        return machines;
    }

    private Double calculateComputationTime(List<Machine> machines) {
        return machines.stream()
                .max((m1, m2) -> (new Machine.MachineComparator()).compare(m1, m2))
                .get().getCurrentLoad();
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this, SHORT_PREFIX_STYLE)
                .append("computationTime", computationTime)
                .append("jobs", jobs)
                .append("machines", machines)
                .toString();
    }
}
