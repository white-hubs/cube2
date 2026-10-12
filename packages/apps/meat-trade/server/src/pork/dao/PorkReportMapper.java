package com.guhecloud.cube.biz.meattrade.server.pork.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkReport;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkReportStatAuditVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkSupplyStatisticsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author lzy
 * @date 2021/12/4
 */
@Repository(value = "meattrade-porkReportMapper")
public interface PorkReportMapper extends BaseMapper<PorkReport> {

    PorkReportStatAuditVO selectSupplierReportAudit(@Param("supplierId") Long supplierId, @Param("tenant") Integer tenant);

    PorkSupplyStatisticsVO selectSupplyStatistics(@Param("param") StatQueryParam queryParam);

    Integer selectSupplySupplierCount(@Param("param") StatQueryParam queryParam);
}
