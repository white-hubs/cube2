package com.guhecloud.cube.srv.gen.energymanagement.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.YearEnergy;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.YearEnergyQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyTypeStatVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用能类型
 */
@Mapper
public interface YearEnergyMapper extends BaseMapper<YearEnergy> {

    /**
     * 获取年用能趋势
     *
     * @param param 开始时间
     *
     * @return 用能趋势
     */
    List<EnergyTypeStatVO> getYearEnergyTrend(@Param("param") YearEnergyQueryParam param);
}
