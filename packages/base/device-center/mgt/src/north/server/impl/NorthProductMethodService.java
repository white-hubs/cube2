package com.guhecloud.cube.iot.mgt.north.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.iot.mgt.base.mapper.CommandRecordMapper;
import com.guhecloud.cube.iot.mgt.base.service.CommandInfoService;
import com.guhecloud.cube.iot.mgt.base.service.CommandLogService;
import com.guhecloud.cube.iot.mgt.base.service.CommandRecordService;
import com.guhecloud.cube.iot.mgt.comm.translate.MethodSpecValidator;
import com.guhecloud.cube.iot.mgt.core.data.lang.cmd.CommandExecStatusEnum;
import com.guhecloud.cube.iot.mgt.core.data.lang.DeviceStatusEnum;
import com.guhecloud.cube.iot.mgt.core.data.lang.cmd.DownCmd;
import com.guhecloud.cube.iot.mgt.core.data.msg.device.PreDeviceMethodInvokeMsg;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.DeviceDigestVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.DeviceState;
import com.guhecloud.cube.iot.mgt.core.def.cache.DeviceCacheSupport;
import com.guhecloud.cube.iot.mgt.core.innerbus.SouthInnerBus;
import com.guhecloud.cube.iot.mgt.north.mapper.NorthDeviceMapper;
import com.guhecloud.cube.iot.mgt.north.vo.MethodInvokeHistoryVo;
import com.guhecloud.cube.iot.mgt.north.vo.MethodInvokeRequest;
import com.guhecloud.cube.iot.mgt.north.vo.MethodInvokeResponse;
import com.guhecloud.cube.iot.mgt.north.vo.MethodRawCmdContent;
import com.guhecloud.cube.iot.mgt.things.model.MdProductMethod;
import com.guhecloud.cube.iot.mgt.things.server.MdProductMethodService;
import com.guhecloud.cube.iot.mgt.north.vo.ProductMethodVo;
import com.guhecloud.cube.iot.mgt.things.vo.ProductMethodMgtVo;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ResourceNotFoundException;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.guhecloud.cube.iot.mgt.base.model.CommandInfo.methodInvokeCmdTranslatedContent;
import static com.guhecloud.cube.iot.mgt.base.model.CommandRecord.methodInvokeCmdToRawContent;
import static com.guhecloud.cube.iot.mgt.base.service.CommandLogService.issueToIotPlatform;
import static com.guhecloud.cube.iot.mgt.core.data.lang.cmd.DownCmd.METHOD_INVOKE;

/**
 * 产品方法北向服务
 */
@Service
@Slf4j
public class NorthProductMethodService {

    @Resource
    private MdProductMethodService mdProductMethodService;

    @Resource
    private DeviceCacheSupport deviceCacheSupport;

    @Resource
    private SouthInnerBus southInnerBus;

    @Resource
    private NorthDeviceMapper northDeviceMapper;

    @Resource
    private CommandInfoService commandInfoService;

    @Resource
    private CommandRecordService commandRecordService;

    @Resource
    private CommandLogService commandLogService;

    @Resource
    private CommandRecordMapper commandRecordMapper;

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 查询产品方法列表
     *
     * @param prodId 产品ID
     * @return 方法列表
     */
    public List<ProductMethodVo> listProductMethods(Long prodId) {
        Assert.isParameterError(prodId == null, "产品ID不能为空");
        List<ProductMethodMgtVo> productMethodMgtVos =  mdProductMethodService.listProdMethod(prodId);

        if (productMethodMgtVos == null || productMethodMgtVos.isEmpty()) {
            return Collections.emptyList();
        }

        return productMethodMgtVos.stream()
                .map(this::convert)
                .toList();
    }

    /**
     * 查询产品方法详情
     *
     * @param methodId 方法ID
     * @return 方法详情
     */
    public ProductMethodVo getMethodDetail(Long methodId) {
        Assert.isParameterError(methodId == null, "方法ID不能为空");
        ProductMethodMgtVo productMethodMgtVo = mdProductMethodService.prodMethodDetail(methodId);
        return convert(productMethodMgtVo);
    }

    /**
     * 调用设备方法
     *
     * @param request 调用请求
     * @return 调用响应
     */
    public MethodInvokeResponse invokeMethod(MethodInvokeRequest request) {
        // 1. 查询设备信息
        DeviceDigestVo deviceDigestVo = queryDeviceDigest(request.getDeviceId());

        MdProductMethod method = mdProductMethodService.getOne(new LambdaQueryWrapper<MdProductMethod>()
                        .eq(MdProductMethod::getMethodCode, request.getMethodCode())
                .eq(MdProductMethod::getProdId, deviceDigestVo.getProdId()));

        if (Objects.isNull(method)) {
            throw new ResourceNotFoundException("方法%s不存在".formatted(request.getMethodCode()));
        }

        // 2. 查询方法信息并验证
        ProductMethodMgtVo methodVo = mdProductMethodService.prodMethodDetail(method.getId());
        if (Objects.isNull(methodVo)) {
            throw new ServiceException("方法不存在");
        }

        // 验证方法是否属于设备的产品
        if (!Objects.equals(methodVo.getProdId(), deviceDigestVo.getProdId())) {
            throw new ServiceException("方法不属于该设备的产品");
        }

        // 验证方法是否启用
        if (methodVo.getStatus() != null && methodVo.getStatus() == 0) {
            throw new ServiceException("方法已禁用");
        }

        // 校验输入参数（基于数据库配置的规格定义）
        try {
            MethodSpecValidator.validateInputParams(methodVo.getInputParams(), request.getInputParams());
        } catch (ServiceException e) {
            // 参数校验失败，记录日志并返回错误
            log.error("方法调用参数校验失败: methodCode={}, deviceId={}, error={}",
                    request.getMethodCode(), request.getDeviceId(), e.getMessage());

            String commandSeq = buildCommandSeqIfNeed(request.getCommandSeq());
            MethodInvokeResponse errorResponse = new MethodInvokeResponse();
            errorResponse.setCommandSeq(commandSeq);
            errorResponse.setCommandHandle(commandSeq);
            errorResponse.setStatusCode(CommandExecStatusEnum.REJECT.code);
            errorResponse.setMessage("参数校验失败: " + e.getMessage());
            return errorResponse;
        }

        // 3. 生成命令序列号
        String commandSeq = buildCommandSeqIfNeed(request.getCommandSeq());

        // 4. 构建响应
        MethodInvokeResponse response = new MethodInvokeResponse();
        response.setCommandSeq(commandSeq);
        response.setCommandHandle(commandSeq);
        response.setMessage("执行中");
        response.setStatusCode(CommandExecStatusEnum.PROCESSING.code);

        // 5. 构建命令记录
        String translatedContent = methodInvokeCmdTranslatedContent(
                deviceDigestVo.getDeviceName(),
                methodVo.getName()
        );
        String rawCmdContent = methodInvokeCmdToRawContent(
                commandSeq,
                request.getDeviceId(),
                deviceDigestVo.getDeviceName(),
                methodVo.getMethodCode(),
                methodVo.getName(),
                methodVo.getMethodType(),
                request.getInputParams()
        );

        commandInfoService.startCmd(commandSeq, METHOD_INVOKE, translatedContent);
        commandRecordService.startCmdRecord(commandSeq, commandSeq, translatedContent, rawCmdContent, Long.valueOf(request.getDeviceId()), methodVo.getMethodCode());
        commandLogService.logCmd(commandSeq, commandSeq, issueToIotPlatform);

        // 6. 构建并下发方法调用消息
        PreDeviceMethodInvokeMsg invokeMsg = PreDeviceMethodInvokeMsg.builder()
                .commandSeq(commandSeq)
                .instanceId(commandSeq)
                .tenantId(deviceDigestVo.getTenantId())
                .deviceId(request.getDeviceId())
                .deviceName(deviceDigestVo.getDeviceName())
                .deviceNumber(deviceDigestVo.getDeviceNumber())
                .parentDeviceId(deviceDigestVo.getParentDeviceId())
                .parentDeviceNumber(deviceDigestVo.getParentDeviceNumber())
                .prodId(deviceDigestVo.getProdId())
                .protocolId(deviceDigestVo.getProtocolId())
                .methodCode(methodVo.getMethodCode())
                .methodName(methodVo.getName())
                .methodType(methodVo.getMethodType())
                .inputParams(request.getInputParams())
                .timeoutMs(methodVo.getTimeoutMs())
                .ts(System.currentTimeMillis())
                .build();

        try {
            log.info("preDeviceMethodInvokeMsg:{}", objectMapper.writeValueAsString(invokeMsg));
        } catch (Exception e) {
            log.warn("Failed to serialize invokeMsg to JSON: {}", e.getMessage());
        }
        southInnerBus.process(invokeMsg);

        return response;
    }

    /**
     * 查询方法调用状态
     *
     * @param instanceId 实例ID
     * @return 调用状态详情
     */
    public MethodRawCmdContent queryInvokeStatus(String instanceId) {
        Assert.isParameterError(StringUtils.isBlank(instanceId), "实例ID不能为空");

        // 查询命令记录,
        MethodInvokeHistoryVo vo = commandRecordMapper.getMethodInvokeStatus(instanceId, DownCmd.METHOD_INVOKE.getCode());

        if (vo == null) {
            throw new ResourceNotFoundException("未找到实例ID为[%s]的方法调用记录".formatted(instanceId));
        }

        // 解析原始命令内容为 MethodRawCmdContent 对象
        return parseRawCmdContent(vo);
    }

    /**
     * 解析原始命令内容
     *
     * @param vo 历史记录VO
     * @return MethodRawCmdContent 对象
     */
    private MethodRawCmdContent parseRawCmdContent(MethodInvokeHistoryVo vo) {
        if (StringUtils.isBlank(vo.getRawCmdContent())) {
            return new MethodRawCmdContent();
        }

        try {
            // 直接反序列化为 MethodRawCmdContent 对象
            // outputParams 由外部程序补充,
            return objectMapper.readValue(vo.getRawCmdContent(), MethodRawCmdContent.class);
        } catch (Exception e) {
            log.error("解析 rawCmdContent 失败: {}", vo.getRawCmdContent(), e);
            return new MethodRawCmdContent();
        }
    }

    /**
     * 生成命令序列号(如果未提供)
     *
     * @param commandSeq 命令序列号
     * @return 命令序列号
     */
    private String buildCommandSeqIfNeed(String commandSeq) {
        return StringUtils.isBlank(commandSeq) ? String.valueOf(IdWorker.getId()) : commandSeq;
    }

    /**
     * 查询设备摘要信息
     *
     * @param deviceIdStr 设备ID字符串
     * @return 设备摘要信息
     */
    private DeviceDigestVo queryDeviceDigest(String deviceIdStr) {
        Long deviceId = Long.valueOf(deviceIdStr);
        DeviceDigestVo deviceDigestVo = northDeviceMapper.queryDeviceCache(deviceId);
        Optional<DeviceState> deviceStateOptional = deviceCacheSupport.queryDeviceStateCache(deviceId);

        if (deviceStateOptional.isEmpty()) {
            log.error("NorthProductMethodService.queryDeviceDigest: deviceState is null, deviceId={}", deviceId);
            throw new ServiceException("设备[" + deviceIdStr + "] 未找到,或者未激活");
        }

        DeviceState deviceState = deviceStateOptional.get();

        if (deviceState.getStatus() == DeviceStatusEnum.NO_ACTIVE.value
                || deviceState.getStatus() == DeviceStatusEnum.DISABLED.value) {
            throw new ServiceException("设备[" + deviceIdStr + "] 未激活或则被禁用");
        }

        return deviceDigestVo;
    }

    // 转换方法管理VO到北向方法VO
    private ProductMethodVo convert(ProductMethodMgtVo productMethodMgtVo) {
        if (productMethodMgtVo == null) {
            return null;
        }

        ProductMethodVo vo = new ProductMethodVo();
        vo.setName(productMethodMgtVo.getName());
        vo.setMethodCode(productMethodMgtVo.getMethodCode());
        vo.setDescription(productMethodMgtVo.getDescription());
        vo.setMethodType(productMethodMgtVo.getMethodType());
        vo.setInputParams(productMethodMgtVo.getInputParams());
        vo.setOutputParams(productMethodMgtVo.getOutputParams());
        vo.setTimeoutMs(productMethodMgtVo.getTimeoutMs());
        return vo;
    }
}
