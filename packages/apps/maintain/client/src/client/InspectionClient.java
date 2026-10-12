package com.guhecloud.cube.srv.gen.maintain.client;

import com.guhecloud.cube.srv.gen.maintain.vo.InspectStatByTargetVO;
import com.guhecloud.cube.srv.gen.maintain.vo.SafetyReportResultVO;
import com.guhecloud.platform.base.model.Period;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(ServiceRoute.TAG)
public interface InspectionClient {
    /**
     * 巡检目标对象统计
     *
     * @param targetId 目标对象ID：设备ID，档位ID
     * @param period   时间
     *
     * @return 对象
     */
    @GetMapping("/inspect-stat/target-health")
    InspectStatByTargetVO getHealthStatByTarget(@RequestParam("targetId") Long targetId,
                                                @RequestParam(value = "period", required = false) Period period
                                               );

    @GetMapping("/inspect-analysis/safety-report")
    SafetyReportResultVO inspectAnalysisSafetyReport(@RequestParam("period") Period period);

    /**
     * 根据巡检目标获取异常目标数
     *
     * @param targetIdList 目标id集合
     *
     * @return 异常的目标数量
     */
    @PostMapping("/inspect-record/postErrNumByTargets")
    Long getErrNumByTargets(@RequestParam("targetIdList") List<Long> targetIdList);

}
