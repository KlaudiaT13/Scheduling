package org.kla.scheduler;

public enum Algorithm {
    SBS(""),
    Bbs(""),
    SbsUniform(""),
    BbsUniform(""),
    Greedy(""),
    TwoPhases("Sorted List Scheduling algorithm as base");

    final String description;

    Algorithm( String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
