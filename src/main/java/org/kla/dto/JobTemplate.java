package org.kla.dto;

import java.util.UUID;

public class JobTemplate {
    private final UUID id;
    private final double upperLimit;
    private final double reducedComputationTime;
    private final double test;

    public JobTemplate(double upperLimit, double reducedComputationTime, double test) {
        this.id = UUID.randomUUID();
        this.upperLimit = upperLimit;
        this.reducedComputationTime = reducedComputationTime;
        this.test = test;
    }

    public UUID getId() {
        return id;
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
}
