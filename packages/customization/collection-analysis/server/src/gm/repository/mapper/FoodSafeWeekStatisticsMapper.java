package com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.model.param.FoodSafeStatisticsQueryParam;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.TrendResult;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.FoodSafeWeekStatisticsPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FoodSafeWeekStatisticsMapper extends BaseMapper<FoodSafeWeekStatisticsPO> {

    /**
     * 获取检测次数趋势
     *
     * @param param 条件
     *              format 格式时间化格式
     *              period 时间区间
     *
     * @return 趋势
     */
    List<TrendResult> selectTrend(@Param("param") FoodSafeStatisticsQueryParam param);
}
