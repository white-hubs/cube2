package com.guhecloud.cube.iot.mgt.core.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.iot.mgt.comm.translate.CommonLegacy;
import com.guhecloud.cube.iot.mgt.comm.translate.ConditionOperatorUtils;
import com.guhecloud.cube.iot.mgt.comm.util.Assert;
import com.guhecloud.cube.iot.mgt.core.data.exception.IotBusinessException;
import com.guhecloud.cube.iot.mgt.core.data.exception.IotParameterInvalidException;
import com.guhecloud.cube.iot.mgt.core.data.lang.SourceType;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.DeviceDigestVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.ProductPropSchema;
import com.guhecloud.cube.iot.mgt.core.data.vo.linkage.LinkageConfig;
import com.guhecloud.cube.iot.mgt.core.data.vo.linkage.SingleLinkConditionMeta;
import com.guhecloud.cube.iot.mgt.core.data.vo.linkage.SingleLinkConditionResult;
import com.guhecloud.cube.iot.mgt.core.data.vo.linkage.action.LinkActionTriggerMeta;
import com.guhecloud.cube.iot.mgt.core.data.vo.rule.ComboRule;
import com.guhecloud.cube.iot.mgt.core.data.vo.rule.Rule;
import com.guhecloud.cube.iot.mgt.core.data.vo.rule.SimpleRule;
import com.guhecloud.cube.iot.mgt.core.data.vo.rule.TriggerType;
import com.guhecloud.cube.iot.mgt.core.def.cache.DeviceCacheSupport;
import com.guhecloud.cube.iot.mgt.core.def.cache.LinkageCacheSupport;
import com.guhecloud.cube.iot.mgt.core.def.cache.ProductCacheSupport;
import com.guhecloud.cube.iot.mgt.north.mapper.LinkConditionMetaMapper;
import com.guhecloud.cube.iot.mgt.north.mapper.LinkConfigMapper;
import com.guhecloud.cube.iot.mgt.north.model.LinkConditionMeta;
import com.guhecloud.cube.iot.mgt.north.model.LinkConfig;
import com.guhecloud.cube.iot.mgt.north.server.tx.TransactionAfterCommitUtils;
import com.guhecloud.cube.iot.mgt.north.vo.LinkageCfgEventChange;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.guhecloud.cube.iot.mgt.core.def.LinkageConst.LINKAGE_EXPRESS;
import static com.guhecloud.cube.iot.mgt.core.def.LinkageConst.LINKAGE_MATE;
import static com.guhecloud.cube.iot.mgt.core.def.LinkageConst.LINKAGE_RESULT;
import static com.guhecloud.cube.iot.mgt.core.def.LinkageConst.buildSingleLinkExpress;
import static com.guhecloud.cube.iot.mgt.core.def.LinkageConst.buildSingleMateLink;
import static com.guhecloud.cube.iot.mgt.core.def.LinkageConst.compareVal2Str;
import static com.guhecloud.platform.base.util.StringUtils.isBlank;

@Component
@Slf4j
public class LinkageCacheSupportImpl implements LinkageCacheSupport {
    private static final String LINKAGE_EVENT_CHANNEL = "iot:v2:linkage:change";
    private static final ObjectMapper linkageCacheObjectMapper = new ObjectMapper();

    static {
        linkageCacheObjectMapper.findAndRegisterModules();
    }

    @Resource
    private LinkConfigMapper linkConfigMapper;
    @Resource
    private LinkConditionMetaMapper linkConditionMetaMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private DeviceCacheSupport deviceCacheSupport;
    @Resource
    private ProductCacheSupport productCacheSupport;

    @Override
    public LinkageConfig parseAndWriteCacheRule(Long linkConfigId) {
        LinkConfig linkConfig = linkConfigMapper.selectById(linkConfigId);
        if (Objects.isNull(linkConfig) || linkConfig.getStatus() == 0)//关闭
        {
            return null;
        }

        return parseAndWriteCacheRule(linkConfig, linkConfig.getConfig());
    }

    @Override
    public void removeCacheRule(Long linkConfigId) {
        LinkConfig config = linkConfigMapper.selectById(linkConfigId);
        if (Objects.isNull(config)) {
            log.error("removeCacheRule: linkConfigId={} not found", linkConfigId);
            return;
        }

        removeCacheRuleByEntity(config);
    }

    /**
     * 删除指定规则的缓存
     *
     * @param config 规则
     */
    private void removeCacheRuleByEntity(LinkConfig config) {
        //删除规则表达式
        redisTemplate.delete(LINKAGE_EXPRESS + config.getNumber());
        //删除规则比较的'结果
        redisTemplate.delete(LINKAGE_RESULT + config.getNumber());

        //删除缓存中元数据信息
        List<LinkConditionMeta> mateInfos = linkConditionMetaMapper.selectList(new LambdaQueryWrapper<LinkConditionMeta>()
                .eq(LinkConditionMeta::getRuleNum, config.getNumber()));
        if (Objects.nonNull(mateInfos)) {
            for (LinkConditionMeta mateInfo : mateInfos) {
                String deviceId = String.valueOf(mateInfo.getDeviceId());
                String propCode = mateInfo.getPropCode();

                List<Object> objects = redisTemplate.opsForList()
                        .range(LINKAGE_MATE + buildSingleMateLink(deviceId, propCode), 0, -1);
                redisTemplate.delete(LINKAGE_MATE + buildSingleMateLink(deviceId, propCode));

                objects = removeMetaByRuleNum(objects, config.getNumber());

                if (Objects.nonNull(objects) && !objects.isEmpty()) {
                    //重新保存
                    redisTemplate.delete(LINKAGE_MATE + buildSingleMateLink(deviceId, propCode));
                    redisTemplate.opsForList().leftPushAll(LINKAGE_MATE + buildSingleMateLink(deviceId, propCode), objects);
                }
            }
        }
    }

    /**
     * 将规则写入缓存系统，使其生效
     *
     * @param linkConfig 联动配置
     * @param ruleConfig 规则配置元数据
     */
    private LinkageConfig parseAndWriteCacheRule(LinkConfig linkConfig, String ruleConfig) {
        //缓存所有的联动元数据
        List<SingleLinkConditionMeta> tempConditionMeta = new ArrayList<>();

        LinkageConfig linkageConfig = toCommonModel(linkConfig);

        //拆分规则
        try {
            //操作之前先确保规则被删除。fixme: 这里其实形成了redis资源竞争，后期需要优化
            removeCacheRuleByEntity(linkConfig);

            // (d_1514139396510715905p_1514138847191109633)  &&  ( (d_1514520176583917569p_1475418765771513857) )
            JsonNode rootNode = linkageCacheObjectMapper.readTree(ruleConfig);
            JsonNode comboRuleJson = rootNode.get("rule");
            JsonNode actionJson = rootNode.get("action");
            ComboRule comboRule = linkageCacheObjectMapper.treeToValue(comboRuleJson, ComboRule.class);
            String rootDeviceId = comboRule.getDeviceId();//指定全局设备ID就不用指定规则中的设备ID

            //触发条件
            String express = parseRuleConfig(comboRule,
                    linkConfig.getNumber(),
                    linkConfig.getName(),
                    rootDeviceId, tempConditionMeta);

            //没有绑定规则
            if (isBlank(express) || "()".equals(express)) {
                log.warn("联动规则解析为空，linkConfigId={}", linkConfig.getId());
                return null;
            }

            //联动命令
            if (Objects.nonNull(actionJson) && !actionJson.isNull()) {
                List<LinkActionTriggerMeta> linkActionTriggerMetas = processOnlineTriggerElement(actionJson,
                        linkConfig.getNumber(),
                        linkConfig.getName());

                linkageConfig.setLinkActionTriggerMetaList(linkActionTriggerMetas);
            }

            //整个规则表达式
            redisTemplate.opsForValue().set(LINKAGE_EXPRESS + linkConfig.getNumber(), express);

            //保存单个规则元素
            boolean containsAndOpt = express.contains("&&");
            if (!tempConditionMeta.isEmpty()) {
                for (SingleLinkConditionMeta singleLinkConditionMeta : tempConditionMeta) {
                    String deviceId = String.valueOf(singleLinkConditionMeta.getDeviceId());
                    String propCode = singleLinkConditionMeta.getPropCode();

                    //整个表达式中都没有And动作，只要单个规则匹配整个规则就匹配
                    singleLinkConditionMeta.setExtendsOutMeta(containsAndOpt);

                    try {
                        redisTemplate.opsForList()
                                .rightPush(LINKAGE_MATE + buildSingleMateLink(deviceId, propCode), linkageCacheObjectMapper.writeValueAsString(singleLinkConditionMeta));
                    } catch (Exception e) {
                        throw new IotParameterInvalidException("序列化单个联动规则元数据失败", e);
                    }
                }
            }

            linkageConfig.setLinkConditionMetas(tempConditionMeta);
        } catch (Exception ex) {
            throw new IotParameterInvalidException("联动规则执行解析错误", ex);
        }

        return linkageConfig;
    }

    /**
     * 转换公共模型，方便外部模块使用
     *
     * @param linkConfig linkConfig
     * @return linkageConfig
     */
    private LinkageConfig toCommonModel(LinkConfig linkConfig) {
        LinkageConfig linkageConfig = new LinkageConfig();
        linkageConfig.setId(linkageConfig.getId());
        linkageConfig.setName(linkageConfig.getName());
        linkageConfig.setNumber(linkConfig.getNumber());
        linkageConfig.setTriggerType(linkConfig.getTriggerType());
        linkageConfig.setStatus(linkConfig.getStatus());
        linkageConfig.setConfig(linkConfig.getConfig());

        return linkageConfig;
    }

    /**
     * 处理联动规则中的触发元素
     *
     * @param comboRule    规则
     * @param ruleNum      规则ID
     * @param ruleName     规则名称
     * @param rootDeviceId 规则中的设备ID
     * @return 规则表达式
     */
    private String parseRuleConfig(ComboRule comboRule, String ruleNum, String ruleName, String rootDeviceId,
                                   List<SingleLinkConditionMeta> tempConditionMeta
    ) {
        String express = "";
        List<Rule> ruleList = comboRule.getRules();

        StringBuilder subExpress = new StringBuilder();
        String singleExpress = "";
        for (int i = 0; i < ruleList.size(); i++) {
            Rule rule = ruleList.get(i);

            TriggerType triggerType = rule.getTriggerType();
            switch (triggerType) {
                case SIMPLE:
                    SimpleRule simpleRule = (SimpleRule) rule;
                    singleExpress = parseAndSaveRule(simpleRule, ruleNum, ruleName, rootDeviceId, tempConditionMeta);
                    break;
                case COMBO:
                    singleExpress = "(" + parseRuleConfig((ComboRule) rule, ruleNum, ruleName, rootDeviceId, tempConditionMeta) + ")";
                    break;
                case NATURE:
                case SETTING:
                    //ignore
                    break;
            }

            boolean isFirstInArray = (i == 0);
            subExpress.append((isFirstInArray ? "" : translateOp(comboRule.getOp())))
                    .append(singleExpress);
        }

        return express + subExpress;
    }

    /**
     * 解析单个规则,最细粒度的rule
     *
     * @param simpleRule        规则
     * @param ruleNum           规则ID (number)
     * @param ruleName          规则名称
     * @param rootDeviceId      规则中的设备ID
     * @param tempConditionMeta tempConditionMeta
     * @return 规则表达式
     */
    private String parseAndSaveRule(SimpleRule simpleRule, String ruleNum, String ruleName, String rootDeviceId,
                                    List<SingleLinkConditionMeta> tempConditionMeta
    ) {
        String deviceId = StringUtils.isNotBlank(rootDeviceId) ? rootDeviceId : simpleRule.getDeviceId();
        String propCode = simpleRule.getPropCode();
        String compareType = simpleRule.getOp();
        List<String> values = simpleRule.getOprand();
        int delayTime = simpleRule.getDelayTime();

        Assert.isTrue(StringUtils.isNotBlank(deviceId), "必须指定设备ID");

        Optional<DeviceDigestVo> deviceDigestVo = deviceCacheSupport.queryDeviceDigestCache(Long.valueOf(deviceId));
        DeviceDigestVo digestVo = deviceDigestVo.orElseThrow(() -> new IotParameterInvalidException(
                ruleName + ":无法配置联动执行，设备不存在"));

        Optional<ProductPropSchema> productPropSchema = productCacheSupport.queryProductPropSchemaCache(digestVo.getProdId(), propCode);
        ProductPropSchema propSchema = productPropSchema.orElseThrow(() -> new IotParameterInvalidException(
                ruleName + ":无法配置联动执行，设备属性不存在"));

        String transparentStatement = ConditionOperatorUtils.toTransparentStatement(compareType,
                values.toArray(),
                propSchema.getDataConfig(),
                propSchema.getDataType());

        // put rule meta into temp
        SingleLinkConditionMeta singleLinkConditionMeta = new SingleLinkConditionMeta();
        singleLinkConditionMeta.setRuleNum(ruleNum);
        singleLinkConditionMeta.setRuleName(ruleName);
        singleLinkConditionMeta.setDeviceId(Long.valueOf(deviceId));
        singleLinkConditionMeta.setPropCode(propCode);
        singleLinkConditionMeta.setCompareType(compareType);
        singleLinkConditionMeta.setCompareValue(values.toArray());
        singleLinkConditionMeta.setDelayTime(delayTime);
        singleLinkConditionMeta.setExtendsOutMeta(true);//这里只是暂时，最终结果只能由解析完表达式来确认
        singleLinkConditionMeta.setTransparentStatement(digestVo.getDeviceName() + "." + propSchema.getPropName() + transparentStatement);
        tempConditionMeta.add(singleLinkConditionMeta);
        // 注意: LinkConditionMeta 在外层统一进行保存

        // save rule result
        String ruleExpress = buildSingleLinkExpress(deviceId, propCode, compareType, compareVal2Str(values.toArray()));
        SingleLinkConditionResult singleLinkConditionResult = SingleLinkConditionResult.builder()
                .tenantId(digestVo.getTenantId())
                .deviceId(Long.valueOf(digestVo.getDeviceId()))
                .deviceName(digestVo.getDeviceName())
                .eventId(ruleNum)
                .eventName(ruleName)
                .eventType("link_alarm")//先固定
                .compareType(compareType)
                .compareValue(values.toArray())
                .propertyCode(propSchema.getPropCode())
                .propertyName(propSchema.getPropName())
                .compareResult(false)
                .source(SourceType.automatic)
                .eventTime(new Date())
                .build();

        try {
            redisTemplate.opsForHash()
                    .put(LINKAGE_RESULT + ruleNum, ruleExpress, linkageCacheObjectMapper.writeValueAsString(singleLinkConditionResult));
        } catch (Exception e) {
            throw new IotParameterInvalidException("序列化联动规则结果失败", e);
        }

        return ruleExpress;
    }

    /**
     * 处理联动后需要执行的动作
     *
     * @param actions  动作
     * @param ruleId   规则ID (number)
     * @param ruleName 规则名称
     */
    private List<LinkActionTriggerMeta> processOnlineTriggerElement(JsonNode actions, String ruleId, String ruleName) {
        List<LinkActionTriggerMeta> linkActionTriggerMetas = new ArrayList<>();
        for (JsonNode actionNode : actions) {
            Map<String, String> action = linkageCacheObjectMapper.convertValue(actionNode, new TypeReference<>() {
            });

            String deviceId = action.get("deviceId");
            String propCode = action.get("propCode");
            String propertyValue = action.get("value");

            Optional<DeviceDigestVo> deviceDigestVo = deviceCacheSupport.queryDeviceDigestCache(Long.valueOf(deviceId));
            DeviceDigestVo digestVo = deviceDigestVo.orElseThrow(() -> new IotParameterInvalidException(
                    ruleName + ":无法配置联动执行，设备不存在"));

            Optional<ProductPropSchema> productPropSchema = productCacheSupport.queryProductPropSchemaCache(digestVo.getProdId(), propCode);
            ProductPropSchema propSchema = productPropSchema.orElseThrow(() -> new IotParameterInvalidException(
                    ruleName + ":无法配置联动执行，设备属性不存在"));

            if (isBlank(propertyValue)) {
                throw new IotBusinessException("trigger propertyValue 值不能为空");
            }

            String transparentStatement =
                    "=" + CommonLegacy.toLabelName(propSchema.getDataConfig(), propSchema.getDataType(), propertyValue);
            String transparentStatementMeta =
                    digestVo.getDeviceName() + "." + digestVo.getDeviceName() + transparentStatement;

            LinkActionTriggerMeta eventMetaLinkTrigger = new LinkActionTriggerMeta();
            eventMetaLinkTrigger.setRuleNum(ruleId);
            eventMetaLinkTrigger.setRuleName(ruleName);
            eventMetaLinkTrigger.setDeviceId(Long.valueOf(deviceId));
            eventMetaLinkTrigger.setPropCode(propCode);
            eventMetaLinkTrigger.setNeedValue(propertyValue);
            eventMetaLinkTrigger.setTransparentStatement(transparentStatementMeta);

            linkActionTriggerMetas.add(eventMetaLinkTrigger);
        }

        return linkActionTriggerMetas;
    }

    private String translateOp(String op) {
        if ("and".equals(StringUtils.lowerCase(op))) {
            return " && ";
        } else if ("or".equals(StringUtils.lowerCase(op))) {
            return " || ";
        } else {
            throw new IotParameterInvalidException("不支持的操作符");
        }
    }

    /**
     * 删除指定规则的元数据
     *
     * @param objects 元数据
     * @param ruleNum 规则ID
     * @return 元数据
     */
    private List<Object> removeMetaByRuleNum(List<Object> objects, String ruleNum) {
        if (Objects.isNull(objects) || objects.isEmpty()) {
            return objects;
        }

        List<Object> newObjects = new ArrayList<>();

        for (Object object : objects) {
            try {
                String linkConditionMetaStr = object.toString();
                if (Objects.nonNull(linkConditionMetaStr) && linkConditionMetaStr.contains(ruleNum)) {
                    newObjects.add(object);
                }
            } catch (Exception e) {
                log.warn("JSON解析元数据失败: {}", object.toString(), e);
            }
        }

        return newObjects;
    }

    /**
     * 发送联动配置变更事件到 Redis 通道
     *
     * @param eventType 事件类型：ADD、UPDATE、DELETE
     * @param linkageId 联动配置ID
     */
    public void publishLinkageChangeEvent(String eventType, Long linkageId) {
        TransactionAfterCommitUtils.executeAfterCommit(() -> {
            try {
                LinkageCfgEventChange event = new LinkageCfgEventChange();
                event.setEventType(eventType);
                event.setLinkageId(linkageId);
                event.setTimestamp(System.currentTimeMillis());

                String json = linkageCacheObjectMapper.writeValueAsString(event);

                redisTemplate.convertAndSend(LINKAGE_EVENT_CHANNEL, json);
                log.info("发送联动配置变更事件：{}", event);
            } catch (Exception e) {
                log.error("发送联动配置变更事件失败：eventType={}, linkageId={}", eventType, linkageId, e);
            }
        });
    }

}
