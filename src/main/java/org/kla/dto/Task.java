package org.kla.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

public class Task {
    JobType jobType;
    UUID jobId;
    double completitionTime;
    double upperLimit; //needed unexpectedly for upgraded version in case task is reduced computation time

    private Task(JobType jobType, UUID jobId, double completitionTime) {
        this.jobType = jobType;
        this.jobId = jobId;
        this.completitionTime = completitionTime;
    }

    private Task(JobType jobType, UUID jobId, double completitionTime, double upperLimit) {
        this.jobType = jobType;
        this.jobId = jobId;
        this.completitionTime = completitionTime;
        this.upperLimit = upperLimit;
    }

    public static Task makeTask(Job job, JobType jobType) {
        if (jobType == JobType.TEST) {
            return new Task(jobType, job.getId(), job.getTest());
        } else if (jobType == JobType.UPPER_LIMIT) {
            return new Task(jobType, job.getId(), job.getUpperLimit());
        } else if (jobType == JobType.REDUCED_TIME) {
            return new Task(jobType, job.getId(), job.getReducedComputationTime(), job.getUpperLimit());
        }
        return null;
    }

    public JobType getJobType() {
        return jobType;
    }

    public UUID getJobId() {
        return jobId;
    }

    public double getCompletitionTime() {
        return completitionTime;
    }

    public double getUpperLimit() {return upperLimit;}

    @Override
    public String toString() {
        return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
                .append("jobType", jobType)
                .append("jobId", jobId)
                .append("completitionTime", completitionTime)
                .toString();
    }
}
