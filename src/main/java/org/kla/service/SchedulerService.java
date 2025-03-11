package org.kla.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kla.scheduler.Compute;
import org.kla.scheduler.Sbs;
import org.kla.dto.ComputationJob;
import org.kla.dto.ComputationResult;
import org.kla.dto.ComputationResults;
import org.kla.dto.JobTemplate;
import org.kla.dto.Result;

import java.util.List;

@ApplicationScoped
public class SchedulerService {

    @Inject
    GeneratorService generatorService;

    @Inject
    FileService fileService;

    public ComputationResults run(String fileName, Integer numberOfMachines, String[] algorithms) {
        List<JobTemplate> jobsTemplate = fileService.readFromFile(fileName).getJobTemplateList();
        ComputationResults results = new ComputationResults(jobsTemplate);

        for (String algorithm : algorithms) {
            ComputationResult calculate = calculate(algorithm, numberOfMachines, jobsTemplate);
            results.addResult(new Result(algorithm, calculate));
        }
        return results;
    }

    public void generateToFile() {
        List<JobTemplate> jobsTemplate = generatorService.generateJobTemplates(10, 2);

        ComputationJob computationJob = new ComputationJob(jobsTemplate, "computation1");
        fileService.saveToFile(computationJob);
    }

    private Compute getCompute(String algorithm, int numberOfMachines, List<JobTemplate> jobTemplates) {
        return switch (algorithm) {
            case "SBS" -> new Sbs(numberOfMachines, jobTemplates);
            default -> throw new IllegalStateException("Unexpected value: " + algorithm);
        };
    }

    private ComputationResult calculate(String algorithm, int numberOfMachines, List<JobTemplate> jobTemplates) {
        Compute compute = getCompute(algorithm, numberOfMachines, jobTemplates);
        return compute.compute();
    }


}
