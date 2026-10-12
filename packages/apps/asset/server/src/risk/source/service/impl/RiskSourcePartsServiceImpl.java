package com.guhecloud.cube.srv.gen.property.management.server.risk.source.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.property.management.client.risk.source.param.PartsDetailsParam;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.dao.RiskSourcePartsMapper;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.entity.RiskSourceParts;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.service.RiskSourcePartsService;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-30  18:52
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskSourcePartsServiceImpl extends ServiceImpl<RiskSourcePartsMapper, RiskSourceParts> implements RiskSourcePartsService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertBatch(List<PartsDetailsParam> params, Long riskSourceLedgerId) {
        this.baseMapper.delete(new LambdaQueryWrapper<RiskSourceParts>()
                                       .eq(RiskSourceParts::getRiskSourceLedgerId, riskSourceLedgerId));
        List<RiskSourceParts> partsList = ConvertUtils.toList(RiskSourceParts.class, params);
        partsList.forEach(ex -> {
            ex.setRiskSourceLedgerId(riskSourceLedgerId);
        });
        return ObjectUtils.isEmpty(partsList) || this.saveBatch(partsList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertBatch(List<RiskSourceParts> params) {
        if (ObjectUtils.isEmpty(params)) {
            return true;
        }
        Set<Long> ids = params.stream().map(RiskSourceParts::getRiskSourceLedgerId).collect(Collectors.toSet());
        ids.forEach(ex -> {
            this.baseMapper.delete(new LambdaQueryWrapper<RiskSourceParts>()
                                           .eq(RiskSourceParts::getRiskSourceLedgerId, ex));
        });
        return this.saveBatch(params);
    }

    @Override
    public Boolean deleteBatch(Set<Long> riskSourceLedgerId) {
        return this.remove(new LambdaQueryWrapper<RiskSourceParts>().in(RiskSourceParts::getRiskSourceLedgerId, riskSourceLedgerId));
    }
}
