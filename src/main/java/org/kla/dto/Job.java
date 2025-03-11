package org.kla.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

public class Job {
    private final UUID id;
    private final double upperLimit;
    private final double reducedComputationTime;
    private final double test;
    private final double tau;
    private final double ratio;
    private boolean tested = false;
    private boolean done = false;

    public Job(double upperLimit, double reducedComputationTime, double test) {
        this.upperLimit = upperLimit;
        this.reducedComputationTime = reducedComputationTime;
        this.test = test;
        this.tau = Math.min(upperLimit, test);
        this.ratio = upperLimit / test;
        this.id = UUID.randomUUID();
    }

    public Job(JobTemplate jobTemplate) {
        this.id = jobTemplate.getId();
        this.upperLimit = jobTemplate.getUpperLimit();
        this.reducedComputationTime = jobTemplate.getReducedComputationTime();
        this.test = jobTemplate.getTest();
        this.tau = Math.min(upperLimit, test);
        this.ratio = upperLimit / test;
    }

    public static Job of(JobTemplate jobTemplate) {
        return new Job(jobTemplate);
    }

    public double getUpperLimit() {
        return upperLimit;
    }

    public double getReducedComputationTime() {
        return reducedComputationTime;
    }

    public double getTest() {
        return test;
    }

    public boolean isTested() {
        return tested;
    }

    public void setTested() {
        this.tested = true;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone() {
        this.done = true;
    }

    public double getRatio() {
        return ratio;
    }

    public double getTau() {
        return tau;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("upperLimit", upperLimit)
                .append("reducedComputationTime", reducedComputationTime)
                .append("test", test)
                .append("tau", tau)
                .append("ratio", ratio)
                .append("tested", tested)
                .append("done", done)
                .toString();
    }
}
