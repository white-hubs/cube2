package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.controller;

import com.guhecloud.cube.srv.gen.maintain.server.common.dto.TotalNumberRateDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.InspectionRecordNumMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectRecordQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.AbnormalResultsStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionCommonParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.AbnormalResultsStatisticsResult;
import com.guhecloud.platform.base.model.Period;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 巡检记录统计
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectRecordStatisticsController {
    private final InspectRecordQueryService recordService;

    /**
     * 一段事件的异常项数量统计
     *
     * @param param 条件
     *
     * @return 结果
     */
    @PostMapping("/third/abnormalResultsStatistics")
    public AbnormalResultsStatisticsResult abnormalResultsStatistics(@RequestBody @Valid AbnormalResultsStatisticsParam param) {
        return recordService.abnormalResultsStatistics(param);
    }

    /**
     * 巡检记录异常率
     *
     * @param param 时间段
     *
     * @return 异常率
     */
    @GetMapping("/inspectRecord/errRate")
    public TotalNumberRateDTO getRecordErrRate(InspectionCommonParam param) {
        return recordService.getRecordErrRate(param);
    }

    /**
     * 巡检记录异常率
     *
     * @param param 时间段
     *
     * @return 异常率
     */
    @PostMapping("/inspectRecord/errRate/post")
    public TotalNumberRateDTO postRecordErrRate(@RequestBody InspectionCommonParam param) {
        return recordService.getRecordErrRate(param);
    }

    /**
     * 巡检记录完成率
     *
     * @param param 时间段
     *
     * @return 率
     */
    @GetMapping("/inspectRecord/completeRate")
    public TotalNumberRateDTO getRecordCompleteRate(InspectionCommonParam param) {
        return recordService.getRecordCompleteRate(param);
    }

    /**
     * 巡检次数指标
     */
    @GetMapping("/inspect-record/user/complete/num")
    public InspectionRecordNumMetricDTO getRecordUserNumMetric(@RequestParam("period") Period period) {
        return recordService.getRecordUserNumMetric(period);
    }
}
