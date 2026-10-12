package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyChargeRulesMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyChargeRules;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyChargeRulesParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyChargeRulesQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyChargeRulesService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyChargeRulesVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用能计费规则服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyChargeRulesServiceImpl extends ServiceImpl<EnergyChargeRulesMapper, EnergyChargeRules> implements EnergyChargeRulesService {

    private final EnergyChargeRulesMapper energyChargeRulesMapper;

    /**
     * 获取计费规则分页列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @Override
    public Pagination<EnergyChargeRulesVO> getEnergyChargeRulePage(EnergyChargeRulesQueryParam param) {
        Page<EnergyChargeRules> page = this.page(PagingUtils.convert(param), Wrappers.<EnergyChargeRules>lambdaQuery()
                                                         .eq(StringUtils.isNotBlank(param.getInstrumentType()), EnergyChargeRules::getInstrumentType, param.getInstrumentType())
                                                         .in(!CollectionUtils.isEmpty(param.getInstrumentTypeList()), EnergyChargeRules::getInstrumentType, param.getInstrumentTypeList())
                                                         .orderByDesc(EnergyChargeRules::getEffectiveTime)
                                                );

        return PagingUtils.convert(page, EnergyChargeRulesVO.class);
    }

    /**
     * 规则详情
     *
     * @param id 主键
     *
     * @return 详情
     */
    @Override
    public EnergyChargeRulesVO getEnergyChargeRuleDetail(Long id) {
        Assert.isParameterError(id == null, "主键不能为空！");
        EnergyChargeRules po = this.getById(id);
        Assert.isNotFound(po, "规则不存在！");
        return ObjectUtils.copying(po, EnergyChargeRulesVO.class);
    }

    /**
     * 新增或修改
     *
     * @param param 计费规则
     *
     * @return 主键
     */
    @Override
    @Transactional(rollbackFor = { Exception.class })
    public Long saveOrUpdateEnergyChargeRule(EnergyChargeRulesParam param) {
        EnergyChargeRules po = new EnergyChargeRules();
        if (param.getId() != null) {
            po = this.getById(param.getId());
        }
        Assert.isNotFound(po == null, "规则为空！");
        BeanUtils.copyProperties(param, po);
        this.saveOrUpdate(po);
        return po.getId();
    }

    /**
     * 删除规则
     *
     * @param id 主键
     */
    @Override
    public void removeEnergyChargeRule(Long id) {
        this.removeById(id);
    }

    /**
     * 获取用能类型生效的规则
     *
     * @param param 条件
     *
     * @return 规则列表
     */
    @Override
    public List<EnergyChargeRulesVO> getInstrumentEffectiveRule(EnergyChargeRulesQueryParam param) {
        return energyChargeRulesMapper.getInstrumentEffectiveRule(param);
    }
}
