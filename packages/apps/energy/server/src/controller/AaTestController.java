package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import com.guhecloud.cube.srv.gen.energymanagement.server.job.EnergyCollectJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

/**
 * @author lrj
 * @Description 区域分项配置
 * @date 2022-07-04 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AaTestController {

    private final EnergyCollectJob energyJob;

    /**
     * 功能描述： delete
     * 用于手动删数据
     */
    @DeleteMapping(value = "/meter-device/delete/test")
    public void deleteTest(@RequestParam("measurement") String measurement) throws ParseException {
        energyJob.deleteEnergy(measurement);
    }
}
