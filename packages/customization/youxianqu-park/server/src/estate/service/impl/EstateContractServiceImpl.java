package com.guhecloud.cube.customized.youxianqu.park.management.server.estate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customized.youxianqu.park.management.server.common.utils.TrendTimeUtils;
import com.guhecloud.cube.customized.youxianqu.park.management.server.common.vo.result.TrendResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.estate.enums.ConditionTypeEnum;
import com.guhecloud.cube.customized.youxianqu.park.management.server.estate.mapper.EstateContractMapper;
import com.guhecloud.cube.customized.youxianqu.park.management.server.estate.param.EstateContractQueryParam;
import com.guhecloud.cube.customized.youxianqu.park.management.server.estate.po.EstateContractPO;
import com.guhecloud.cube.customized.youxianqu.park.management.server.estate.result.EstateContractResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.estate.service.EstateContractService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 物业合同实现
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EstateContractServiceImpl extends ServiceImpl<EstateContractMapper, EstateContractPO> implements EstateContractService {
    private final EstateContractMapper estateContractMapper;

    /**
     * 物业合同分页
     *
     * @param param 条件
     *
     * @return 分页
     */
    @Override
    public Pagination<EstateContractResult> getPage(EstateContractQueryParam param) {

        Page<EstateContractPO> page = estateContractMapper.selectPage(PagingUtils.convert(param), this.getPageWrapper(param));

        return PagingUtils.convert(page, EstateContractResult.class);
    }

    /**
     * 获取租赁趋势
     *
     * @param param 条件
     *
     * @return 趋势
     */
    @Override
    public List<TrendResult> getTrend(EstateContractQueryParam param) {
        TrendTimeUtils.setFormatAndPeriod(param);
        return estateContractMapper.selectTrend(param.getFormat(), param.getPeriod());
    }

    private LambdaQueryWrapper<EstateContractPO> getPageWrapper(EstateContractQueryParam param) {
        Period period = param.getPeriod();
        boolean signPeriodBool = period != null && period.getBegin() != null && period.getEnd() != null;
        LambdaQueryWrapper<EstateContractPO> wrapper = Wrappers.<EstateContractPO>lambdaQuery()
                .between(signPeriodBool, EstateContractPO::getSignDate, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd));
        if (StringUtils.isNotBlank(param.getWarnCondition())) {
            if (ConditionTypeEnum.GT.name().equals(param.getWarnCondition()) && param.getWarnDay() != null) {
                wrapper.gt(EstateContractPO::getWarnDay, param.getWarnDay());
            } else if (ConditionTypeEnum.GE.name().equals(param.getWarnCondition())) {
                wrapper.ge(EstateContractPO::getWarnDay, param.getWarnDay());
            } else if (ConditionTypeEnum.LT.name().equals(param.getWarnCondition())) {
                wrapper.lt(EstateContractPO::getWarnDay, param.getWarnDay());
            } else if (ConditionTypeEnum.LE.name().equals(param.getWarnCondition())) {
                wrapper.le(EstateContractPO::getWarnDay, param.getWarnDay());
            } else {
                wrapper.eq(EstateContractPO::getWarnDay, param.getWarnDay());
            }
        }
        if (StringUtils.isNotBlank(param.getOverdueCondition()) && param.getOverdueDay() != null) {
            if (ConditionTypeEnum.GT.name().equals(param.getOverdueCondition())) {
                wrapper.gt(EstateContractPO::getWarnDay, param.getOverdueDay());
            } else if (ConditionTypeEnum.GE.name().equals(param.getOverdueCondition())) {
                wrapper.ge(EstateContractPO::getWarnDay, param.getOverdueDay());
            } else if (ConditionTypeEnum.LT.name().equals(param.getOverdueCondition())) {
                wrapper.lt(EstateContractPO::getWarnDay, param.getOverdueDay());
            } else if (ConditionTypeEnum.LE.name().equals(param.getOverdueCondition())) {
                wrapper.le(EstateContractPO::getWarnDay, param.getOverdueDay());
            } else {
                wrapper.eq(EstateContractPO::getWarnDay, param.getOverdueDay());
            }
        }
        return wrapper.orderByDesc(EstateContractPO::getSignDate);
    }
}
