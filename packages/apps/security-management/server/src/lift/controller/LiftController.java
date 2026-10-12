package com.guhecloud.cube.srv.gen.securitymanagement.server.lift.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.param.LiftRecordParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.param.LiftRecordStatisticsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.service.LiftService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.vo.LiftRecordVo;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LiftController {

    private final LiftService liftService;

    /**
     * @Description 电梯记录
     * @Author lrj
     */
    @GetMapping("/lift-record")
    public Pagination<LiftRecordVo> getLiftRecord(LiftRecordParam param) {
        return liftService.getLiftRecord(param);
    }

    /**
     * @Description 运行分析
     * @Author lrj
     */
    @GetMapping("/operations-analysis")
    public Map<String, Object> getOperationsAnalysis(LiftRecordStatisticsParam param) {
        return liftService.getOperationsAnalysis(param);
    }
}
