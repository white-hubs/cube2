package com.guhecloud.cube.customized.youxianqu.park.management.server.estate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.customized.youxianqu.park.management.server.common.vo.result.TrendResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.estate.po.EstateContractPO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物业合同mapper
 */
@Mapper
public interface EstateContractMapper extends BaseMapper<EstateContractPO> {
    /**
     * 获取签订趋势
     *
     * @param format 格式时间化格式
     * @param period 时间区间
     * @return 趋势
     */
    List<TrendResult> selectTrend(@Param("format") String format, @Param("period") Period period);
}
