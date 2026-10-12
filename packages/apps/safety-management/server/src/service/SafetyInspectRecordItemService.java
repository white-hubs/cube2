package com.guhecloud.cube.srv.gen.safety.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.InspectionItemStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.StackedBarChartVO;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyInspectRecordItemPO;
import com.guhecloud.platform.base.model.Pagination;

public interface SafetyInspectRecordItemService extends IService<SafetyInspectRecordItemPO> {
    Pagination<StackedBarChartVO> inspectVehicleViolationStat(InspectionItemStatisticsQuery query);

    Integer getInspectRecordCount(InspectionItemStatisticsQuery query);
}
