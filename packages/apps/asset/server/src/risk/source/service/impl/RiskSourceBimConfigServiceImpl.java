package com.guhecloud.cube.srv.gen.property.management.server.risk.source.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceBimConfigDeleteParam;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceBimConfigPageParam;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceBimConfigParam;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerPageParam;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceBimConfigVO;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceLedgerVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.PropertyBimConfigEnum;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.dao.RiskSourceBimConfigMapper;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.entity.RiskSourceBimConfig;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.service.RiskSourceBimConfigService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-07  13:54
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskSourceBimConfigServiceImpl extends ServiceImpl<RiskSourceBimConfigMapper, RiskSourceBimConfig> implements RiskSourceBimConfigService {
    private final AreaClient areaClient;

    @Override
    public Pagination<RiskSourceLedgerVO> queryledgerByBimSceneTypePage(RiskSourceLedgerPageParam param) {
        Page<RiskSourceLedgerVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = this.baseMapper.queryBySceneTypePage(page, param);
        page.getRecords().forEach(ex -> {
            if (ObjectUtils.isNotEmpty(ex.getAreaId())) {
                List<AreaDTO> area = areaClient.getByIds(StringUtils.splitAndTranslate(ex.getAreaId(), Long::valueOf));
                ex.setArea(area);
            }
        });
        return PagingUtils.convert(page);
    }

    @Override
    public Pagination<RiskSourceBimConfigVO> queryByPage(RiskSourceBimConfigPageParam param) {
        Page<RiskSourceBimConfigVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = this.baseMapper.queryByPage(page, param);
        page.getRecords().forEach(ex -> {
            if (ObjectUtils.isNotEmpty(ex.getAreaId())) {
                List<AreaDTO> area = areaClient.getByIds(StringUtils.splitAndTranslate(ex.getAreaId(), Long::valueOf));
                ex.setArea(area);
            }
        });
        return PagingUtils.convert(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(RiskSourceBimConfigParam param) {
        List<RiskSourceBimConfig> riskSourceBimConfigList = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(param.getBindWay()) && PropertyBimConfigEnum.ALL.name().equals(param.getBindWay())) {
            List<RiskSourceLedgerVO> riskSourceLedgerList = this.baseMapper.queryBySceneTypeAll(param);
            for (RiskSourceLedgerVO ex : riskSourceLedgerList) {
                RiskSourceBimConfig bimConfig = new RiskSourceBimConfig();
                bimConfig.setSceneType(param.getSceneType());
                bimConfig.setBusinessId(ex.getId().toString());
                riskSourceBimConfigList.add(bimConfig);
                if (riskSourceLedgerList.size() > 200) {
                    this.saveBatch(riskSourceBimConfigList);
                    riskSourceLedgerList = new ArrayList<>();
                }
            }
        } else {
            param.getBusinessIds().forEach(ex -> {
                RiskSourceBimConfig bimConfig = new RiskSourceBimConfig();
                bimConfig.setSceneType(param.getSceneType());
                bimConfig.setBusinessId(ex);
                riskSourceBimConfigList.add(bimConfig);
            });
        }
        return this.saveBatch(riskSourceBimConfigList);
    }

    @Override
    public Boolean deleteById(RiskSourceBimConfigDeleteParam ids) {
        ids.getIds().forEach(ex -> {
            DatabaseContextHolder.softDelete(this, ex);
        });
        return true;
    }
}
