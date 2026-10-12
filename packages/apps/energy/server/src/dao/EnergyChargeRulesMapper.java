package com.guhecloud.cube.srv.gen.energymanagement.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyChargeRules;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyChargeRulesQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyChargeRulesVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用能计费规则mapper
 */
@Mapper
public interface EnergyChargeRulesMapper extends BaseMapper<EnergyChargeRules> {

    /**
     * 获取用能类型生效的规则
     *
     * @param param 条件
     *
     * @return 列表
     */
    List<EnergyChargeRulesVO> getInstrumentEffectiveRule(@Param("param") EnergyChargeRulesQueryParam param);
}
