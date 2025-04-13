package org.kla.scheduler;

public enum Algorithm {
    SBS(""),
    Greedy(""),
    OptOffline("bruteforce -> reallly slow"),
    SbsUniform(""),
    Bbs(""),
    BbsUniform(""),
    UpgradeBbsUniform(""),
    BbsUniformTest(""),
    TwoPhases("Sorted List Scheduling algorithm as base");

    final String description;

    Algorithm( String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
