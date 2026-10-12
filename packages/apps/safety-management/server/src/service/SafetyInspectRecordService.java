package com.guhecloud.cube.srv.gen.safety.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.MarketInspectionRecordQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.MarketStatisticsInfo;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyInspectRecordVO;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyInspectRecordPO;
import com.guhecloud.platform.base.model.Pagination;
import org.apache.ibatis.annotations.Param;

public interface SafetyInspectRecordService extends IService<SafetyInspectRecordPO> {
    Pagination<MarketStatisticsInfo> getInspectionRecordMarketStatistics(MarketInspectionRecordQuery query);

    Pagination<SafetyInspectRecordVO> safetyRecordPage(@Param("query") MarketInspectionRecordQuery query);

    Pagination<SafetyInspectRecordVO> safetyErrorRecordPage(MarketInspectionRecordQuery query);

    /**
     * 小散工程巡查记录
     *
     * @param query
     *
     * @return
     */
    Pagination<SafetyInspectRecordVO> getSafetyInspectRecordPage(MarketInspectionRecordQuery query);
}
