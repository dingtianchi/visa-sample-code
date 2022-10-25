package com.visa.samplecode.service;

import com.visa.samplecode.config.PlanAcceptancesApiConfiguration;
import com.visa.samplecode.model.PlanAcceptancesRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    value = "apiClient",
    url = "${client.visa.baseUrl}",
    configuration = PlanAcceptancesApiConfiguration.class)
public interface PlanAcceptancesApi {
  @RequestMapping(method = RequestMethod.POST, value = "${client.visa.resourcePaths.planAcceptancesApi}")
  String searchPlanAcceptances(@RequestParam("apiKey") String apiKey, @RequestBody PlanAcceptancesRequest req);
}
