package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.dao.InspectPlanStatisticsMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.model.param.InspectPlanStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.model.vo.InspectPlanStatisticsVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectPlanStatisticsService {

    private final InspectPlanStatisticsMapper inspectPlanStatisticsMapper;

    public Pagination<InspectPlanStatisticsVO> getPage(InspectPlanStatisticsParam param) {
        InspectPlanStatisticsParam queryParam = prepareParam(param);
        Page<InspectPlanStatisticsVO> page = inspectPlanStatisticsMapper.selectPlanStatisticsPage(PagingUtils.convert(queryParam), queryParam);
        return PagingUtils.convert(page);
    }

    public List<InspectPlanStatisticsVO> getExportPage(InspectPlanStatisticsParam param) {
        InspectPlanStatisticsParam queryParam = prepareParam(param);
        Page<InspectPlanStatisticsVO> page = inspectPlanStatisticsMapper.selectPlanStatisticsPage(PagingUtils.convert(queryParam), queryParam);
        List<InspectPlanStatisticsVO> records = page.getRecords();
        return CollectionUtils.isEmpty(records) ? new ArrayList<>() : records;
    }

    private InspectPlanStatisticsParam prepareParam(InspectPlanStatisticsParam param) {
        InspectPlanStatisticsParam queryParam = param == null ? new InspectPlanStatisticsParam() : param;
        if (queryParam.getPeriod() == null) {
            LocalDateTime now = LocalDateTime.now();
            queryParam.setPeriod(new Period(DateUtils.beginOfMonth(now), DateUtils.endOfMonth(now)));
        }
        return queryParam;
    }
}
