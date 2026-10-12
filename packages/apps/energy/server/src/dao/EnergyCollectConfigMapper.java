package com.guhecloud.cube.srv.gen.energymanagement.server.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyCollectConfig;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyCollectConfigVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：能耗采集配置 Repository接口
 *
 * @author Appet
 * @date 2024-01-26 10:08:36
 */
@Repository
public interface EnergyCollectConfigMapper extends BaseMapper<EnergyCollectConfig> {

    /**
     * (能耗分类
     * 采集配置列表,包含燃料类型参数
     *
     * @param wrapper 条件，注意此处条件不能是 配置表和燃料表都有的字段
     *
     * @return 列表
     */
    @Select(" select ecc.*,fds.fuel_name,fds.fuel_name,fds.unit ,fds.carbon_factor ,fds.calculate_proportion from energy_collect_config ecc left join fuel_discharge_standard fds on(ecc.fuel_code = fds.code)  ${ew.customSqlSegment}")
    List<EnergyCollectConfigVO> getEnergyCollectConfigFuelList(@Param(Constants.WRAPPER) Wrapper<EnergyCollectConfig> wrapper);

    @Select(" select ecc.*,fds.fuel_name,fds.unit ,fds.carbon_factor ,fds.calculate_proportion from energy_collect_config ecc left join fuel_discharge_standard fds on(ecc.fuel_code = fds.code)  ${ew.customSqlSegment}")
    Page<EnergyCollectConfigVO> getEnergyCollectConfigFuelPage(Page<EnergyCollectConfigVO> page,
                                                               @Param(Constants.WRAPPER) Wrapper<EnergyCollectConfig> wrapper
                                                              );
}