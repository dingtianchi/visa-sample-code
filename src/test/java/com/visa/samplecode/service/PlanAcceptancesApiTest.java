package com.visa.samplecode.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.visa.samplecode.model.PlanAcceptancesRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@SpringBootTest
class PlanAcceptancesApiTest {

    @Autowired
    private PlanAcceptancesApi planAcceptancesApi;

    @Value("${client.visa.apiKey}")
    private String apiKey;

    @BeforeEach
    void setUp() throws IOException {
        Path path = Paths.get("output/");
        Files.createDirectories(path);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void searchPlanAcceptances() throws IOException {
        PlanAcceptancesRequest req = PlanAcceptancesRequest.builder()
                .startDateTime(toDate("2022-09-16T00:00:00.000+08:00"))
                .endDateTime(toDate("2022-10-07T08:35:00.000+08:00"))
                .status("CANCELLED")
                .planDetailLevel("SUMMARY")
                .build();
        String responseJson = planAcceptancesApi.searchPlanAcceptances(apiKey, req);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
        JsonNode root = objectMapper.readTree(responseJson);
        String filename = new SimpleDateFormat("'output/PlanAcceptancesResponse.'yyyy-MM-dd.HH:mm:ss'.json'").format(new Date());
        writer.writeValue(Paths.get(filename).toFile(), root);
    }

    private Date toDate(String input) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        AtomicReference<Date> output = new AtomicReference<>(new Date());
        try {
            output.set(formatter.parse(input));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output.get();
    }
}