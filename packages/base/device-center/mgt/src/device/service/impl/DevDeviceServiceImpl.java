package com.guhecloud.cube.iot.mgt.device.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.iot.mgt.base.mapper.CommandRecordMapper;
import com.guhecloud.cube.iot.mgt.base.service.CommandInfoService;
import com.guhecloud.cube.iot.mgt.base.service.CommandLogService;
import com.guhecloud.cube.iot.mgt.base.service.CommandRecordService;
import com.guhecloud.cube.iot.mgt.comm.platform.PlatformEvnCom;
import com.guhecloud.cube.iot.mgt.comm.translate.DeviceConvert;
import com.guhecloud.cube.iot.mgt.comm.translate.ThingsConvert;
import com.guhecloud.cube.iot.mgt.comm.util.Assert;
import com.guhecloud.cube.iot.mgt.comm.util.ParameterCheck;
import com.guhecloud.cube.iot.mgt.core.data.lang.DeviceAuthAlgorithmEnum;
import com.guhecloud.cube.iot.mgt.core.data.lang.DeviceStatusEnum;
import com.guhecloud.cube.iot.mgt.core.data.lang.cmd.DownCmd;
import com.guhecloud.cube.iot.mgt.core.data.msg.device.DeviceInfoSyncMsg;
import com.guhecloud.cube.iot.mgt.core.data.msg.device.DeviceRemove;
import com.guhecloud.cube.iot.mgt.core.data.sys.DeviceCfgBind2Biz;
import com.guhecloud.cube.iot.mgt.core.data.sys.DeviceCodeNameChange;
import com.guhecloud.cube.iot.mgt.core.data.sys.IotInnerDataMsg;
import com.guhecloud.cube.iot.mgt.core.data.sys.ThirdPartyIdBind2Biz;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.DeviceDigestVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.DevicePropValueVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.DeviceState;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.ProductPropSchema;
import com.guhecloud.cube.iot.mgt.core.innerbus.SouthInnerBus;
import com.guhecloud.cube.iot.mgt.device.mapper.DevDataShareMapper;
import com.guhecloud.cube.iot.mgt.device.mapper.DevDeviceMapper;
import com.guhecloud.cube.iot.mgt.device.model.DevDevice;
import com.guhecloud.cube.iot.mgt.device.param.BatchSyncDeviceParam;
import com.guhecloud.cube.iot.mgt.device.param.BatchUpdateDeviceParam;
import com.guhecloud.cube.iot.mgt.device.param.DeviceCreateParam;
import com.guhecloud.cube.iot.mgt.device.param.DeviceCreateParamNew;
import com.guhecloud.cube.iot.mgt.device.param.DeviceListParam;
import com.guhecloud.cube.iot.mgt.device.param.DevicePageParam;
import com.guhecloud.cube.iot.mgt.device.param.DeviceUpdateParam;
import com.guhecloud.cube.iot.mgt.device.param.CommandHistoryPageParam;
import com.guhecloud.cube.iot.mgt.device.service.DevDataShareService;
import com.guhecloud.cube.iot.mgt.device.service.DevDeviceService;
import com.guhecloud.cube.iot.mgt.device.vo.CommandHistoryVo;
import com.guhecloud.cube.iot.mgt.device.vo.CommandLogVo;
import com.guhecloud.cube.iot.mgt.device.vo.DevDataShareEchoVo;
import com.guhecloud.cube.iot.mgt.device.vo.DevDataShareSummaryVo;
import com.guhecloud.cube.iot.mgt.device.vo.DeviceCreateEvent;
import com.guhecloud.cube.iot.mgt.device.vo.DeviceIdCountVo;
import com.guhecloud.cube.iot.mgt.device.vo.DeviceRemoveEvent;
import com.guhecloud.cube.iot.mgt.device.vo.DeviceRemoveInfo;
import com.guhecloud.cube.iot.mgt.device.vo.DeviceThinPairVo;
import com.guhecloud.cube.iot.mgt.device.vo.DeviceVo;
import com.guhecloud.cube.iot.mgt.things.mapper.MdProductMapper;
import com.guhecloud.cube.iot.mgt.things.model.MdProduct;
import com.guhecloud.cube.iot.mgt.things.param.ProdDetailQueryParam;
import com.guhecloud.cube.iot.mgt.things.server.MdProductService;
import com.guhecloud.cube.iot.mgt.things.vo.ProdNameAndNodeTypeVo;
import com.guhecloud.cube.iot.mgt.things.vo.ProductVo;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.guhecloud.cube.iot.mgt.base.service.CommandLogService.issueToIotPlatform;
import static com.guhecloud.cube.iot.mgt.core.data.lang.RocketConstToApps.IOT_DEVICE_CFG_BID;
import static com.guhecloud.cube.iot.mgt.core.data.lang.RocketConstToApps.IOT_DEVICE_NUMBER_NAME_CHANGE;
import static com.guhecloud.cube.iot.mgt.core.data.lang.RocketConstToApps.IOT_INNER_DATA_CHANGE;
import static com.guhecloud.cube.iot.mgt.core.data.lang.RocketConstToApps.IOT_THIRD_PARTY_ID_BIND;
import static com.guhecloud.cube.iot.mgt.core.def.cache.DeviceCacheSupport.DEVICE_PROP_KEY;
import static com.guhecloud.cube.iot.mgt.core.def.cache.DeviceCacheSupport.DEVICE_STATE_KEY;

@Service
public class DevDeviceServiceImpl extends ServiceImpl<DevDeviceMapper, DevDevice> implements DevDeviceService {

    @Resource
    private MdProductService productService;
    @Resource
    private PlatformEvnCom platformEvnCom;
    @Resource
    private DevDeviceMapper devDeviceMapper;
    @Resource
    private DevDataShareService devDataShareService;
    @Resource
    private DevDataShareMapper devDataShareMapper;
    @Resource
    private SouthInnerBus southInnerBus;
    @Resource
    private MdProductMapper mdProductMapper;
    @Resource
    private CommandInfoService commandInfoService;
    @Resource
    private CommandRecordService commandRecordService;
    @Resource
    private CommandLogService commandLogService;
    @Resource
    private CommandRecordMapper commandRecordMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    @Qualifier("iotCommonPool")
    private ExecutorService executorService;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public DeviceVo deviceDetail(Long deviceId) {
        DevDevice device = this.getById(deviceId);
        ParameterCheck.notNull(device, "设备不存在");

        ProdDetailQueryParam param = new ProdDetailQueryParam();
        param.setProdId(device.getProductId());
        param.setWithProp(true);
        param.setWithEvent(true);
        ProductVo productVo = productService.productDetail(param);

        DeviceVo deviceVo = convert2Vo(device, true, true);
        deviceVo.setProduct(productVo);

        DevDataShareSummaryVo dataShareSummary = devDataShareService.summary(deviceId);
        DevDataShareEchoVo dataShareEcho = new DevDataShareEchoVo();
        if (dataShareSummary != null) {
            dataShareEcho.setDataShareAsSourceList(dataShareSummary.getAsSourceList());
            dataShareEcho.setDataShareAsTargetList(dataShareSummary.getAsTargetList());
            dataShareEcho.setDataShareAsSourceCount(dataShareSummary.getAsSourceCount());
            dataShareEcho.setDataShareAsTargetCount(dataShareSummary.getAsTargetCount());
        }
        deviceVo.setDataShare(dataShareEcho);
        return deviceVo;
    }

    @Override
    public Pagination<DeviceVo> listPage(DevicePageParam devicePageParam) {
        Set<Long> prodIds = new HashSet<>();
        if (Objects.nonNull(devicePageParam.getProdCategoryId())) {
            List<Long> prodsTemp = mdProductMapper.queryProdIdByCategory(devicePageParam.getProdCategoryId());

            prodIds.addAll(prodsTemp);
        }

        //如果prodId不为空，以prodId为准
        if (Objects.nonNull(devicePageParam.getProdId())) {
            prodIds.add(devicePageParam.getProdId());
        }

        //产品ID&设备编码都不为空
        Page<DevDevice> page = null;
        LambdaQueryWrapper<DevDevice> queryWrapper = new LambdaQueryWrapper<>();
        //关联设备查询
        List<Long> ids = splitToLongList(devicePageParam.getDeviceIds());
        if (StringUtils.isNotBlank(devicePageParam.getDeviceIds()) && CollectionUtils.isNotEmpty(ids)) {
            queryWrapper.in(DevDevice::getId, ids);
        }
        if (devicePageParam.getIsMainDeviceOnly() != null && devicePageParam.getIsMainDeviceOnly()) {
            queryWrapper.isNull(DevDevice::getMainDeviceId);
        }
        page = devDeviceMapper.selectPage(PagingUtils.convert(devicePageParam), queryWrapper
                .in(!prodIds.isEmpty(), DevDevice::getProductId, prodIds)
                .eq(!ObjectUtils.isEmpty(devicePageParam.getStatus()), DevDevice::getStatus, devicePageParam.getStatus())
                .and(StringUtils.isNotBlank(devicePageParam.getDeviceNameOrNumber()),
                        l -> l.like(DevDevice::getDeviceNumber, devicePageParam.getDeviceNameOrNumber())
                                .or()
                                .like(DevDevice::getName, devicePageParam.getDeviceNameOrNumber()))
                .orderByDesc(DevDevice::getCreateTime));

        if (page.getRecords().isEmpty()) {
            return Pagination.empty();
        }

        List<DeviceVo> productVoList = page.getRecords()
                .stream()
                .map(device -> convert2Vo(device, true, false))
                .collect(Collectors.toList());

        fillDataShareCount(productVoList);
        return PagingUtils.convert(page, productVoList);
    }

    /**
     * 设备分页列表回显：批量统计并填充每台设备作为源/目标的数据共享配置数量，避免逐条查询造成的 N+1 问题。
     */
    private void fillDataShareCount(List<DeviceVo> deviceVos) {
        if (CollectionUtils.isEmpty(deviceVos)) {
            return;
        }

        List<Long> deviceIds = deviceVos.stream()
                .map(DeviceVo::getId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(deviceIds)) {
            return;
        }

        Requester requester = SessionContextHolder.getRequester();
        Integer tenant = requester == null ? null : requester.getTenant();

        Map<Long, Long> asSourceCountMap = Optional.ofNullable(devDataShareMapper.countAsSourceByDeviceIds(deviceIds, tenant))
                .orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .filter(r -> r.getDeviceId() != null && r.getCnt() != null)
                .collect(Collectors.toMap(DeviceIdCountVo::getDeviceId, DeviceIdCountVo::getCnt, (a, b) -> a));

        Map<Long, Long> asTargetCountMap = Optional.ofNullable(devDataShareMapper.countAsTargetByDeviceIds(deviceIds, tenant))
                .orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .filter(r -> r.getDeviceId() != null && r.getCnt() != null)
                .collect(Collectors.toMap(DeviceIdCountVo::getDeviceId, DeviceIdCountVo::getCnt, (a, b) -> a));

        for (DeviceVo deviceVo : deviceVos) {
            Long deviceId = deviceVo.getId();

            DevDataShareEchoVo dataShareEcho = Optional.ofNullable(deviceVo.getDataShare())
                    .orElseGet(() -> {
                        DevDataShareEchoVo echo = new DevDataShareEchoVo();
                        deviceVo.setDataShare(echo);
                        return echo;
                    });
            dataShareEcho.setDataShareAsSourceCount(Optional.ofNullable(asSourceCountMap.get(deviceId)).map(Long::intValue).orElse(0));
            dataShareEcho.setDataShareAsTargetCount(Optional.ofNullable(asTargetCountMap.get(deviceId)).map(Long::intValue).orElse(0));
        }
    }

    @Override
    public void saveDevice(DeviceCreateParam deviceCreateParam) {
        DevDevice dbDevice = devDeviceMapper.selectOne(new LambdaQueryWrapper<DevDevice>()
                                                               .eq(DevDevice::getDeviceNumber, deviceCreateParam.getDeviceNumber()));
        Assert.isTrue(Objects.isNull(dbDevice), "设备编号已存在");
        Assert.isTrue(checkDeviceIdentifier(deviceCreateParam.getDeviceIdentifier()), "边缘侧设备标识已存在");
        deviceSaveOrUpdate(deviceCreateParam);
    }

    /**
     * 添加设备
     * 有就直接返回id，没有就添加
     *
     * @param deviceCreateParam 设备信息
     * @return 设备id
     */
    @Override
    public Long addDevice(DeviceCreateParam deviceCreateParam) {
        DevDevice dbDevice = devDeviceMapper.selectOne(new LambdaQueryWrapper<DevDevice>()
                                                               .eq(DevDevice::getDeviceNumber, deviceCreateParam.getDeviceNumber()));
        if (dbDevice != null) {
            return dbDevice.getId();
        }
        return this.deviceSaveOrUpdate(deviceCreateParam);
    }

    /**
     * 保存/修改设备
     * 注意:这里只是保存逻辑，请进行前置验证后再调用
     *
     * @param deviceCreateParam 设备信息
     */
    private Long deviceSaveOrUpdate(DeviceCreateParam deviceCreateParam) {
        DevDevice device = new DevDevice();
        device.setId(platformEvnCom.nextSnowflakeId());
        device.setDeviceParentId(deviceCreateParam.getParentDeviceId());
        device.setDeviceNumber(deviceCreateParam.getDeviceNumber());
        device.setName(deviceCreateParam.getDeviceName());
        device.setProductId(deviceCreateParam.getProductId());
        device.setStatus(DeviceStatusEnum.NO_ACTIVE.value);
        device.setDeviceType(deviceCreateParam.getDeviceType());
        device.setEdgeDevIdentify(deviceCreateParam.getDeviceIdentifier());
        device.setRawDeviceId(deviceCreateParam.getRawDeviceId());
        device.setSalt(newUUid());
        device.setAuthAlgorithm(DeviceAuthAlgorithmEnum.hmacmd5.name());//默认算法
        device.setRemark(deviceCreateParam.getRemark());
        //关联设备处理
        if (StringUtils.isNotBlank(deviceCreateParam.getRelationDeviceIds())) {
            updateMainDeviceIdByIdStr(deviceCreateParam.getRelationDeviceIds(), device.getId());
            device.setSlaveDeviceIds(deviceCreateParam.getRelationDeviceIds());
        }

        saveOrUpdate(device);

        boolean isGateway = productService.isGateway(deviceCreateParam.getProductId());

        //推送事件出去,潜在的消费者为物联中心
        if (!isGateway) {
            DeviceCreateEvent deviceCreateEvent = new DeviceCreateEvent();
            deviceCreateEvent.setDeviceId(device.getId());
            deviceCreateEvent.setDeviceCode(device.getDeviceNumber());
            deviceCreateEvent.setDeviceName(device.getName());
            deviceCreateEvent.setProductId(device.getProductId());

            applicationEventPublisher.publishEvent(deviceCreateEvent);
        }
        return device.getId();
    }

    /**
     * 检查设备边缘侧标识是否已存在：tenant+deleted+deviceIdentifier
     */
    private boolean checkDeviceIdentifier(String deviceIdentifier) {
        if (StringUtils.isBlank(deviceIdentifier)) {
            return true;
        }
        var tenant = SessionContextHolder.getRequester().getTenant();
        var count = this.lambdaQuery().eq(DevDevice::getEdgeDevIdentify, deviceIdentifier)
                .eq(DevDevice::getDeleted, 0)
                .eq(tenant != null, DevDevice::getTenant, tenant)
                .count();
        return count == 0;
    }

    @Override
    public Collection<String> batchSaveDevice(List<DeviceCreateParamNew> modelList, Long parentId, Long productId) {
        if (modelList.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> errorMsg = new ArrayList<>();
        for (DeviceCreateParamNew deviceCreateParamNew : modelList) {
            String msg = saveEachDevice(deviceCreateParamNew.getDeviceName(), deviceCreateParamNew.getDeviceNumber(),
                    productId, parentId, null, deviceCreateParamNew.getDeviceIdentifier());
            if (StringUtils.isNotBlank(msg)) {
                errorMsg.add(msg);
            }
        }
        return errorMsg;
    }

    private String saveEachDevice(String deviceName, String deviceNumber, Long productId,
                                  Long parentId, Integer deviceType, String deviceIdentifier
    ) {
        DeviceCreateParam deviceCreateParam = new DeviceCreateParam();
        deviceCreateParam.setDeviceNumber(deviceNumber);
        deviceCreateParam.setDeviceName(deviceName);
        deviceCreateParam.setProductId(productId);
        deviceCreateParam.setParentDeviceId(parentId);
        deviceCreateParam.setDeviceIdentifier(deviceIdentifier);
        deviceCreateParam.setDeviceType(deviceType == null ? 1 : deviceType);
        deviceCreateParam.setRawDeviceId(deviceIdentifier);
        try {
            saveDevice(deviceCreateParam);
        } catch (Exception e) {
            return "设备导入失败" + deviceCreateParam.getDeviceName() + ":" + deviceCreateParam.getDeviceNumber() + ":" + e.getMessage();
        }
        return "";
    }

    private void updateMainDeviceId(Long mainDeviceId, List<Long> slaveDeviceIds) {
        LambdaUpdateWrapper<DevDevice> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(DevDevice::getId, slaveDeviceIds)
                .set(DevDevice::getMainDeviceId, mainDeviceId);
        devDeviceMapper.update(null, updateWrapper);
    }

    @Override
    public DeviceDigestVo queryDeviceDigest(Long deviceId) {
        return devDeviceMapper.queryDeviceCache(deviceId);
    }

    @Override
    public List<ProductPropSchema> queryProductPropSchema(Long prodId) {
        return mdProductMapper.queryProductPropSchema(prodId);
    }

    @Override
    public Optional<DevicePropValueVo> queryDevicePropCache(Long deviceId, String propCode) {
        String redisKey = DEVICE_PROP_KEY.replace("{device-id}", deviceId.toString());
        Object object = redisTemplate.opsForHash().get(redisKey, propCode);

        if (Objects.isNull(object)) {
            return Optional.empty();
        }

        String objStr = object.toString();
        if (StringUtils.isBlank(objStr)) {
            return Optional.empty();
        }

        JSONObject jsonObject = JSON.parseObject(objStr);
        if (Objects.isNull(jsonObject)) {
            return Optional.empty();
        }

        DevicePropValueVo devicePropValueVo = new DevicePropValueVo();
        devicePropValueVo.setTs(jsonObject.getLongValue("ts"));
        devicePropValueVo.setValue(jsonObject.getString("value"));
        devicePropValueVo.setPreValue(jsonObject.getString("preValue"));
        return Optional.of(devicePropValueVo);
    }

    @Override
    @Transactional
    public void updateDevice(DeviceUpdateParam deviceUpdateParam) {
        DevDevice device = this.getById(deviceUpdateParam.getDeviceId());
        Assert.notNull(device, "设备不存在");

        //对设备编码进行校验，如果相同给前端提示编码已经存在
        Assert.isTrue(!deviceNumberExists(deviceUpdateParam), "设备编号已存在");

        if (!Objects.equals(device.getSlaveDeviceIds(), deviceUpdateParam.getRelationDeviceIds())) {
            //更新子设备关联信息
            updateMainDeviceIdByIdStr(device.getSlaveDeviceIds(), null);//先将原本的子设备关联信息清空
            updateMainDeviceIdByIdStr(deviceUpdateParam.getRelationDeviceIds(), device.getId());
        }
        List<Message<?>> iotInnerDataMsgList = new ArrayList<>(2);
        boolean updateDeviceNumOrCode = false;
        //更新设备信息
        device.setSlaveDeviceIds(deviceUpdateParam.getRelationDeviceIds());
        device.setRawDeviceId(deviceUpdateParam.getRawDeviceId());
        device.setConfig(deviceUpdateParam.getConfig());
        if (StringUtils.isNotBlank(deviceUpdateParam.getDeviceNumber()) &&
                !device.getDeviceNumber().equals(deviceUpdateParam.getDeviceNumber()))
        {
            updateDeviceNumOrCode = true;
        }
        if (StringUtils.isNotBlank(deviceUpdateParam.getName()) && !device.getName().equals(deviceUpdateParam.getName())) {
            updateDeviceNumOrCode = true;
        }
        saveOrUpdate(device);

        if (updateDeviceNumOrCode) {
            BatchUpdateDeviceParam param = new BatchUpdateDeviceParam();
            BatchUpdateDeviceParam.UpdateInfo updateInfo = new BatchUpdateDeviceParam.UpdateInfo();
            updateInfo.setDeviceId(device.getId());
            updateInfo.setDeviceNumber(deviceUpdateParam.getDeviceNumber());
            updateInfo.setName(deviceUpdateParam.getName());
            param.setUpdateInfos(Collections.singletonList(updateInfo));
            batchUpdate(param);
        }
        //通知三方设备ID更新
        if (StringUtils.isNotBlank(device.getRawDeviceId())) {
            IotInnerDataMsg iotInnerDataMsg = new IotInnerDataMsg();
            iotInnerDataMsg.setType(IOT_THIRD_PARTY_ID_BIND);

            ThirdPartyIdBind2Biz bind2BizThirdPartyId = new ThirdPartyIdBind2Biz();
            bind2BizThirdPartyId.setDeviceId(deviceUpdateParam.getDeviceId());
            bind2BizThirdPartyId.setThirdPartyId(deviceUpdateParam.getRawDeviceId());
            bind2BizThirdPartyId.setEventSource(2);

            iotInnerDataMsg.setDataMsg(bind2BizThirdPartyId);

            iotInnerDataMsgList.add(MessageBuilder.withPayload(JSON.toJSONString(iotInnerDataMsg)).build());
        }

        //通知设备配置更新
        if (StringUtils.isNotBlank(deviceUpdateParam.getConfig())) {
            IotInnerDataMsg iotInnerDataMsg = new IotInnerDataMsg();
            iotInnerDataMsg.setType(IOT_DEVICE_CFG_BID);

            DeviceCfgBind2Biz bind2BizCfg = new DeviceCfgBind2Biz();
            bind2BizCfg.setDeviceId(deviceUpdateParam.getDeviceId());
            bind2BizCfg.setCfg(deviceUpdateParam.getConfig());
            bind2BizCfg.setEventSource(2);

            iotInnerDataMsg.setDataMsg(bind2BizCfg);

            iotInnerDataMsgList.add(MessageBuilder.withPayload(JSON.toJSONString(iotInnerDataMsg)).build());
        }
        //推送绑定事件到业务平台
        for (Message<?> message : iotInnerDataMsgList) {
            RocketMQContextHolder.send(IOT_INNER_DATA_CHANGE, message);
        }
    }

    //判断设备编码是否重复
    private boolean deviceNumberExists(DeviceUpdateParam param) {
        var count = devDeviceMapper.selectCount(
                new LambdaQueryWrapper<DevDevice>()
                        .eq(DevDevice::getDeviceNumber, param.getDeviceNumber())
                        .ne(ObjectUtils.isNotEmpty(param.getDeviceId()), DevDevice::getId, param.getDeviceId())
        );
        return count > 0;
    }

    /**
     * 更新设备信息并同步接入平台
     *
     * @param param 修改参数
     * @return 任务执行编号
     */
    @Override
    public String batchUpdate(BatchUpdateDeviceParam param) {
        Assert.notEmpty(param.getUpdateInfos(), "修改对象不能为空");
        //先执行更新操作
        exeBatchUpdate(param.getUpdateInfos());
        //按网关设备分组,gwDeviceId == 子设备列表
        Map<Long, List<DevDevice>> parentChildDeviceMap = new HashMap<>();
        //仅存网关/直连设备
        Map<Long, DevDevice> gwOrDirectDeviceMap = new HashMap<>();
        Map<Long, BatchUpdateDeviceParam.UpdateInfo> deviceUpdateInfo = new HashMap<>();

        for (BatchUpdateDeviceParam.UpdateInfo updateInfo : param.getUpdateInfos()) {
            DevDevice device = this.lambdaQuery().eq(DevDevice::getId, updateInfo.getDeviceId()).one();

            if (Objects.isNull(device)) {
                throw new ServiceException("设备不存在，ID：" + updateInfo.getDeviceId());
            }

            //如果是网关设备，则将子设备分组
            if (Objects.isNull(device.getDeviceParentId())) {
                //网关设备
                parentChildDeviceMap.put(device.getId(), new ArrayList<>());
                gwOrDirectDeviceMap.put(device.getId(), device);
            } else {
                //非网关设备，直接更新
                long parentId = device.getDeviceParentId();
                if (Objects.isNull(parentChildDeviceMap.get(parentId))) {
                    // 缓存中无父设备，尝试从数据库中查询
                    DevDevice parentDev = this.lambdaQuery().eq(DevDevice::getId, parentId).one();
                    if (Objects.isNull(parentDev)) {
                        throw new ServiceException("设备【" + device.getName() + "】不存在父设备，ID：" + updateInfo.getDeviceId());
                    }

                    parentChildDeviceMap.put(parentDev.getId(), new ArrayList<>());
                    gwOrDirectDeviceMap.put(parentDev.getId(), parentDev);
                }

                //将当前设备添加到父设备的子设备列表中
                parentChildDeviceMap.get(parentId).add(device);
            }
            //将设备更新信息存储到map中
            deviceUpdateInfo.put(device.getId(), updateInfo);
        }

        //生成命令任务
        String cmdSeq = String.valueOf(platformEvnCom.nextSnowflakeId());
        commandInfoService.startCmd(cmdSeq, DownCmd.DEV_INFO_SYNC, "设备信息修改任务");

        //按网关组织命令下发
        for (Map.Entry<Long, DevDevice> devDeviceEntry : gwOrDirectDeviceMap.entrySet()) {
            List<DevDevice> childDevices = parentChildDeviceMap.get(devDeviceEntry.getKey());
            DevDevice parentDevice = devDeviceEntry.getValue();
            BatchUpdateDeviceParam.UpdateInfo parentInfo = deviceUpdateInfo.computeIfAbsent(parentDevice.getDeviceParentId(),
                                                                                            k -> {
                                                                                                //这里不存在，说明不会对父设备/直连设备更新
                                                                                                BatchUpdateDeviceParam.UpdateInfo info = new BatchUpdateDeviceParam.UpdateInfo();
                                                                                                info.setDeviceId(parentDevice.getId());
                                                                                                info.setDeviceNumber(parentDevice.getDeviceNumber());
                                                                                                info.setName(parentDevice.getName());
                                                                                                return info;
                                                                                            });

            DeviceInfoSyncMsg msg = new DeviceInfoSyncMsg();
            msg.setInstanceId(String.valueOf(platformEvnCom.nextSnowflakeId()));
            msg.setCmdSeq(cmdSeq);
            msg.setTenantId(String.valueOf(parentDevice.getTenant()));
            msg.setSyncChildInfo(new ArrayList<>());
            msg.setSyncSelfInfo(buildDeviceInfo(parentDevice, parentInfo));

            StringBuilder translateDev = new StringBuilder();
            translateDev.append("同步网关设备/直连设备")
                    .append("【").append(parentDevice.getDeviceNumber()).append("】").append("\n")
                    .append("同步设备编码:【").append(parentDevice.getDeviceNumber()).append("===>").append(parentInfo.getDeviceNumber())
                    .append("】")
                    .append("\n")
                    .append("同步设备名称:【").append(parentDevice.getName()).append("===>").append(parentInfo.getName()).append("】")
                    .append("\n");

            for (DevDevice childDevice : childDevices) {
                BatchUpdateDeviceParam.UpdateInfo info = deviceUpdateInfo.get(childDevice.getId());

                msg.getSyncChildInfo().add(buildDeviceInfo(childDevice, info));
                translateDev.append("同步子设备")
                        .append("【").append(childDevice.getDeviceNumber()).append("】").append("\n")
                        .append("同步子设备编码:【").append(childDevice.getDeviceNumber()).append("===>").append(info.getDeviceNumber())
                        .append("】")
                        .append("\n")
                        .append("同步子设备名称:【").append(childDevice.getName()).append("===>").append(info.getName()).append("】")
                        .append("\n");
            }

            String translated = String.format("修改设备【%s】下的设备信息,共计%s个设备.",
                    parentDevice.getName(), msg.getSyncChildInfo().size() + 1);
            SessionContextHolder.requestTenant(parentDevice.getTenant());

            commandLogService.logCmd(cmdSeq, msg.getInstanceId(), issueToIotPlatform+":"+ translateDev);
            commandRecordService.startCmdRecord(cmdSeq, msg.getInstanceId(), translated, JSON.toJSONString(msg), null, null);

            //提交设备修改任务
            southInnerBus.process(msg);
        }

        return cmdSeq;
    }

    @Override
    public List<String> synDeviceByCache(BatchSyncDeviceParam syncDeviceParam) {
        Map<String, List<BatchSyncDeviceParam.SynDeviceInfo>> map = sortingEquipment(syncDeviceParam);
        List<String> errorMsg = new ArrayList<>();
        errorMsg.addAll(insertSyncDevice(map.get("insert"), syncDeviceParam));
        errorMsg.addAll(updateSyncDevice(map.get("update"), syncDeviceParam));
        return errorMsg;
    }

    @Override
    public List<DeviceVo> listInfoByIds(DeviceListParam param) {
        Set<Long> prodIds = new HashSet<>();
        if (Objects.nonNull(param.getProdCategoryId())) {
            List<Long> prodsTemp = mdProductMapper.queryProdIdByCategory(param.getProdCategoryId());

            prodIds.addAll(prodsTemp);
        }
        //如果prodId不为空，以prodId为准
        if (Objects.nonNull(param.getProdId())) {
            prodIds.add(param.getProdId());
        }
        List<DevDevice> vos = null;
        LambdaQueryWrapper<DevDevice> queryWrapper = new LambdaQueryWrapper<>();
        //关联设备查询
        List<Long> ids = splitToLongList(param.getDeviceIds());
        if (StringUtils.isNotBlank(param.getDeviceIds()) && CollectionUtils.isNotEmpty(ids)) {
            queryWrapper.in(DevDevice::getId, ids);
        }
        if (param.getIsMainDeviceOnly() != null && param.getIsMainDeviceOnly()) {
            queryWrapper.isNull(DevDevice::getMainDeviceId);
        }
        if (!prodIds.isEmpty() && StringUtils.isNotBlank(param.getDeviceNameOrNumber())) {
            vos = devDeviceMapper.selectList(queryWrapper
                                                     .in(DevDevice::getProductId, prodIds)
                                                     .eq(!ObjectUtils.isEmpty(param.getStatus()), DevDevice::getStatus, param.getStatus())
                                                     .and(StringUtils.isNotBlank(param.getDeviceNameOrNumber()),
                                                          l -> l.like(DevDevice::getDeviceNumber, param.getDeviceNameOrNumber())
                                                                  .or()
                                                                  .like(DevDevice::getName, param.getDeviceNameOrNumber()))
                                                     .orderByDesc(DevDevice::getCreateTime));
        } else {
            //产品ID 和 设备编码都可能为空
            queryWrapper.in(!prodIds.isEmpty(), DevDevice::getProductId, prodIds)
                    .eq(!ObjectUtils.isEmpty(param.getStatus()), DevDevice::getStatus, param.getStatus())
                    .or(StringUtils.isNotBlank(param.getDeviceNameOrNumber()),
                        l -> l.like(DevDevice::getDeviceNumber, param.getDeviceNameOrNumber())
                                .or()
                                .like(DevDevice::getName, param.getDeviceNameOrNumber()))
                    .orderByDesc(DevDevice::getCreateTime);

            vos = devDeviceMapper.selectList(queryWrapper);
        }
        if (ObjectUtils.isEmpty(vos)) {
            return new ArrayList<>();
        }
        return vos
                .stream()
                .map(device -> convert2Vo(device, true, false))
                .collect(Collectors.toList());
    }

    private List<String> updateSyncDevice(List<BatchSyncDeviceParam.SynDeviceInfo> updates, BatchSyncDeviceParam syncDeviceParam) {
        List<String> errorMsg = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(updates)) {
            List<BatchUpdateDeviceParam.UpdateInfo> arrayList = new ArrayList<>();
            updates.forEach(update -> {
                List<DevDevice> list = this.lambdaQuery().eq(DevDevice::getDeviceNumber, update.getDeviceNumber()).list();
                if (!update.getDeviceName().equals(list.get(0).getName())) {
                    BatchUpdateDeviceParam.UpdateInfo info = new BatchUpdateDeviceParam.UpdateInfo();
                    info.setDeviceId(list.get(0).getId());
                    info.setDeviceNumber(update.getDeviceNumber());
                    info.setName(update.getDeviceName());
                    arrayList.add(info);
                }
                if (ObjectUtils.isNotEmpty(arrayList)) {
                    BatchUpdateDeviceParam param = new BatchUpdateDeviceParam();
                    param.setUpdateInfos(arrayList);
                    batchUpdate(param);
                }
            });
        }
        return errorMsg;
    }

    private List<String> insertSyncDevice(List<BatchSyncDeviceParam.SynDeviceInfo> inserts, BatchSyncDeviceParam syncDeviceParam) {
        List<String> errorMsg = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(inserts)) {
            List<Future<String>> futures = new ArrayList<>();
            Requester requester = SessionContextHolder.getRequester();
            Principal principal = SecurityContextHolder.getPrincipal();
            inserts.forEach(insert -> {
                futures.add(executorService.submit(() ->
                                                   {
                                                       SessionContextHolder.setRequester(requester);//租户信息
                                                       SecurityContextHolder.setPrincipal(principal);//设置用户信息
                                                       return saveEachDevice(insert.getDeviceName(), insert.getDeviceNumber(),
                                                                             syncDeviceParam.getProductId(), syncDeviceParam.getParentDeviceId(), syncDeviceParam.getDeviceType(), null);
                                                   }));
            });
            try {
                for (Future<String> future : futures) {
                    String msg = future.get();
                    if (StringUtils.isNotBlank(msg)) {
                        errorMsg.add(msg);
                    }
                }
            } catch (Exception e) {
                log.error("get batch syn insert msg error:", e);
            }
        }
        return errorMsg;
    }

    private Map<String, List<BatchSyncDeviceParam.SynDeviceInfo>> sortingEquipment(BatchSyncDeviceParam syncDeviceParam) {
        Map<String, List<BatchSyncDeviceParam.SynDeviceInfo>> map = new HashMap<>();
        Set<String> existDevices = devDeviceMapper.queryDeviceCode(syncDeviceParam.getSynDevices());
        List<BatchSyncDeviceParam.SynDeviceInfo> updateDevices = new ArrayList<>();
        List<BatchSyncDeviceParam.SynDeviceInfo> insertDevices = new ArrayList<>();
        for (BatchSyncDeviceParam.SynDeviceInfo info : syncDeviceParam.getSynDevices()) {
            if (existDevices.contains(info.getDeviceNumber())) {
                updateDevices.add(info);
            } else {
                insertDevices.add(info);
            }
        }
        map.put("insert", insertDevices);
        map.put("update", updateDevices);
        return map;
    }

    private void exeBatchUpdate(List<BatchUpdateDeviceParam.UpdateInfo> updateInfos) {
        devDeviceMapper.batchUpdate(updateInfos);
        updateInfos.forEach(info -> {
            redisTemplate.delete(DEVICE_PROP_KEY.replace("{device-id}", String.valueOf(info.getDeviceId())));
            redisTemplate.delete(DEVICE_STATE_KEY.replace("{device-id}", String.valueOf(info.getDeviceId())));

            IotInnerDataMsg iotInnerDataMsg = new IotInnerDataMsg();
            iotInnerDataMsg.setType(IOT_DEVICE_NUMBER_NAME_CHANGE);
            DeviceCodeNameChange codeNameChange = new DeviceCodeNameChange();
            codeNameChange.setDeviceId(info.getDeviceId());
            codeNameChange.setDeviceNumber(info.getDeviceNumber());
            codeNameChange.setName(info.getName());
            codeNameChange.setEventSource(2);
            iotInnerDataMsg.setDataMsg(codeNameChange);
            RocketMQContextHolder.send(IOT_INNER_DATA_CHANGE, iotInnerDataMsg);
        });
    }

    private DeviceInfoSyncMsg.DeviceInfo buildDeviceInfo(DevDevice oldDevice, BatchUpdateDeviceParam.UpdateInfo info) {
        DeviceInfoSyncMsg.DeviceInfo deviceInfo = new DeviceInfoSyncMsg.DeviceInfo();
        deviceInfo.setDevNum(oldDevice.getDeviceNumber());
        deviceInfo.setDevId(String.valueOf(oldDevice.getId()));
        deviceInfo.setEdgeDevIdentify(oldDevice.getEdgeDevIdentify());
        if (StringUtils.isNotBlank(info.getDeviceNumber()) && StringUtils.isNotBlank(info.getName())) {
            DeviceInfoSyncMsg.DeviceConfig config = new DeviceInfoSyncMsg.DeviceConfig();
            config.setNewDevNum(info.getDeviceNumber());
            config.setNewDevName(info.getName());
            deviceInfo.setConfig(config);
        }
        return deviceInfo;
    }

    @Override
    public void batchDel(List<Long> ids) {
        //收集设备消息，通知南向
        List<DeviceRemove> allDeviceRemoves = new ArrayList<>();
        //以50个为批量删除单位
        for (int i = 0; i < ids.size(); i += 50) {
            List<Long> subIds = ids.subList(i, Math.min(i + 50, ids.size()));
            List<DeviceRemoveInfo> removeInfos = new ArrayList<>(64);
            for (Long id : subIds) {
                DevDevice device = getById(id);
                //更新关联主设备字段信息
                LambdaUpdateWrapper<DevDevice> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(DevDevice::getMainDeviceId, id)
                        .set(DevDevice::getMainDeviceId, null);
                devDeviceMapper.update(null, updateWrapper);
                //删除关联子设备信息
                cleanSlaveDeviceId(device.getId());
                //删除设备数据共享配置（设备作为源/目标的所有关联）
                devDataShareService.removeByDeviceId(id);

                DatabaseContextHolder.softDelete(DevDevice.class, id);

                //删除属性、状态
                redisTemplate.delete(DEVICE_PROP_KEY.replace("{device-id}", id.toString()));
                redisTemplate.delete(DEVICE_STATE_KEY.replace("{device-id}", id.toString()));

                if (!productService.isGateway(device.getProductId())) {
                    DeviceRemoveInfo deviceRemoveInfo = new DeviceRemoveInfo();
                    deviceRemoveInfo.setDeviceId(device.getId());
                    deviceRemoveInfo.setDeviceCode(device.getDeviceNumber());
                    deviceRemoveInfo.setDeviceName(device.getName());
                    deviceRemoveInfo.setProductId(device.getProductId());
                    removeInfos.add(deviceRemoveInfo);

                    allDeviceRemoves.add(new DeviceRemove(
                            String.valueOf(device.getDeviceParentId()),
                            String.valueOf(device.getId()),
                            Long.valueOf(device.getTenant()),
                            SecurityContextHolder.getPrincipal() != null
                                    ? String.valueOf(SecurityContextHolder.getPrincipal().getId()) : null,
                            "mgt 设备移除"
                    ));
                }
            }
            //推送事件出去,潜在的消费者为物联中心
            applicationEventPublisher.publishEvent(new DeviceRemoveEvent(removeInfos));
        }
        //所有批次数据库操作完成后，统一通知南向(iot-runtime)
        if (!allDeviceRemoves.isEmpty()) {
            southInnerBus.deviceRemove(allDeviceRemoves);
        }
    }

    @Override
    public List<DeviceThinPairVo> listDeviceByProductId(Long productId) {
        return this.getBaseMapper().listThinDeviceByProductId(productId);
    }

    @Override
    public String queryProdCategoryIdByDeviceId(Long deviceId) {
        DevDevice device = getById(deviceId);
        MdProduct product = productService.getById(device.getProductId());
        return String.valueOf(product.getProdCategoryId());
    }

    @Override
    public Long countByProdId(Long productId) {
        return this.getBaseMapper()
                .selectCount(new LambdaQueryWrapper<DevDevice>().eq(DevDevice::getProductId, productId));
    }

    @Override
    public String queryDeviceSecret(String deviceId) {
        DevDevice device = this.getById(deviceId);
        Assert.notNull(device, "设备不存在");

        return device.getSalt();
    }

    @Override
    public List<DeviceVo> listVisibleGateway() {
        List<DevDevice> list = getBaseMapper().selectVisibleGatewayDevice();
        if (list.isEmpty()) {
            return Collections.emptyList();
        }

        return list.stream()
                .map(device -> convert2Vo(device, false, false))
                .collect(Collectors.toList());
    }

    //更新关联主设备ID
    private void updateMainDeviceIdByIdStr(String deviceIds, Long mainDeviceId) {
        String commaString = deviceIds;
        //防止关联自己
        if (ObjectUtils.isNotEmpty(mainDeviceId) && deviceIds.contains(mainDeviceId.toString())) {
            commaString = removeIdFromCommaString(deviceIds, mainDeviceId);
        }
        Optional.ofNullable(commaString)
                .filter(StringUtils::isNotBlank)
                .map(this::splitToLongList)
                .ifPresent(slaveDeviceIds -> updateMainDeviceId(mainDeviceId, slaveDeviceIds));
    }

    private DeviceVo convert2Vo(DevDevice device, boolean fillProdNameAndNode, boolean fillParentNameIfExist) {
        DeviceVo deviceVo = new DeviceVo();
        deviceVo.setId(device.getId());
        deviceVo.setDeviceParentId(device.getDeviceParentId());
        deviceVo.setDeviceKey(device.getDeviceKey());
        deviceVo.setDeviceNumber(device.getDeviceNumber());
        deviceVo.setProductId(device.getProductId());
        deviceVo.setName(device.getName());
        deviceVo.setDeviceType(device.getDeviceType());
        deviceVo.setDeviceTypeName(DeviceConvert.deviceType(device.getDeviceType()));
        deviceVo.setActivationTime(device.getActivationTime());
        deviceVo.setActivationTimeStr(Optional.ofNullable(device.getActivationTime()).map(DateUtils::format).orElse(null));
        deviceVo.setCreateTime(device.getCreateTime());
        deviceVo.setCreateTimeStr(Optional.ofNullable(device.getCreateTime()).map(DateUtils::format).orElse(null));
        deviceVo.setConfig(device.getConfig());
        deviceVo.setRawDeviceId(device.getRawDeviceId());
        deviceVo.setSalt(device.getSalt());
        deviceVo.setRemark(device.getRemark());
        deviceVo.setSlaveDeviceIds(device.getSlaveDeviceIds());
        deviceVo.setMainDeviceId(device.getMainDeviceId());
        deviceVo.setEdgeDevIdentify(device.getEdgeDevIdentify());

        deviceVo.setStatus(device.getStatus());
        deviceVo.setStatusName(DeviceConvert.status(device.getStatus()));

        //long 时间转换为LocalDateTime
        deviceVo.setOnlineTime(device.getOnlineTime());
        if (device.getOnlineTime() != null) {
            deviceVo.setOnlineTimeStr(DateUtils.format(device.getOnlineTime()));
        }

        //填充产品名称和节点类型
        if (fillProdNameAndNode) {
            ProdNameAndNodeTypeVo prodNameAndNodeTypeVo = productService.queryProdNameAndNodeType(device.getProductId());
            deviceVo.setProductName(prodNameAndNodeTypeVo.getProdName());
            deviceVo.setNodeTypeName(ThingsConvert.toNodeTypeName(prodNameAndNodeTypeVo.getNodeType()));
            deviceVo.setNodeType(prodNameAndNodeTypeVo.getNodeType());
        }

        //填充父设备名称
        if (fillParentNameIfExist && Objects.nonNull(device.getDeviceParentId())) {
            DevDevice parentDevice = this.getById(device.getDeviceParentId());
            if (Objects.nonNull(parentDevice)) {
                deviceVo.setDeviceParentName(parentDevice.getName());
            }
        }

        return deviceVo;
    }

    /**
     * 查询设备状态
     *
     * @param deviceId 设备IDs
     * @return 设备状态
     */
    private Optional<DeviceState> fetchDeviceState(Long deviceId) {
        Object deviceStateStr = redisTemplate.opsForValue().get(DEVICE_STATE_KEY.replace("{device-id}", deviceId.toString()));
        if (Objects.nonNull(deviceStateStr)) {
            DeviceState state = JSON.parseObject(deviceStateStr.toString(), DeviceState.class);
            return Optional.of(state);
        }

        return Optional.empty();
    }

    //清理关联从设备信息
    public void cleanSlaveDeviceId(Long targetId) {
        // 1. 查询包含该 ID 的记录
        DevDevice delDe = getById(targetId);
        if (ObjectUtils.isEmpty(delDe) || StringUtils.isBlank(delDe.getMainDeviceId())) {
            return;
        }
        //查询主设备
        LambdaQueryWrapper<DevDevice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DevDevice::getMainDeviceId, delDe.getMainDeviceId());
        DevDevice mainDevice = devDeviceMapper.selectOne(queryWrapper);
        if (ObjectUtils.isNotEmpty(mainDevice)) {
            String updatedIds = removeIdFromCommaString(mainDevice.getSlaveDeviceIds(), targetId);
            // 更新数据库
            DevDevice update = new DevDevice();
            update.setId(mainDevice.getId());
            update.setSlaveDeviceIds(updatedIds);

            devDeviceMapper.updateById(update);
        }
    }

    /**
     * 从逗号分隔的字符串中移除指定 ID（Long 类型）
     */
    private String removeIdFromCommaString(String source, Long removeId) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        List<String> idList = StringUtils.split(source, ',');
        idList = idList.stream()
                .filter(StringUtils::isNotBlank)
                .filter(id -> !Objects.equals(id.trim(), String.valueOf(removeId)))
                .collect(Collectors.toList());
        return idList.isEmpty() ? null : String.join(",", idList);
    }

    private List<Long> splitToLongList(String str) {
        ArrayList<Long> longArrayList = new ArrayList<>();
        for (String s : StringUtils.split(str, ',')) {
            try {
                Long l = Long.valueOf(s);
                longArrayList.add(l);
            } catch (NumberFormatException e) {
            }
        }
        return longArrayList;
    }

    private String newUUid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public Pagination<CommandHistoryVo> getCommandHistoryPage(CommandHistoryPageParam param) {
        if (param.getStartTime() == null) {
            param.setStartTime(java.time.LocalDateTime.now().minusWeeks(1));
        }

        Page<CommandHistoryVo> page = new Page<>(param.getPageNumber(), param.getPageSize());
        Page<CommandHistoryVo> result = commandRecordMapper.selectCommandHistoryPage(page, param);
        if (result == null || result.getRecords().isEmpty()) {
            return Pagination.empty();
        }

        List<CommandHistoryVo> records = result.getRecords();
        List<Long> deviceIds = records.stream()
                .map(CommandHistoryVo::getDeviceId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        if (!deviceIds.isEmpty()) {
            List<DevDevice> devices = devDeviceMapper.selectBatchIds(deviceIds);
            if (devices != null && !devices.isEmpty()) {
                Map<Long, DevDevice> deviceMap = devices.stream()
                        .collect(Collectors.toMap(DevDevice::getId, d -> d, (k1, k2) -> k1));
                
                for (CommandHistoryVo vo : records) {
                    DevDevice device = deviceMap.get(vo.getDeviceId());
                    if (device != null) {
                        vo.setDeviceNumber(device.getDeviceNumber());
                        vo.setDeviceName(device.getName());
                    }
                }
            }
        }

        return PagingUtils.convert(result, records);
    }

    @Override
    public List<CommandLogVo> getCommandLogs(String instanceId) {
        Assert.isTrue(StringUtils.isNotBlank(instanceId), "实例ID不能为空");
        return commandRecordMapper.selectCommandLogs(instanceId);
    }

}
