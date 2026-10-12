package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyChargeRules;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyChargeRulesParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyChargeRulesQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyChargeRulesVO;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用能计费规则服务
 */
public interface EnergyChargeRulesService extends IService<EnergyChargeRules> {
    /**
     * 获取计费规则分页列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    Pagination<EnergyChargeRulesVO> getEnergyChargeRulePage(EnergyChargeRulesQueryParam param);

    /**
     * 规则详情
     *
     * @param id 主键
     *
     * @return 详情
     */
    EnergyChargeRulesVO getEnergyChargeRuleDetail(Long id);

    /**
     * 新增或修改
     *
     * @param param 计费规则
     *
     * @return 主键
     */
    @Transactional(rollbackFor = { Exception.class })
    Long saveOrUpdateEnergyChargeRule(EnergyChargeRulesParam param);

    /**
     * 删除规则
     *
     * @param id 主键
     */
    void removeEnergyChargeRule(Long id);

    /**
     * 获取用能类型生效的规则
     *
     * @param param 条件
     *
     * @return 规则列表
     */
    List<EnergyChargeRulesVO> getInstrumentEffectiveRule(EnergyChargeRulesQueryParam param);
}
