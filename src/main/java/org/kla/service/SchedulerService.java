package org.kla.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kla.scheduler.*;
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

    public ComputationResults run(String fileName, Integer numberOfMachines, Algorithm[] algorithms) {
        List<JobTemplate> jobsTemplate = fileService.readFromFile(fileName).getJobTemplateList();
        ComputationResults results = new ComputationResults(jobsTemplate);
//        generateToFile();

        for (Algorithm algorithm : algorithms) {
            ComputationResult calculate = calculate(algorithm, numberOfMachines, jobsTemplate);
            results.addResult(new Result(algorithm.name(), calculate));
        }
        return results;
    }

    public void generateToFile() {
        List<JobTemplate> jobsTemplate = generatorService.generateJobTemplates(3, 1);

        ComputationJob computationJob = new ComputationJob(jobsTemplate, "3 jobs, general tests, lambda = 1");
//        ComputationJob computationJob = new ComputationJob(jobsTemplate, "100 jobs, uniform tests, everything else generated in exponential distribution, lambda = 1");
        fileService.saveToFile(computationJob);
    }

    private Compute getCompute(Algorithm algorithm, int numberOfMachines, List<JobTemplate> jobTemplates) {
        return switch (algorithm) {
            case SBS -> new Sbs(numberOfMachines, jobTemplates);
            case Bbs -> new Bbs(numberOfMachines, jobTemplates);
            case SbsUniform -> new SbsUniform(numberOfMachines, jobTemplates);
            case BbsUniform -> new BbsUniform(numberOfMachines, jobTemplates);
            case Greedy -> new Greedy(numberOfMachines, jobTemplates);
            case TwoPhases -> new TwoPhases(numberOfMachines, jobTemplates);
        };
    }

    private ComputationResult calculate(Algorithm algorithm, int numberOfMachines, List<JobTemplate> jobTemplates) {
        Compute compute = getCompute(algorithm, numberOfMachines, jobTemplates);
        return compute.compute();
    }


}
