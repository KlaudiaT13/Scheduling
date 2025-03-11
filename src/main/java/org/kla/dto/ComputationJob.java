package org.kla.dto;

import java.util.List;


public class ComputationJob {
    String description;
    List<JobTemplate> jobTemplateList;

    public ComputationJob() {
    }

    public ComputationJob(List<JobTemplate> jobTemplateList) {
        this.jobTemplateList = jobTemplateList;
    }

    public ComputationJob(List<JobTemplate> jobTemplateList, String description) {
        this.description = description;
        this.jobTemplateList = jobTemplateList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<JobTemplate> getJobTemplateList() {
        return jobTemplateList;
    }

    public void setJobTemplateList(List<JobTemplate> jobTemplateList) {
        this.jobTemplateList = jobTemplateList;
    }
}
