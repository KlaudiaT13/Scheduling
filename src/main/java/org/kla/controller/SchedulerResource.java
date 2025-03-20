package org.kla.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.kla.dto.ComputationResults;
import org.kla.scheduler.Algorithm;
import org.kla.service.FileService;
import org.kla.service.SchedulerService;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.kla.scheduler.Algorithm.SBS;

@Path("/")
public class SchedulerResource {

    @Inject
    SchedulerService schedulerService;

    @Inject
    FileService fileService;

    @Inject
    Template index ;

    @Inject
    ObjectMapper mapper;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get(
            @DefaultValue("default") @QueryParam("file") String currentFile,
            @DefaultValue("2") @QueryParam("machine") Integer numberOfMachines,
            @QueryParam("alg") Algorithm[] algorithms
    ) throws JsonProcessingException {
        if (algorithms == null || algorithms.length == 0) {
            algorithms = new Algorithm[]{SBS};
        }
        ComputationResults result = schedulerService.run(currentFile, numberOfMachines, algorithms);
        Set<String> files = fileService.listFiles();

        return index.data("jobs", mapper.writeValueAsString(result.getJobs()))
                .data("results", mapper.writeValueAsString(result.getResults()))
                .data("resultSize", result.getResults().size())
                .data("machine", numberOfMachines)
                .data("currentFile", currentFile)
//                .data("algorithms", Algorithm.values())
                .data("algorithms", getAlgorithms(algorithms))
                .data("files", files);
    }

    private List<AlgorithmHtml> getAlgorithms(Algorithm[] algorithms) {
        return Arrays.stream(Algorithm.values())
                .map(a -> new AlgorithmHtml(a.name(), a.getDescription(), contains(algorithms, a)))
                .toList();
    }

    private record AlgorithmHtml(String name, String description, Boolean selected) {}
}
