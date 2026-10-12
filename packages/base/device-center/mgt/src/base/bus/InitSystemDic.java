package com.guhecloud.cube.iot.mgt.base.bus;

import com.alibaba.fastjson2.JSON;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.cube.biz.ouaa.client.message.TenantInitMessage;
import com.guhecloud.cube.iot.mgt.base.mapper.SysDictMapper;
import com.guhecloud.cube.iot.mgt.base.model.SysDict;
import com.guhecloud.cube.iot.mgt.base.model.SysDictItem;
import com.guhecloud.cube.iot.mgt.base.service.SysDictItemService;
import com.guhecloud.cube.iot.mgt.base.service.SysDictService;
import com.guhecloud.cube.iot.mgt.base.vo.PlatformMsg;
import com.guhecloud.cube.iot.mgt.comm.platform.PlatformEvnCom;
import com.guhecloud.cube.iot.mgt.comm.util.Assert;
import com.guhecloud.cube.iot.mgt.core.data.lang.env.InnerDicEnum;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.guhecloud.cube.iot.mgt.base.bus.RocketMqConst.IOT_GROUP;
import static com.guhecloud.cube.iot.mgt.base.bus.RocketMqConst.TENANT_INIT_TOPIC;

@Component
@RocketMQMessageListener(consumerGroup = IOT_GROUP + "_DIC_LISTEN", topic = TENANT_INIT_TOPIC, selectorType = SelectorType.TAG)
@Order(2000)
@Slf4j
@EventBusListen(order = 1)
public class InitSystemDic implements RocketMQListener<String> {

    @Resource
    private SysDictService sysDictService;
    @Resource
    private SysDictMapper sysDictMapper;
    @Resource
    private SysDictItemService sysDictItemService;
    @Resource
    private TenantClient tenantClient;
    @Resource
    private PlatformEvnCom platformEvnCom;

    @Resource
    private InitSystemCategoryDic initSystemCategoryDic;

    private void ensureTenantDic(TenantDTO tenantDTO) {
        log.info("ensureTenantDic for tenant:{}", tenantDTO.getCode());
        try {
            initEventTypeDic(tenantDTO);
        } catch (Exception e) {
            log.error("initEventTypeDic error", e);
        }

        try {
            initAccessModeDic(tenantDTO);
        } catch (Exception e) {
            log.error("initAccessModeDic error", e);
        }
        //try {
            //标准
            // initSystemCategoryDic.ensureTenantCategoryDic(tenantDTO);
        //} catch (Exception e) {
        //    log.error("initSystemCategoryDic error", e);
        //}
    }

    /**
     * 初始化系统字典<br/>
     * 系统字典跨越租户，所以只需要初始化一次
     */
    private void initSysDict() {
        List<SysDict> sysDicts = sysDictMapper.queryAllSysDictNoTenant();
        if (Objects.nonNull(sysDicts) && !sysDicts.isEmpty()) {
            return;
        }

        List<SysDict> allAccDictItem = buildSysDict();
        List<Long> existIds = new ArrayList<>(sysDicts.size());
        for (SysDict sysDict : sysDicts) {
            existIds.add(sysDict.getId());
        }

        for (SysDict sysDict : allAccDictItem) {

            if (existIds.contains(sysDict.getId())) {
                continue;
            }

            sysDictService.save(sysDict);
        }
    }

    /**
     * 初始化接入方式字典<br/>
     * //        1、对于网关节点，接入方式有：mqtt <br/>
     * //        2、网关子节点，接入方式有：any  <br/>
     *
     * @param tenantDTO
     */
    private void initAccessModeDic(TenantDTO tenantDTO) {
        List<SysDictItem> dbAll = new ArrayList<>(10);

        List<SysDictItem> sysDictItemList = sysDictItemService.queryDictItemByIdAndTenantId(InnerDicEnum.gateway_node_acc.id, tenantDTO.getCode());
        if (Objects.nonNull(sysDictItemList)) {
            dbAll.addAll(sysDictItemList);
        }

        sysDictItemList = sysDictItemService.queryDictItemByIdAndTenantId(InnerDicEnum.gw_child_node_acc.id, tenantDTO.getCode());
        if (Objects.nonNull(sysDictItemList)) {
            dbAll.addAll(sysDictItemList);
        }

        SessionContextHolder.requestTenant(tenantDTO.getCode());
        List<SysDictItem> allAccDictItem = buildAccDictItem(tenantDTO);

        for (SysDictItem sysDictItem : allAccDictItem) {
            boolean exist = false;
            for (SysDictItem dbItem : dbAll) {
                if (dbItem.getItemValue().equals(sysDictItem.getItemValue())) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                sysDictItemService.save(sysDictItem);
            }
        }
    }

    /**
     * 初始化事件类型字典<br/>
     * //        1、信息
     * //        2、告警
     * //        3、故障
     *
     * @param tenantDTO
     */
    private void initEventTypeDic(TenantDTO tenantDTO) {
        List<SysDictItem> dbAll = new ArrayList<>(10);

        List<SysDictItem> sysDictItemList = sysDictItemService.queryDictItemByIdAndTenantId(InnerDicEnum.event_type.id, tenantDTO.getCode());
        if (Objects.nonNull(sysDictItemList)) {
            dbAll.addAll(sysDictItemList);
        }

        sysDictItemList = sysDictItemService.queryDictItemByIdAndTenantId(InnerDicEnum.event_type.id, tenantDTO.getCode());
        if (Objects.nonNull(sysDictItemList)) {
            dbAll.addAll(sysDictItemList);
        }

        SessionContextHolder.requestTenant(tenantDTO.getCode());
        List<SysDictItem> allEventTypeDictItem = buildEventTypeDic(tenantDTO);

        for (SysDictItem sysDictItem : allEventTypeDictItem) {
            boolean exist = false;
            for (SysDictItem dbItem : dbAll) {
                if (dbItem.getItemValue().equals(sysDictItem.getItemValue())) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                sysDictItemService.save(sysDictItem);
            }
        }
    }

    /**
     * 构建系统字典
     *
     * @return 系统字典
     */
    private List<SysDict> buildSysDict() {
        List<SysDict> sysDicts = new ArrayList<>(10);
        for (InnerDicEnum value : InnerDicEnum.values()) {
            if (!value.cacheSupport) {
                continue;
            }

            SysDict sysDict = new SysDict();
            sysDict.setId(value.id);
            sysDict.setDictName(value.name);
            sysDict.setDictCode(value.value);
            sysDict.setStatus(0);
            sysDict.setType(0);
            sysDict.setRemark(value.name);

            sysDicts.add(sysDict);
        }
        return sysDicts;
    }

    private List<SysDictItem> buildEventTypeDic(TenantDTO tenantDTO) {
        List<SysDictItem> sysDictItemList = new ArrayList<>();
        //        1、信息
        //        2、告警
        //        3、故障

        SysDictItem info = new SysDictItem();
        info.setId(platformEvnCom.nextIotPlatformSnowflakeId());
        info.setDictId(InnerDicEnum.event_type.id);
        info.setItemParentId(0L);
        info.setItemSort(1);
        info.setItemLabel("信息");
        info.setItemValue("info");
        info.setRemark("info");
        sysDictItemList.add(info);

        SysDictItem alarm = new SysDictItem();
        alarm.setId(platformEvnCom.nextIotPlatformSnowflakeId());
        alarm.setDictId(InnerDicEnum.event_type.id);
        alarm.setItemParentId(0L);
        alarm.setItemSort(2);
        alarm.setItemLabel("告警");
        alarm.setItemValue("alarm");
        alarm.setRemark("alarm");
        sysDictItemList.add(alarm);

        SysDictItem fault = new SysDictItem();
        fault.setId(platformEvnCom.nextIotPlatformSnowflakeId());
        fault.setDictId(InnerDicEnum.event_type.id);
        fault.setItemParentId(0L);
        fault.setItemSort(3);
        fault.setItemLabel("故障");
        fault.setItemValue("fault");
        fault.setRemark("fault");
        sysDictItemList.add(fault);

        return sysDictItemList;
    }

    private List<SysDictItem> buildAccDictItem(TenantDTO tenantDTO) {
        List<SysDictItem> sysDictItemList = new ArrayList<>();

        SysDictItem mqtt = new SysDictItem();
        mqtt.setId(platformEvnCom.nextIotPlatformSnowflakeId());
        mqtt.setDictId(InnerDicEnum.gateway_node_acc.id);
        mqtt.setItemParentId(0L);
        mqtt.setItemSort(1);
        mqtt.setItemLabel("mqtt");
        mqtt.setItemValue("mqtt");
        mqtt.setRemark("mqtt");
        sysDictItemList.add(mqtt);

        SysDictItem any = new SysDictItem();
        any.setId(platformEvnCom.nextIotPlatformSnowflakeId());
        any.setDictId(InnerDicEnum.gw_child_node_acc.id);
        any.setItemParentId(0L);
        any.setItemSort(1);
        any.setItemLabel("any");
        any.setItemValue("any");
        any.setRemark("any");
        sysDictItemList.add(any);

        SysDictItem convertMqtt = new SysDictItem();
        convertMqtt.setId(platformEvnCom.nextIotPlatformSnowflakeId());
        convertMqtt.setDictId(InnerDicEnum.direct_node_acc.id);
        convertMqtt.setItemParentId(0L);
        convertMqtt.setItemSort(1);
        convertMqtt.setItemLabel("mqtt-convert");
        convertMqtt.setItemValue("mqtt-convert");
        convertMqtt.setRemark("mqtt-convert");
        sysDictItemList.add(convertMqtt);

        return sysDictItemList;
    }

    @Override
    public void onMessage(String message) {
        if (StringUtils.isBlank(message)) {
            log.warn("tenantInitMessage is blank, can't init tenant dic");
            return;
        }

        try {
            PlatformMsg platformMsg = JSON.parseObject(message, PlatformMsg.class);

            TenantInitMessage tenantInitMessage = JSON.parseObject(platformMsg.getPayload(), TenantInitMessage.class);

            Assert.isTrue(Objects.nonNull(tenantInitMessage),
                          "tenantInitMessage is blank, can't init tenant dic, message is :" + message);

            //响应租户初始化消息
            if (!platformEvnCom.isIotPlatform()) {
                return;
            }

            //模拟出一个tenantDTO,用于创建
            TenantDTO tenantDTO = new TenantDTO();
            tenantDTO.setCode(tenantInitMessage.getTenant());

            //阻塞创建
            ensureTenantDic(tenantDTO);
        } catch (Exception e) {
            log.error("tenantInitMessage is blank, can't init tenant dic, message is :" + message, e);
        }
    }

    @Subscribe
    @AllowConcurrentEvents
    public void run(ApplicationArguments args) throws Exception {
        //系统初始化时，检查租户字典
        if (!platformEvnCom.isIotPlatform()) {
            return;
        }

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.schedule(() -> {
            log.info("initSystemDic at {} time:", DateUtils.format(LocalDateTime.now()));
            SessionContextHolder.requestTenant(0);
            List<TenantDTO> tenantDTOS = tenantClient.publicListTenants("");

            tenantDTOS.stream()
                    .filter(TenantDTO::getEnabled)
                    .forEach(this::ensureTenantDic);

            initSysDict();
        }, 10, TimeUnit.MINUTES);
    }
}
