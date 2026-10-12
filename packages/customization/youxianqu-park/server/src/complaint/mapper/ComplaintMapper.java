package com.guhecloud.cube.customized.youxianqu.park.management.server.complaint.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.customized.youxianqu.park.management.server.common.vo.result.TrendResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.complaint.po.ComplaintPO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 投诉mapper
 */
public interface ComplaintMapper extends BaseMapper<ComplaintPO> {

    /**
     * 获取投诉趋势
     *
     * @param format 格式时间化格式
     * @param period 时间区间
     * @return 趋势
     */
    List<TrendResult> selectTrend(@Param("format") String format, @Param("period") Period period);
}
