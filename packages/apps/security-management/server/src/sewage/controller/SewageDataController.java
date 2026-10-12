package com.guhecloud.cube.srv.gen.securitymanagement.server.sewage.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.sewage.service.SewageDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SewageDataController {
    private final SewageDataService sewageDataService;

    /**
     * @Description 水处理实时数据
     * @Author lrj
     */
    @GetMapping("/data-water-treatment")
    public Map<String, Object> getDataWaterTreatment() {
        return sewageDataService.getDataWaterTreatment();
    }
}
