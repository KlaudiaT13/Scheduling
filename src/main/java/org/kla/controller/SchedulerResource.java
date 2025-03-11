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
import org.kla.dto.Result;
import org.kla.service.FileService;
import org.kla.service.SchedulerService;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

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
    @Path("/hello")
    @Produces(MediaType.APPLICATION_JSON)
    public ComputationResults hello() {
        return null; //schedulerService.run();
//        return "Helslo from Quarkus REST";
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get(
            @DefaultValue("default") @QueryParam("file") String fileName,
            @DefaultValue("2") @QueryParam("machine") Integer numberOfMachines,
            @QueryParam("alg") String[] algorithms
    ) throws JsonProcessingException {
        if (algorithms == null || algorithms.length == 0) {
            algorithms = new String[]{"SBS"};
        }
        ComputationResults result = schedulerService.run(fileName, numberOfMachines, algorithms);
        Set<String> files = fileService.listFiles();

        return index.data("jobs", mapper.writeValueAsString(result.getJobs()))
                .data("results", mapper.writeValueAsString(result.getResults()))
                .data("machine", numberOfMachines)
                .data("algorithms", mapper.writeValueAsString(algorithms))
                .data("files", files);
    }
}
