package com.guhecloud.cube.biz.meattrade.server.order.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.meattrade.server.order.dao.SettlementConfigMapper;
import com.guhecloud.cube.biz.meattrade.server.order.entity.SettlementConfig;
import com.guhecloud.cube.biz.meattrade.server.order.param.SettlementConfigCreateParam;
import com.guhecloud.cube.biz.meattrade.server.order.param.SettlementConfigUpdateParam;
import com.guhecloud.cube.biz.meattrade.server.order.service.SettlementConfigService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.cloud.logger.constant.OperationType;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("meattrade-settlementConfigService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SettlementConfigServiceImpl extends ServiceImpl<SettlementConfigMapper, SettlementConfig> implements SettlementConfigService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(success = "编辑分佣规则", fail = "编辑分佣规则失败，失败原因：「{{#_errorMsg}}」",
               type = "MEATTRADE", typeName = "肉类滑轨",
               subType = "SETTLEMENT_CONFIG", subTypeName = "分佣规则",
               operationType = OperationType.UPDATE,
               extra = "{{#param}}",
               bizNo = ""
    )
    public void save(List<SettlementConfigCreateParam> param) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        List<SettlementConfig> result = new ArrayList<>();
        param.forEach(p -> {
            p.getRules().forEach(config -> {
                SettlementConfig settlementConfig = ObjectUtils.copying(config, SettlementConfig.class);
                settlementConfig.setName(p.getName());
                settlementConfig.setFlag(p.getFlag());
                settlementConfig.setWay(p.getWay());
                settlementConfig.setTenant(tenant);
                result.add(settlementConfig);
            });
        });
        this.saveBatch(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(success = "编辑分佣规则", fail = "编辑分佣规则失败，失败原因：「{{#_errorMsg}}」",
               type = "MEATTRADE", typeName = "肉类滑轨",
               subType = "SETTLEMENT_CONFIG", subTypeName = "分佣规则",
               operationType = OperationType.UPDATE,
               extra = "{{#param}}",
               bizNo = ""
    )
    public void update(List<SettlementConfigUpdateParam> param) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        List<SettlementConfig> updateResult = new ArrayList<>();
        List<SettlementConfig> insertResult = new ArrayList<>();
        param.forEach(p -> {
            p.getRules().forEach(config -> {
                SettlementConfig settlementConfig = this.getById(config.getId());
                if (settlementConfig == null) {
                    settlementConfig = new SettlementConfig();
                    settlementConfig.setId(IdWorker.getId());
                    settlementConfig.setName(p.getName());
                    settlementConfig.setWay(p.getWay());
                    settlementConfig.setFlag(p.getFlag());
                    settlementConfig.setStartWeight(config.getStartWeight());
                    settlementConfig.setEndWeight(config.getEndWeight());
                    settlementConfig.setValue(config.getValue());
                    settlementConfig.setTenant(tenant);
                    insertResult.add(settlementConfig);
                    return;
                }
                settlementConfig.setStartWeight(config.getStartWeight());
                settlementConfig.setEndWeight(config.getEndWeight());
                settlementConfig.setValue(config.getValue());
                settlementConfig.setFlag(p.getFlag());
                updateResult.add(settlementConfig);
            });
        });
        this.updateBatchById(updateResult);
        this.saveBatch(insertResult);
    }

    @Override
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public List<SettlementConfig> listActiveSettlementConfig(@NonNull Integer tenant) {
        return this.baseMapper.selectList(Wrappers.<SettlementConfig>lambdaQuery()
                                                  .eq(SettlementConfig::getFlag, true)
                                                  .eq(SettlementConfig::getTenant, tenant));
    }
}
