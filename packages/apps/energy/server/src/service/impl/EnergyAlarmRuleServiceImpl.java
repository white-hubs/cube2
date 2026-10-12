package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyAlarmRuleMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyAlarmRule;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyAlarmRuleParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyAlarmRuleQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyAlarmRuleService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyAlarmRuleVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：报警规则设备绑定服务层
 *
 * @author lrj
 * @date 2022-07-07 15:12:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyAlarmRuleServiceImpl extends ServiceImpl<EnergyAlarmRuleMapper, EnergyAlarmRule> implements EnergyAlarmRuleService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(EnergyAlarmRuleParam param) {
        EnergyAlarmRule energyAlarmRule = ObjectUtils.copying(param, EnergyAlarmRule.class);
        this.baseMapper.insert(energyAlarmRule);
        return energyAlarmRule.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, EnergyAlarmRuleParam param) {
        EnergyAlarmRule energyAlarmRule = this.baseMapper.selectById(id);
        Assert.isNotFound(energyAlarmRule, "未查询到相关信息");
        ObjectUtils.copying(param, energyAlarmRule);
        this.baseMapper.updateById(energyAlarmRule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.stream().forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public EnergyAlarmRuleVO getById(Long id) {
        EnergyAlarmRule energyAlarmRule = this.baseMapper.selectById(id);
        return ObjectUtils.copying(energyAlarmRule, EnergyAlarmRuleVO.class);
    }

    @Override
    public Pagination<EnergyAlarmRuleVO> getPage(EnergyAlarmRuleQueryParam param) {
        //请自行添加查询条件
        Page<EnergyAlarmRule> page = this.lambdaQuery()
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, EnergyAlarmRuleVO.class);
    }
}