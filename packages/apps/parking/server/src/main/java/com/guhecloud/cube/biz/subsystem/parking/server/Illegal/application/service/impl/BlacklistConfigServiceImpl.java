package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.commandgetway.command.BlacklistConfigCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.service.BlacklistConfigService;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.BlacklistConfigPO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.repository.BlacklistConfigRepository;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.vo.BlacklistConfigVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 16:45
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BlacklistConfigServiceImpl implements BlacklistConfigService {

    private final BlacklistConfigRepository blacklistConfigRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdate(BlacklistConfigCommand param) {
        if (Objects.nonNull(param.getId())) {
            BlacklistConfigPO blacklistConfigPO = blacklistConfigRepository.getById(param.getId());
            blacklistConfigPO.setComparisonCondition(param.getComparisonCondition());
            blacklistConfigPO.setThresholdValue(param.getThresholdValue());
            return blacklistConfigRepository.updateById(blacklistConfigPO);
        }
        BlacklistConfigPO blacklistConfigPO = BlacklistConfigPO.builder()
                .id(IdWorker.getId())
                .comparisonCondition(param.getComparisonCondition())
                .thresholdValue(param.getThresholdValue())
                .build();
        return blacklistConfigRepository.save(blacklistConfigPO);
    }

    @Override
    public BlacklistConfigVO getConfig() {
        BlacklistConfigPO blacklistConfigPO = blacklistConfigRepository.lambdaQuery()
                .orderByDesc(BlacklistConfigPO::getUpdateTime)
                .last("limit 1")
                .one();
        if (Objects.isNull(blacklistConfigPO)) {
            return new BlacklistConfigVO();
        }
        return BlacklistConfigVO.builder()
                .id(blacklistConfigPO.getId())
                .comparisonCondition(blacklistConfigPO.getComparisonCondition())
                .thresholdValue(blacklistConfigPO.getThresholdValue())
                .build();

    }
}
