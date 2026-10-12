package com.guhecloud.cube.biz.meattrade.server.pork.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.SpotCheckResource;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.SpotCheckResourcePageVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository("meattrade-spotCheckResourceMapper")
public interface SpotCheckResourceMapper extends BaseMapper<SpotCheckResource> {
    @Select("select r.*,d.label,d.foot_mark_code,mpr.goods_category_id,mpr.goods_category_name,mpr.fresh_label from meattrade_spot_check_resource r " +
            "inner join meattrade_pork_arrange_detail d on r.pork_arrange_detail_id = d.id " +
            "INNER JOIN meattrade_pork_report mpr on d.pork_report_id = mpr.id ${ew.customSqlSegment}"
    )
    Page<SpotCheckResourcePageVO> pageSCR(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
