package com.guhecloud.cube.base.device.center.open.service;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.base.device.center.core.mapper.ExtDeviceAttrCmdMapper;
import com.guhecloud.cube.base.device.center.core.mapper.ExtDeviceAttrCmdStepMapper;
import com.guhecloud.cube.base.device.center.core.mapper.ExtDeviceCmdMapper;
import com.guhecloud.cube.base.device.center.core.model.ExtDeviceAttrCmd;
import com.guhecloud.cube.base.device.center.core.model.ExtDeviceAttrCmdStep;
import com.guhecloud.cube.base.device.center.core.model.ExtDeviceCmd;
import com.guhecloud.cube.base.device.center.metadata.lang.AttrCtlExecuteResultEnum;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePropWriteCmd;
import com.guhecloud.cube.base.device.center.metadata.util.DevicePropUtils;
import com.guhecloud.cube.base.device.center.metadata.util.Tuple2;
import com.guhecloud.cube.iot.north.bridge.client.IotCommandClient;
import com.guhecloud.cube.iot.north.bridge.client.IotDeviceClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.CommandResponseDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceDetailDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductPropDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.CommandParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceDetailParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceAttrCtlService {
    private final ExtDeviceAttrCmdMapper extDeviceAttrCmdMapper;
    private final ExtDeviceAttrCmdStepMapper extDeviceAttrCmdStepMapper;
    private final ExtDeviceCmdMapper extDeviceCmdMapper;

    private final DeviceCenterService deviceCenterService;

    private final IotCommandClient iotCommandClient;
    private final IotDeviceClient iotDeviceClient;

    private final RedisTemplate redisTemplate;

    /**
     * 属性命令下发
     *
     * @param devicePropWriteCmd 设备命令对象
     *
     * @return Long 批量执行命令ID
     */
    public Long propCommandDown(DevicePropWriteCmd devicePropWriteCmd) {
        if (Objects.isNull(devicePropWriteCmd)) {
            throw new IllegalArgumentException("设备命令对象不能为空");
        }

        if (!deviceCenterService.deviceExits(devicePropWriteCmd.getDeviceId())) {
            throw new IllegalArgumentException("设备实例ID不能为空");
        }

        if (Objects.isNull(devicePropWriteCmd.getPropWriteValues())) {
            throw new IllegalArgumentException("设备属性不能为空");
        }

        Long deviceId = devicePropWriteCmd.getDeviceId();

        //t1 Map<String, ProductPropDTO> 属性map key:属性code value:属性对象
        //t2 Map<String, Object> 设备快照
        Tuple2<Map<String, ProductPropDTO>, Map<String, Object>> productPropMap = queryDeviceDetailWithAttr(deviceId);

        //step1 生成命令序列
        Long batchCmdSeq = determinebatchCmdSeq(devicePropWriteCmd.getCommandSeqOptional());
        ExtDeviceCmd cmd = ExtDeviceCmd.create(batchCmdSeq);
        extDeviceCmdMapper.insert(cmd);

        //step2 生成多条属性命令
        for (DevicePropWriteCmd.PropWriteValue keyValue : devicePropWriteCmd.getPropWriteValues()) {
            // 属性翻译
            String attrCode = keyValue.getCode();
            String attrValueExpected = keyValue.getExpectedValue();

            ProductPropDTO propDto = productPropMap.t1.get(attrCode);
            if (Objects.isNull(propDto)) {
                throw new IllegalArgumentException("属性不存在,无法控制");
            }

            //实际值
            Object attrValueActualObj = productPropMap.t2.get(attrCode);
            String attrValueActual = Objects.isNull(attrValueActualObj) ? null : String.valueOf(attrValueActualObj);

            //期待值翻译
            String translationAttrValExpected = DevicePropUtils.iotPropVal2Chinese(propDto.getDataType(), attrValueExpected, propDto.getDataConstraint());
            String translationAttrValOriginal = DevicePropUtils.iotPropVal2Chinese(propDto.getDataType(), attrValueActual, propDto.getDataConstraint());

            String attrName = propDto.getPropName();
            Long commandSeq = propCommandDown(deviceId, attrCode, attrValueExpected);

            putTenantInfo2Cache(commandSeq);

            //构建属性命令记录实体
            ExtDeviceAttrCmd extDeviceAttrCmd = ExtDeviceAttrCmd.create(batchCmdSeq, commandSeq, deviceId, attrCode, attrName, attrValueExpected, attrValueActual);
            extDeviceAttrCmd.setExpectedValue(toJsonPair(attrName, translationAttrValExpected));
            extDeviceAttrCmd.setOriginalValue(toJsonPair(attrName, translationAttrValOriginal));
            extDeviceAttrCmd.setExecuteResult(AttrCtlExecuteResultEnum.ARRIVAL.name());
            extDeviceAttrCmdMapper.insert(extDeviceAttrCmd);

            //构建命令详情(多条执行详情)
            ExtDeviceAttrCmdStep extDeviceAttrCmdStep = ExtDeviceAttrCmdStep.create(commandSeq, AttrCtlExecuteResultEnum.ARRIVAL.name());
            extDeviceAttrCmdStepMapper.insert(extDeviceAttrCmdStep);
        }

        return batchCmdSeq;
    }

    /**
     * 将租户信息放入缓存,回调时获取
     *
     * @param batchCmdSeq 批量命令序列
     */
    private void putTenantInfo2Cache(Long batchCmdSeq) {
        Map<String, Object> tenant = new HashMap<>();
        tenant.put("tenant", SessionContextHolder.getRequester().getTenant());
        tenant.put("createBy", SecurityContextHolder.getPrincipal().getName());
        redisTemplate.opsForHash().putAll("controlAttributeCommandSeq:" + batchCmdSeq, tenant);
        redisTemplate.expire("controlAttributeCommandSeq:" + batchCmdSeq, 60, TimeUnit.MINUTES);
    }

    /**
     * 属性命令下发
     *
     * @param deviceId          设备ID
     * @param attrCode          属性code
     * @param attrValueExpected 期望值
     *
     * @return Long 命令序列
     */
    private Long propCommandDown(Long deviceId, String attrCode, String attrValueExpected) {
        CommandParam commandRequest = new CommandParam();
        commandRequest.setDeviceId(String.valueOf(deviceId));
        commandRequest.setPropCode(attrCode);
        commandRequest.setPropValue(attrValueExpected);
        CommandResponseDTO commandResponseDTO = iotCommandClient.projectCommandIssue(commandRequest);
        if (Objects.isNull(commandResponseDTO)) {
            throw new IllegalArgumentException("IOT提示:命令下发失败");
        }

        //注意:commandSeq === commandSeq;
        return Long.parseLong(commandResponseDTO.getCommandHandle());
    }

    /**
     * 转换为json键值对，用于兼容原有代码
     *
     * @param name  属性名
     * @param value 属性值
     *
     * @return String
     */
    private String toJsonPair(String name, String value) {
        return String.format("{\"name\":\"%s\",\"value\":\"%s\"}", name, value);
    }

    /**
     * 确定命令序列id
     *
     * @param commandSeqOptional 命令序列
     *
     * @return Long
     */
    public Long determinebatchCmdSeq(Long commandSeqOptional) {
        return Objects.isNull(commandSeqOptional) ? IdWorker.getId() : commandSeqOptional;
    }

    /**
     * 查询设备详情with属性
     *
     * @param deviceId 设备ID
     *                 return1 Map<String, ProductPropDTO> 属性map key:属性code value:属性对象
     *                 return2 Map<String, Object> 设备快照
     */
    public Tuple2<Map<String, ProductPropDTO>, Map<String, Object>> queryDeviceDetailWithAttr(Long deviceId) {
        DeviceDetailParam deviceDetailParam = new DeviceDetailParam();
        deviceDetailParam.setDeviceId(String.valueOf(deviceId));
        deviceDetailParam.setWithPropertySpec(true);
        deviceDetailParam.setWithSnapShot(true);
        DeviceDetailDTO iotDeviceDetail = iotDeviceClient.detail(deviceDetailParam);

        if (Objects.isNull(iotDeviceDetail)) {
            throw new IllegalArgumentException("设备不存在");
        }

        Map<String, ProductPropDTO> propMap = Collections.emptyMap();
        if (Objects.nonNull(iotDeviceDetail.getProductPropVoList())) {
            propMap = iotDeviceDetail.getProductPropVoList()
                    .stream()
                    .collect(Collectors.toMap(ProductPropDTO::getPropCode, Function.identity()));
        }

        Map<String, Object> propValMap = Collections.emptyMap();
        if (Objects.nonNull(iotDeviceDetail.getSnapshots())) {
            propValMap = iotDeviceDetail.getSnapshots()
                    .stream()
                    .filter(keyValueWithTsDTO -> StringUtils.isNotEmpty(keyValueWithTsDTO.getKey()) &&
                                                 Objects.nonNull(keyValueWithTsDTO.getValue()))
                    .collect(Collectors.toMap(KeyValueWithTsDTO::getKey, KeyValueWithTsDTO::getValue));
        }

        return new Tuple2<>(propMap, propValMap);
    }
}
