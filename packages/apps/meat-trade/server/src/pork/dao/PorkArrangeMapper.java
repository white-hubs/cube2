package com.guhecloud.cube.biz.meattrade.server.pork.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrange;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatQueryParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("meattrade-porkArrangeMapper")
public interface PorkArrangeMapper extends BaseMapper<PorkArrange> {

    Page<PorkArrange> pageReportSuspicion(Page<?> page, @Param("param") StatQueryParam queryParam);

    /**
     * 注意这里把审核时间赋给了理货时间
     *
     * @param wrapper
     *
     * @return
     */
    @Select("select pd.id,pd.slices,pd.weight,pd.status,pa.audit_time as porkArrangeTime from meattrade_pork_arrange_audit pa " +
            "left join meattrade_pork_arrange pd on pa.pork_report_id = pd.pork_report_id ${ew.customSqlSegment}"
    )
    List<PorkArrange> listByComplex(@Param(Constants.WRAPPER) Wrapper wrapper);

    Integer selectCheatCount(@Param("param") StatQueryParam param);

    Integer selectArrangeCount(@Param("param") StatQueryParam queryParam);
}
