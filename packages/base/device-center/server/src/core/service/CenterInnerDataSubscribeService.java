package com.guhecloud.cube.base.device.center.core.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizDeviceMapper;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizTagRefMapper;
import com.guhecloud.cube.base.device.center.core.model.BizDevice;
import com.guhecloud.cube.base.device.center.core.model.BizTag;
import com.guhecloud.cube.base.device.center.core.model.BizTagRef;
import com.guhecloud.cube.base.device.center.core.vo.CameraDeviceConfirmed;
import com.guhecloud.cube.base.device.center.core.vo.DeviceCfgBind;
import com.guhecloud.cube.base.device.center.core.vo.RemoteThirdPartyIdBind;
import com.guhecloud.cube.base.device.center.metadata.std.param.CreateTagCmd;
import com.guhecloud.cube.iot.mgt.core.data.sys.DeviceCodeNameChange;
import com.guhecloud.cube.iot.mgt.device.vo.DeviceCreateEvent;
import com.guhecloud.cube.iot.mgt.device.vo.DeviceRemoveEvent;
import com.guhecloud.cube.iot.mgt.device.vo.DeviceRemoveInfo;
import com.guhecloud.cube.iot.mgt.things.vo.ProductChangeEvent;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.guhecloud.cube.base.device.center.metadata.lang.Const.IOT_INNER_DATA_CHANGE;
import static com.guhecloud.cube.base.device.center.metadata.lang.Const.VIDEO_DEVICE_TAG_CODE;
import static com.guhecloud.cube.iot.mgt.core.data.lang.RocketConstToApps.IOT_DEVICE_NUMBER_NAME_CHANGE;

/**
 * 处理一些内部数据<br/>
 * 1.三方id绑定事件
 * 2.。。。。。
 */
@RocketMQMessageListener(consumerGroup = "DEVICE_CENTER_INNER_DATA", topic = IOT_INNER_DATA_CHANGE, selectorType = SelectorType.TAG)
@Slf4j
@Component
public class CenterInnerDataSubscribeService implements RocketMQListener<String> {

    /**
     * 设备配置绑定
     */
    public static final String IOT_DEVICE_CFG_BID = "IOT_DEVICE_CFG_BID";
    /**
     * 三方设备ID发现
     */
    private static final String IOT_THIRD_PARTY_ID_BIND = "IOT_THIRD_PARTY_ID_BIND";
    /**
     * 摄像头设备确认
     */
    private static final String IOT_CAMERA_DEVICE_CONFIRMED = "IOT_CAMERA_DEVICE_CONFIRMED";
    @Resource
    private CenterBizDeviceMapper centerBizDeviceMapper;
    @Resource
    private CenterDeviceTagService centerDeviceTagService;
    @Resource
    private CenterDeviceService centerDeviceService;
    @Resource
    private CenterBizTagRefMapper tagRefMapper;

    @Override
    public void onMessage(String message) {
        //目前只有三方id绑定事件
        log.debug("inner data change message:{}", message);

        JSONObject jsonObject = JSON.parseObject(message);
        if (jsonObject.containsKey("payload")) {
            jsonObject = jsonObject.getJSONObject("payload");
        }

        String dataMsgStr = jsonObject.getString("type");
        if (StringUtils.isBlank(dataMsgStr)) {
            log.error("inner data change message type is null");
            return;
        }

        jsonObject = jsonObject.getJSONObject("dataMsg");

        if (IOT_THIRD_PARTY_ID_BIND.equals(dataMsgStr)) {
            RemoteThirdPartyIdBind remoteThirdPartyIdBind = jsonObject.toJavaObject(RemoteThirdPartyIdBind.class);
            //            centerBizDeviceMapper.updateThirdPartyId(remoteThirdPartyIdBind.getDeviceId(), remoteThirdPartyIdBind.getThirdPartyId());
            centerDeviceService.updateThirdPartyId(remoteThirdPartyIdBind);
        } else if (IOT_CAMERA_DEVICE_CONFIRMED.equals(dataMsgStr)) {
            handlerCameraDeviceConfirmed(jsonObject.toJavaObject(CameraDeviceConfirmed.class));
        } else if (IOT_DEVICE_CFG_BID.equals(dataMsgStr)) {
            DeviceCfgBind deviceCfgBind = jsonObject.toJavaObject(DeviceCfgBind.class);
            centerBizDeviceMapper.updateDeviceCfg(deviceCfgBind.getDeviceId(), deviceCfgBind.getCfg());
        } else if (IOT_DEVICE_NUMBER_NAME_CHANGE.equals(dataMsgStr)) {
            DeviceCodeNameChange codeNameChange = jsonObject.toJavaObject(DeviceCodeNameChange.class);
            centerDeviceService.changeDeviceName(codeNameChange);
        }
    }

    @EventListener
    public void iotDeviceCreate(DeviceCreateEvent deviceCreateEvent) {
        log.info("mgt创建设备,biz 同步:{}", deviceCreateEvent.getDeviceId());
        BizDevice bizDevice = new BizDevice();
        bizDevice.setId(deviceCreateEvent.getDeviceId());
        bizDevice.setDeviceName(deviceCreateEvent.getDeviceName());
        bizDevice.setDeviceCode(deviceCreateEvent.getDeviceCode());
        bizDevice.setDeviceIotProdId(deviceCreateEvent.getProductId());
        bizDevice.setDeviceState(0);//默认设备离线

        centerDeviceService.checkDeviceCodeUnique(deviceCreateEvent.getDeviceCode(), deviceCreateEvent.getDeviceId());
        centerDeviceService.saveUpdateDevice(bizDevice);
    }

    @EventListener
    public void iotProductChange(ProductChangeEvent productChangeEvent) {
        centerDeviceService.iotProductChange(productChangeEvent);
    }

    @EventListener
    public void iotDeviceRemove(DeviceRemoveEvent deviceRemoveEvent) {
        List<DeviceRemoveInfo> devices = deviceRemoveEvent.getDevices();
        List<Long> ids = new ArrayList<>();
        Integer tenant = null;
        for (DeviceRemoveInfo device : devices) {
            BizDevice oldDeviceInfo = centerBizDeviceMapper.getDeviceInfoNoTenant(device.getDeviceId());
            if (Objects.isNull(oldDeviceInfo)) {
                log.error("物联中心不能删除设备,设备不存在，设备id:{}", device.getDeviceId());
                return;
            }
            if (tenant == null) {
                tenant = oldDeviceInfo.getTenant();
            }
            ids.add(device.getDeviceId());
        }

        //上游通知删除设备不可靠，这里再次校验一下
        if (!ids.isEmpty()) {
            //响应设备移除动作
            SessionContextHolder.requestTenant(tenant);
            centerDeviceService.batchDel(ids);
        }
    }

    /**
     * 处理摄像头设备确认事件<br/>
     * 如果没有绑定视频标签，绑定视频标签
     *
     * @param cameraDeviceConfirmed 摄像头设备确认事件
     */
    private void handlerCameraDeviceConfirmed(CameraDeviceConfirmed cameraDeviceConfirmed) {
        Integer tenantId = centerDeviceTagService.findTenantIdByDeviceId(cameraDeviceConfirmed.getDeviceId());
        if (Objects.isNull(tenantId) || tenantId == 0) {
            return;
        }

        SessionContextHolder.requestTenant(tenantId);

        BizTag bizTag = centerDeviceTagService.createTagIfNeed(buildCreateTagParam());

        BizTagRef bizTagRef = tagRefMapper.selectOne(Wrappers.<BizTagRef>lambdaQuery()
                                                             .eq(BizTagRef::getBusinessKey, cameraDeviceConfirmed.getDeviceId())
                                                             .eq(BizTagRef::getTagId, bizTag.getId()));

        if (Objects.nonNull(bizTagRef)) {
            return;
        }

        //绑定视频标签
        BizTagRef tagRef = new BizTagRef();
        tagRef.setTagId(bizTag.getId());
        tagRef.setBusinessKey(cameraDeviceConfirmed.getDeviceId());
        EntityAuditor.populateInnerAudit(tagRef);
        tagRefMapper.insert(tagRef);
    }

    private CreateTagCmd buildCreateTagParam() {
        CreateTagCmd createTagParam = new CreateTagCmd();
        createTagParam.setTagCode(VIDEO_DEVICE_TAG_CODE);
        createTagParam.setTagName("视频标签");
        return createTagParam;
    }

}

