package com.guhecloud.cube.iot.mgt.things.server.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.iot.mgt.base.service.SysDictCacheService;
import com.guhecloud.cube.iot.mgt.comm.platform.PlatformEvnCom;
import com.guhecloud.cube.iot.mgt.comm.translate.ConditionOperatorUtils;
import com.guhecloud.cube.iot.mgt.comm.util.Assert;
import com.guhecloud.cube.iot.mgt.core.data.lang.env.InnerDicEnum;
import com.guhecloud.cube.iot.mgt.core.data.vo.rule.SettingRule;
import com.guhecloud.cube.iot.mgt.core.data.vo.rule.SimpleRule;
import com.guhecloud.cube.iot.mgt.core.data.vo.rule.TriggerType;
import com.guhecloud.cube.iot.mgt.things.mapper.MdProductEventMapper;
import com.guhecloud.cube.iot.mgt.things.mapper.MdProductEventMetaMapper;
import com.guhecloud.cube.iot.mgt.things.mapper.MdProductMapper;
import com.guhecloud.cube.iot.mgt.things.mapper.MdProductPropMapper;
import com.guhecloud.cube.iot.mgt.things.model.MdProductEvent;
import com.guhecloud.cube.iot.mgt.things.model.MdProductEventMeta;
import com.guhecloud.cube.iot.mgt.things.model.MdProductProp;
import com.guhecloud.cube.iot.mgt.things.server.MdProductEventService;
import com.guhecloud.cube.iot.mgt.things.vo.EventConfig;
import com.guhecloud.cube.iot.mgt.things.vo.ProductEventVo;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.guhecloud.cube.iot.mgt.comm.translate.ThingsConvert.toStatusName;
import static com.guhecloud.cube.iot.mgt.things.ProductUtils.incrementProductVersion;
import static com.guhecloud.platform.base.util.StringUtils.isNotBlank;

@Service
public class MdProductEventServiceImpl extends ServiceImpl<MdProductEventMapper, MdProductEvent>
        implements MdProductEventService {

    @Resource
    private PlatformEvnCom platformEvnCom;
    @Resource
    private SysDictCacheService sysDictCacheService;
    @Resource
    private MdProductEventMetaMapper productEventMetaMapper;
    @Resource
    private MdProductPropMapper productPropMapper;
    @Resource
    private MdProductMapper mdProductMapper;

    @Override
    public void saveProdEvent(ProductEventVo productEventVo, boolean pushCreateEvent) {
        assertEventCodeUnique(productEventVo.getProdId(), productEventVo.getEventCode());

        MdProductEvent mdProductEvent = convert2ProductEvent(productEventVo);
        mdProductEvent.setId(platformEvnCom.nextSnowflakeId());
        this.save(mdProductEvent);

        EventConfig eventConfig = productEventVo.getEventConfig();
        if (eventConfig.getTriggerType() == TriggerType.SIMPLE
                && eventConfig.getRules() instanceof SimpleRule simpleRule) {
            saveProductEventMeta(mdProductEvent, simpleRule);
        }

        notifyPropEventChange(mdProductEvent.getProdId(), mdProductEvent.getEventCode());
    }

    @Override
    public ProductEventVo prodEventDetail(Long eventId) {
        MdProductEvent mdProductEvent = this.getById(eventId);
        Assert.notNull(mdProductEvent, "事件不存在");

        return convertProductEventVo(mdProductEvent);
    }

    @Override
    public void updateProdEvent(ProductEventVo productEventVo) {
        Assert.notNull(productEventVo.getId(), "事件ID不能为空");
        MdProductEvent mdProductEvent = this.getById(productEventVo.getId());

        // 不允许修改事件code
        mdProductEvent.setCategoryId(productEventVo.getCategoryId());
        mdProductEvent.setName(productEventVo.getName());
        mdProductEvent.setRemark(productEventVo.getRemark());
        mdProductEvent.setStatus(productEventVo.getStatus());
        mdProductEvent.setConfig(productEventVo.getEventConfig());

        this.updateById(mdProductEvent);

        notifyPropEventChange(mdProductEvent.getProdId(), mdProductEvent.getEventCode());

    }

    @Override
    public void deleteProdEvent(Long eventId) {
        MdProductEvent mdProductEvent = this.getById(eventId);
        Assert.notNull(mdProductEvent, "事件不存在");

        DatabaseContextHolder.softDelete(this, eventId);

        //删除元数据信息
        MdProductEventMeta mdProductEventMeta = productEventMetaMapper.selectOne(new QueryWrapper<MdProductEventMeta>()
                                                                                         .lambda()
                                                                                         .eq(MdProductEventMeta::getEventId, eventId));
        if (Objects.nonNull(mdProductEventMeta)) {

            productEventMetaMapper.deleteById(mdProductEventMeta.getId());
        }

        if (platformEvnCom.isIotPlatform()) {
            notifyPropEventChange(mdProductEvent.getProdId(), mdProductEvent.getEventCode());
        }
    }

    /**
     * 通知属性事件变更
     *
     * @param productId
     * @param eventCode
     */
    private void notifyPropEventChange(Long productId, String eventCode) {
        incrementProductVersion(productId, mdProductMapper);
    }

    @Override
    public List<ProductEventVo> listProdEvent(Long prodId) {
        List<MdProductEvent> list = this.list(new LambdaQueryWrapper<>(MdProductEvent.class)
                                                      .eq(MdProductEvent::getProdId, prodId));

        if (Objects.isNull(list) || list.isEmpty()) {
            return Collections.emptyList();
        }

        return list.stream()
                .map(this::convertProductEventVo)
                .collect(Collectors.toList());
    }

    /**
     * 功能描述: 保存事件元
     *
     * @param productEvent productEvent
     * @param simpleRule   simpleRule
     *
     * @auther: fl
     * @date: 2022/10/10 14:22
     */
    public void saveProductEventMeta(MdProductEvent productEvent, SimpleRule simpleRule) {
        MdProductProp productProperty = productPropMapper.selectOne(new QueryWrapper<MdProductProp>().lambda()
                                                                            .eq(MdProductProp::getProdId, productEvent.getProdId())
                                                                            .eq(MdProductProp::getPropCode, simpleRule.getPropCode()));

        MdProductEventMeta eventMetaProp = new MdProductEventMeta();
        eventMetaProp.setId(platformEvnCom.nextSnowflakeId());
        eventMetaProp.setEventId(productEvent.getId());
        eventMetaProp.setProdId(productEvent.getProdId());
        eventMetaProp.setDeviceId(isNotBlank(simpleRule.getDeviceId()) ? Long.valueOf(simpleRule.getDeviceId()) : null);
        eventMetaProp.setPropCode(productProperty.getPropCode());
        eventMetaProp.setCompareType(simpleRule.getOp());
        eventMetaProp.setIsEnabled(1);
        eventMetaProp.setDelayTime(simpleRule.getDelayTime());

        String transparentStatement = ConditionOperatorUtils.toTransparentStatement(simpleRule.getOp(),
                                                                                    simpleRule.getOprand().toArray(),
                                                                                    productProperty.getDataConfig(),
                                                                                    productProperty.getDataType());

        String transparentStatementMeta = "【" + productProperty.getName() + "】属性，" + transparentStatement;
        eventMetaProp.setTransparentStatement(transparentStatementMeta);
        eventMetaProp.setCompareValue(simpleRule.getOprand().toArray());
        productEventMetaMapper.insert(eventMetaProp);
    }

    private ProductEventVo convertProductEventVo(MdProductEvent mdProductEvent) {
        ProductEventVo productEventVo = new ProductEventVo();
        productEventVo.setId(mdProductEvent.getId());
        productEventVo.setProdId(mdProductEvent.getProdId());
        productEventVo.setName(mdProductEvent.getName());
        productEventVo.setEventCode(mdProductEvent.getEventCode());
        productEventVo.setCategoryId(mdProductEvent.getCategoryId());
        productEventVo.setCategoryName(sysDictCacheService.dictItemLabelName(InnerDicEnum.event_type.id, mdProductEvent.getCategoryId()));
        productEventVo.setRemark(mdProductEvent.getRemark());
        productEventVo.setStatus(mdProductEvent.getStatus());
        productEventVo.setStatusName(toStatusName(mdProductEvent.getStatus()));

        productEventVo.setEventConfig(mdProductEvent.getConfig());
        return productEventVo;
    }

    /**
     * 转换一下
     *
     * @param productEventVo 事件VO
     *
     * @return 事件
     */
    private MdProductEvent convert2ProductEvent(ProductEventVo productEventVo) {
        MdProductEvent mdProductEvent = new MdProductEvent();
        mdProductEvent.setId(productEventVo.getId());
        mdProductEvent.setProdId(productEventVo.getProdId());
        mdProductEvent.setName(productEventVo.getName());
        mdProductEvent.setEventCode(productEventVo.getEventCode());
        mdProductEvent.setCategoryId(productEventVo.getCategoryId());
        mdProductEvent.setRemark(productEventVo.getRemark());
        mdProductEvent.setStatus(productEventVo.getStatus() == null ? 0 : productEventVo.getStatus());

        EventConfig eventConfig = productEventVo.getEventConfig();
        Assert.isTrue(eventConfig != null, "事件配置不能为空");
        Assert.notNull(eventConfig.getTriggerType(), "触发类型不能为空");

        if (eventConfig.getTriggerType() != TriggerType.NATURE) {
            Assert.notNull(eventConfig.getRules(), "规则不能为空");

            switch (eventConfig.getTriggerType()) {
                case SIMPLE:
                    checkSimpleRule((SimpleRule) eventConfig.getRules());
                    break;
                case SETTING:
                    checkSettingRule((SettingRule) eventConfig.getRules());
                    break;
                case COMBO:
                default:
                    break;
            }
        }
        mdProductEvent.setConfig(eventConfig);
        return mdProductEvent;
    }

    private void checkSettingRule(SettingRule settingRule) {

        Assert.notEmpty(settingRule.getSetValues(), "请检查event-rule配置，确保参数setValues存在");
        Assert.isTrue(isNotBlank(settingRule.getDeviceId()) || Objects.nonNull(settingRule.getProductId()),
                      "请检查event-rule配置，确保参数deviceId或productId存在");
    }

    private void checkSimpleRule(SimpleRule simpleRule) {

        Assert.isTrue((Objects.nonNull(simpleRule))
                      && isNotBlank(simpleRule.getPropCode())
                      && isNotBlank(simpleRule.getOp())
                      && Objects.nonNull(simpleRule.getOprand())
                      && (!simpleRule.getOprand().isEmpty()),
                      "请检查event-rule配置，确保参数propCode、op、oprandList存在");
    }

    private void assertEventCodeUnique(Long productId, String eventCode) {
        var count = this.count(new LambdaQueryWrapper<MdProductEvent>()
                                       .eq(MdProductEvent::getProdId, productId)
                                       .eq(MdProductEvent::getEventCode, eventCode));

        if (count > 0) {
            throw new ServiceException("事件标识[" + eventCode + "]已存在");
        }
    }
}
