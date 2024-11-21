import java.util.UUID;

public class Task {
    JobType jobType;
    UUID uuid;
    double completitionTime;

    private Task(JobType jobType, UUID uuid, double completitionTime) {
        this.jobType = jobType;
        this.uuid = uuid;
        this.completitionTime = completitionTime;
    }

    public static Task makeTask(Job job, JobType jobType) {
        if (jobType == JobType.TEST) {
            return new Task(jobType, job.getId(), job.getTest());
        }
        else if (jobType == JobType.UPPER_LIMIT) {
            return new Task(jobType, job.getId(), job.getUpperLimit());
        }
        else if (jobType == JobType.REDUCED_TIME) {
            return new Task(jobType, job.getId(), job.getReducedComputationTime());
        }
        return null;
    }
}
