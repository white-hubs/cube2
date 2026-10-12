package com.guhecloud.cube.srv.gen.safety.management.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.MarketInspectionRecordQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.MarketStatisticsInfo;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyInspectRecordVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework.SafetyErrorInspectItemVO;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyInspectRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SafetyInspectRecordMapper extends BaseMapper<SafetyInspectRecordPO> {
    /**
     * 巡检排名环比
     *
     * @param query 条件
     *
     * @return 巡检排名环比
     */
    List<MarketStatisticsInfo> getInspectionRecordMarketStatistics(@Param("query") MarketInspectionRecordQuery query);

    /**
     * 巡检记录
     *
     * @param query 条件
     *
     * @return 巡检记录
     */
    Page<SafetyInspectRecordVO> safetyRecordPage(Page page, @Param("query") MarketInspectionRecordQuery query);

    /**
     * 巡检记录
     *
     * @param query 条件
     *
     * @return 巡检记录
     */
    Page<SafetyInspectRecordVO> safetyErrorRecordPage(Page page, @Param("query") MarketInspectionRecordQuery query);

    /**
     * 查询巡检记录异常项
     *
     * @return
     */
    List<SafetyErrorInspectItemVO> selectErrorInspectItem(@Param("recordIdList") List<Long> recordIdList);
}
