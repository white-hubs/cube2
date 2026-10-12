package com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.param.GasStatisticsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.service.MedicalGasDataService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.vo.MedicalGasDataPageVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.vo.MedicalGasDataVo;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalGasDataController {
    private final MedicalGasDataService medicalGasDataService;

    /**
     * @Description 今日气体用量
     * @Author lrj
     */
    @GetMapping("/today-gas-usage")
    public List<MedicalGasDataVo> getTodayGasUsage(GasStatisticsParam param) {
        return medicalGasDataService.getTodayGasUsage(param);
    }

    /**
     * @Description 今日气体用量top6
     * @Author lrj
     */
    @GetMapping("/today-gas-usage-top")
    public List<MedicalGasDataVo> getTodayGasUsageTop(GasStatisticsParam param) {
        return medicalGasDataService.getTodayGasUsageTop(param);
    }

    /**
     * @Descriptio气体使用趋势
     * @Author lrj
     */
    @GetMapping("/gas-usage-trend")
    public Map<String, Object> getGasUsageTrend(GasStatisticsParam param) {
        return medicalGasDataService.getGasUsageTrend(param);
    }

    /**
     * @Description 医用气体记录
     * @Author lrj
     */
    @GetMapping("/gas-usage-page")
    public Pagination<MedicalGasDataPageVo> getGasUsagePage(GasStatisticsParam param) {
        return medicalGasDataService.getGasUsagePage(param);
    }

}
