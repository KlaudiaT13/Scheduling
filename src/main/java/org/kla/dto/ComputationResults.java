package org.kla.dto;

import java.util.ArrayList;
import java.util.List;

public class ComputationResults {
    List<JobTemplate> jobs = new ArrayList<>();
    List<Result> results = new ArrayList<>();

    public ComputationResults() {
    }

    public ComputationResults(List<JobTemplate> jobs) {
        this.jobs = jobs;
    }

    public List<JobTemplate> getJobs() {return jobs;
    }

    public void setJobs(List<JobTemplate> jobs) {
        this.jobs = jobs;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public void addResult(Result result) {
        results.add(result);
    }
}
