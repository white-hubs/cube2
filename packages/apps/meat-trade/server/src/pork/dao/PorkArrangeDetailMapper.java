package com.guhecloud.cube.biz.meattrade.server.pork.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeDetail;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PlaceStatQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkDeviceStatisticsVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkStatSummaryVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("meattrade-porkArrangeDetailMapper")
public interface PorkArrangeDetailMapper extends BaseMapper<PorkArrangeDetail> {

    int insertOrUpdateBatch(List<PorkArrangeDetail> list);

    @Select("select count(DISTINCT pd.supplier_id) as supplier,count(pd.id) as supplySlices,sum(pd.weight) as supplyWeight from meattrade_pork_arrange_audit pa " +
            "left join meattrade_pork_arrange_detail pd on pa.pork_report_id = pd.pork_report_id ${ew.customSqlSegment}"
    )
    PorkStatSummaryVO statSupplyMeatInfo(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select pd.* from meattrade_pork_arrange_audit pa " +
            "left join meattrade_pork_arrange_detail pd on pa.pork_report_id = pd.pork_report_id ${ew.customSqlSegment}"
    )
    Page<PorkArrangeDetail> pageByComplex(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Update("update meattrade_pork_arrange_detail set passed = #{passed} where pork_report_id = #{reportId}")
    void updateArrangeDetailPassed(@Param("reportId") Long reportId, @Param("passed") Boolean passed);

    List<PorkArrangeDetail> selectNonTransferFiles(@Param("querySize") Integer querySize, @Param("transferNumber") Integer transferNumber);

    int updateTransferFile(@Param("id") Long id, @Param("transferStatus") Boolean transferStatus,
                           @Param("transferNumber") Integer transferNumber, @Param("addNumber") Integer addNumber
                          );

    List<PorkDeviceStatisticsVO> selectPlacePorkArrangeStatistics(@Param("param") PlaceStatQueryParam param);
}
