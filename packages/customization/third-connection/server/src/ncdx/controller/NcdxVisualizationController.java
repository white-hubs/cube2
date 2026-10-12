package com.guhecloud.cube.srv.gen.third.connection.server.ncdx.controller;

import com.guhecloud.cube.srv.gen.third.connection.server.ncdx.service.NcdxVisualizationService;
import com.guhecloud.cube.srv.gen.third.connection.server.ncdx.vo.NcdxEducationalVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 南充党校可视化
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NcdxVisualizationController {
    private final NcdxVisualizationService ncdxVisualizationService;

    @GetMapping("/ncdx/educational/visualization")
    public NcdxEducationalVO getEducational() {
        return ncdxVisualizationService.getEducational();
    }

    @GetMapping("/ncdx/test/educational/visualization")
    public NcdxEducationalVO test(@RequestParam(value = "baseUrl", required = false) String baseUrl) {
        return ncdxVisualizationService.getEducationalTest(baseUrl);
    }

}
