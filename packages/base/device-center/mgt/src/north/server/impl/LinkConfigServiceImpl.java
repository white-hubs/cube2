package com.guhecloud.cube.iot.mgt.north.server.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.iot.mgt.comm.platform.PlatformEvnCom;
import com.guhecloud.cube.iot.mgt.comm.util.Assert;
import com.guhecloud.cube.iot.mgt.core.data.vo.linkage.LinkageConfig;
import com.guhecloud.cube.iot.mgt.core.data.vo.linkage.SingleLinkConditionMeta;
import com.guhecloud.cube.iot.mgt.core.data.vo.linkage.action.LinkActionTriggerMeta;
import com.guhecloud.cube.iot.mgt.core.def.cache.LinkageCacheSupport;
import com.guhecloud.cube.iot.mgt.north.mapper.LinkConditionMetaMapper;
import com.guhecloud.cube.iot.mgt.north.mapper.LinkConfigMapper;
import com.guhecloud.cube.iot.mgt.north.mapper.LinkTriggerMetaMapper;
import com.guhecloud.cube.iot.mgt.north.model.LinkConditionMeta;
import com.guhecloud.cube.iot.mgt.north.model.LinkConfig;
import com.guhecloud.cube.iot.mgt.north.model.LinkTriggerMeta;
import com.guhecloud.cube.iot.mgt.north.param.RuleCreateRequest;
import com.guhecloud.cube.iot.mgt.north.server.LinkConfigService;
import com.guhecloud.cube.iot.mgt.north.vo.RuleCreateResponse;
import com.guhecloud.cube.iot.mgt.north.vo.RuleDetailVo;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.guhecloud.cube.iot.mgt.core.def.LinkageConst.compareVal2Str;

@Service
public class LinkConfigServiceImpl extends ServiceImpl<LinkConfigMapper, LinkConfig> implements LinkConfigService {
    @Resource
    private PlatformEvnCom platformEvnCom;
    @Resource
    private LinkConditionMetaMapper linkConditionMetaMapper;
    @Resource
    private LinkTriggerMetaMapper linkTriggerMetaMapper;
    @Resource
    private LinkageCacheSupport linkageCacheSupport;

    @Override
    @Transactional
    public RuleCreateResponse projectRuleAdd(RuleCreateRequest ruleCreateRequest) {
        LinkConfig exitLinkConfig = this.getBaseMapper().selectOne(new LambdaQueryWrapper<LinkConfig>()
                                                                           .eq(LinkConfig::getNumber, ruleCreateRequest.getRuleNum()));
        Assert.isNull(exitLinkConfig, "exits number {" + ruleCreateRequest.getRuleNum() + "}");

        LinkConfig linkConfig = new LinkConfig();
        linkConfig.setId(platformEvnCom.nextIotPlatformSnowflakeId());
        linkConfig.setName(ruleCreateRequest.getRuleName());
        linkConfig.setNumber(ruleCreateRequest.getRuleNum());
        linkConfig.setConfig(ruleCreateRequest.getRuleConfig().toString());
        linkConfig.setStatus(Boolean.parseBoolean(ruleCreateRequest.getEnabled()) ? 1 : 0);
        linkConfig.setTriggerType("online");

        //save link config
        this.save(linkConfig);

        //拆分规则
        try {
            parse(linkConfig.getId(), true);
            return new RuleCreateResponse(String.valueOf(linkConfig.getId()), ruleCreateRequest.getRuleNum());
        } catch (Exception ex) {
            throw new ServiceException("联动规则执行解析错误", ex);
        }
    }

    @Override
    @Transactional
    public void ruleDelete(String ruleNum) {
        List<LinkConfig> configs = this.lambdaQuery()
                .eq(LinkConfig::getNumber, ruleNum)
                .eq(LinkConfig::getDeleted, 0)
                .list();

        if (Objects.isNull(configs)) {
            return;
        }

        for (LinkConfig config : configs) {
            remove(ruleNum, config.getId(), true);
        }
    }

    @Override
    public RuleDetailVo ruleDetail(String ruleNum) {
        LinkConfig linkConfig = this.getOne(new LambdaQueryWrapper<LinkConfig>().eq(LinkConfig::getNumber, ruleNum));

        if (Objects.isNull(linkConfig)) {
            return null;//淳哥要求返空
        }

        RuleDetailVo ruleDetailVo = new RuleDetailVo();
        ruleDetailVo.setRuleNum(ruleNum);
        ruleDetailVo.setRuleName(linkConfig.getName());
        ruleDetailVo.setRuleConfig(JSON.parseObject(linkConfig.getConfig()));
        ruleDetailVo.setEnabled(linkConfig.getStatus() == 1);
        return ruleDetailVo;
    }

    @Override
    @Transactional
    public void changeRuleState(String ruleNum, boolean enable) {
        LinkConfig linkConfig = this.getOne(new LambdaQueryWrapper<LinkConfig>().eq(LinkConfig::getNumber, ruleNum));

        if (Objects.isNull(linkConfig)) {
            return;
        }

        linkConfig.setStatus(enable ? 1 : 0);
        this.updateById(linkConfig);

        if (enable) {
            parse(linkConfig.getId(), false);
        } else {
            remove(ruleNum, linkConfig.getId(), false);
        }
    }

    private void parse(Long linkageId, boolean mateWrite2Db) {
        LinkageConfig linkageConfig = linkageCacheSupport.parseAndWriteCacheRule(linkageId);
        if (Objects.isNull(linkageConfig)) {
            return;
        }

        linkageCacheSupport.publishLinkageChangeEvent(LinkageCacheSupport.ADD_KEY, linkageId);

        List<SingleLinkConditionMeta> linkConditionMetas = linkageConfig.getLinkConditionMetas();
        List<LinkActionTriggerMeta> linkActionTriggerMetaList = linkageConfig.getLinkActionTriggerMetaList();

        //联动条件表达元数据
        if (Objects.nonNull(linkConditionMetas) && (!linkConditionMetas.isEmpty()) && mateWrite2Db) {
            for (SingleLinkConditionMeta singleLinkConditionMeta : linkConditionMetas) {
                LinkConditionMeta linkConditionMeta = new LinkConditionMeta();
                linkConditionMeta.setId(platformEvnCom.nextIotPlatformSnowflakeId());
                linkConditionMeta.setRuleNum(singleLinkConditionMeta.getRuleNum());
                linkConditionMeta.setRuleName(singleLinkConditionMeta.getRuleName());
                linkConditionMeta.setDeviceId(singleLinkConditionMeta.getDeviceId());
                linkConditionMeta.setPropCode(singleLinkConditionMeta.getPropCode());
                linkConditionMeta.setCompareType(singleLinkConditionMeta.getCompareType());
                linkConditionMeta.setCompareValue(compareVal2Str(singleLinkConditionMeta.getCompareValue()));
                linkConditionMeta.setDelayTime(singleLinkConditionMeta.getDelayTime());
                linkConditionMeta.setTransparentStatement(singleLinkConditionMeta.getTransparentStatement());
                linkConditionMetaMapper.insert(linkConditionMeta);
            }
        }

        //联动执行元数据
        if (Objects.nonNull(linkActionTriggerMetaList) && (!linkActionTriggerMetaList.isEmpty()) && mateWrite2Db) {
            for (LinkActionTriggerMeta linkActionTriggerMeta : linkActionTriggerMetaList) {
                LinkTriggerMeta eventMetaLinkTrigger = new LinkTriggerMeta();
                eventMetaLinkTrigger.setId(platformEvnCom.nextIotPlatformSnowflakeId());
                eventMetaLinkTrigger.setRuleNum(linkActionTriggerMeta.getRuleNum());
                eventMetaLinkTrigger.setRuleName(linkActionTriggerMeta.getRuleName());
                eventMetaLinkTrigger.setDeviceId(linkActionTriggerMeta.getDeviceId());
                eventMetaLinkTrigger.setPropCode(linkActionTriggerMeta.getPropCode());
                eventMetaLinkTrigger.setNeedValue(linkActionTriggerMeta.getNeedValue());
                eventMetaLinkTrigger.setTransparentStatement(linkActionTriggerMeta.getTransparentStatement());
                linkTriggerMetaMapper.insert(eventMetaLinkTrigger);
            }
        }
    }

    /**
     * 移除系统中联动的缓存
     *
     * @param ruleNum      规则编号
     * @param linkConfigId 数据库中联动配置的ID
     * @param deleteDb     是否删除对应的数据库记录
     */
    private void remove(String ruleNum, Long linkConfigId, boolean deleteDb) {
        //移除缓存先
        linkageCacheSupport.removeCacheRule(linkConfigId);
        linkageCacheSupport.publishLinkageChangeEvent(LinkageCacheSupport.DELETE, linkConfigId);

        //删除主配置信息
        if (!deleteDb) {
            return;
        }

        //删除联动配置信息
        DatabaseContextHolder.softDelete(this, linkConfigId);

        //删除缓存中元数据信息
        List<LinkConditionMeta> mateInfos = linkConditionMetaMapper.selectList(new LambdaQueryWrapper<LinkConditionMeta>()
                                                                                       .eq(LinkConditionMeta::getRuleNum, ruleNum));
        if (Objects.nonNull(mateInfos)) {
            for (LinkConditionMeta mateInfo : mateInfos) {
                DatabaseContextHolder.softDelete(LinkConditionMeta.class, mateInfo.getId());
            }
        }

        //删除DB中的触发信息
        List<LinkTriggerMeta> triggerMetas = linkTriggerMetaMapper.selectList(new LambdaQueryWrapper<LinkTriggerMeta>()
                                                                                      .eq(LinkTriggerMeta::getRuleNum, ruleNum));
        if (Objects.nonNull(triggerMetas)) {
            for (LinkTriggerMeta triggerMeta : triggerMetas) {
                DatabaseContextHolder.softDelete(LinkTriggerMeta.class, triggerMeta.getId());
            }
        }
    }
}
