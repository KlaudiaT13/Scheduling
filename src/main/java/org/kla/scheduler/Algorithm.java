package org.kla.scheduler;

public enum Algorithm {
    SBS(""),
    Greedy(""),
    OptOffline("bruteforce -> reallly slow"),
    SbsUniform(""),
    Bbs(""),
    BbsUniform("");

    final String description;

    Algorithm( String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
