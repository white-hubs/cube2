package com.guhecloud.cube.srv.gen.disinfection.server.member.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductCountDTO;
import com.guhecloud.cube.base.device.center.metadata.param.ChangeDeviceLntLatCmd;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterPageParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceProductCountQueryParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePropWriteCmd;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterAttrCtlStdClient;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.iot.north.bridge.client.IotDeviceClient;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.IotProductClient;
import com.guhecloud.cube.iot.north.bridge.client.IotProductMethodClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.MethodInvokeRequestDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.MethodInvokeResponseDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductPropDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceAddParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.MultiDevicePropParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.ProductPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.dao.DisinfectionMemberGroupDeviceMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberDeviceManualControlSetting;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroup;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroupDevice;
import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.ManualControlCommandEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.ManualControlMethodParamEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.MemberLogTypeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.member.event.DisinfectionMemberGroupDeviceBindingLogAddEvent;
import com.guhecloud.cube.srv.gen.disinfection.server.member.event.DisinfectionMemberGroupTotalEvent;
import com.guhecloud.cube.srv.gen.disinfection.server.member.event.DisinfectionMemberLogAddEvent;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.DeviceMethodControlCommandParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.DevicePropControlCommandParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.ManualControlParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberGroupDeviceCommandParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberGroupDeviceQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberDeviceManualControlSettingService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberGroupDeviceService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberGroupService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.util.Gcj02ToWgs84;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.ManualControlDurationSettingResult;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberDeviceMethodDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberDevicePageResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DisinfectionMemberGroupDeviceServiceImpl extends ServiceImpl<DisinfectionMemberGroupDeviceMapper, MemberGroupDevice> implements DisinfectionMemberGroupDeviceService {
    private static final String TIANDITU_GEOCODER_URL = "https://api.tianditu.gov.cn/geocoder";
    /**
     * 数据字典：itemCode=THIRD_SERVICE, code=TD_MAP；variable 为 JSON，取 webKey 作为天地图 tk
     */
    private static final String TIANDITU_TK_ITEM_CODE = "THIRD_SERVICE";
    private static final String TIANDITU_TK_CODE = "TD_MAP";

    private final IotProductClient iotProductClient;
    private final IotDevicePropClient iotDevicePropClient;
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final IotDeviceClient iotDeviceClient;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final DeviceCenterAttrCtlStdClient deviceCenterAttrCtlStdClient;
    private final DisinfectionMemberGroupService memberGroupService;
    private final DisinfectionMemberDeviceManualControlSettingService manualControlSettingService;
    private final IotProductMethodClient iotProductMethodClient;
    private final DataDictionaryClient dataDictionaryClient;
    private final RestTemplate restTemplate;

    /**
     * 设备属性控制批量
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean devicePropControlBatch(DevicePropControlCommandParam param) {
        /* 组装IOT指令*/
        var deviceAttrCtlCommand = new DevicePropWriteCmd();
        deviceAttrCtlCommand.setDeviceId(param.getDeviceId());
        var propWriteValues = new HashSet<DevicePropWriteCmd.PropWriteValue>();
        var stringBuffer = new StringBuffer();
        param.getPropWriteValueList().forEach(dto -> {
            var propWriteValue = new DevicePropWriteCmd.PropWriteValue();
            propWriteValue.setCode(dto.getCode());
            propWriteValue.setExpectedValue(dto.getExpectedValue());
            propWriteValues.add(propWriteValue);
            stringBuffer.append(dto.getName()).append("(").append(propWriteValue.getExpectedValue()).append(")").append("，");
        });
        deviceAttrCtlCommand.setPropWriteValues(propWriteValues);
        // 执行
        deviceCenterAttrCtlStdClient.controlAttribute(deviceAttrCtlCommand);

        /* 发送日志事件 */
        var detail = param.getDeviceName();
        if (!stringBuffer.isEmpty()) {
            var str = stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString();
            detail = detail + ":" + str;
        }
        var account = SecurityContextHolder.getPrincipal().getAccount();
        var name = SecurityContextHolder.getPrincipal().getName();
        var logAddEvent = new DisinfectionMemberLogAddEvent(this, account, MemberLogTypeEnum.USER_OPERATION_LOG, param.getDeviceId(), name, detail);
        applicationEventPublisher.publishEvent(logAddEvent);

        return true;
    }

    /**
     * 绑定单个设备(通过code绑定)
     * 全新的设备会先添加到物联，再绑定到组
     *
     * @param param 设备信息
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean bindDeviceOne(MemberGroupDeviceCommandParam param) {
        Assert.isParameterError(param.getGroupId() == null, "组织不能为空");
        Assert.isParameterError(StringUtils.isBlank(param.getDeviceCode()), "设备编码不能为空");
        //查询组是否存在
        var groupId = param.getGroupId();
        var exist = memberGroupService.lambdaQuery().eq(MemberGroup::getId, groupId).exists();
        Assert.isConflict(!exist, "组不存在!");
        /* 添加到物联（物联会没有就添加，有就直接返回id）*/
        var deviceAddParam = new DeviceAddParam();
        deviceAddParam.setProductId(param.getProductId());
        deviceAddParam.setDeviceNumber(param.getDeviceCode());
        var deviceId = iotDeviceClient.addDevice(deviceAddParam);
        // 判断是否已被绑定
        var groupDevice = this.getOne(Wrappers.<MemberGroupDevice>lambdaQuery()
                                              .eq(MemberGroupDevice::getDeviceId, deviceId)
                                              .last("limit 1"));
        Assert.isConflict(groupDevice != null, "设备已绑定");

        param.setDeviceIds(Collections.singletonList(deviceId));
        var bindingLogs = new ArrayList<DisinfectionMemberGroupDeviceBindingLogAddEvent.BindingLog>();
        bindingLogs.add(new DisinfectionMemberGroupDeviceBindingLogAddEvent.BindingLog(SecurityContextHolder.getPrincipal()
                                                                                               .getAccount(), param.getGroupId(), deviceId));
        //发送绑定事件
        applicationEventPublisher.publishEvent(new DisinfectionMemberGroupDeviceBindingLogAddEvent(this, bindingLogs));
        return this.bindDeviceBatch(param);
    }

    /**
     * 批量绑定设备(通过设备id绑定)
     * 将已经存在于物联中的设备绑定到组
     *
     * @param param 绑定信息
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean bindDeviceBatch(MemberGroupDeviceCommandParam param) {
        Assert.isParameterError(param.getGroupId() == null, "组织不能为空");
        Assert.isParameterError(CollectionUtils.isEmpty(param.getDeviceIds()), "设备不能为空");
        fillLocationByCoordinate(param);
        //查询组是否存在
        var groupId = param.getGroupId();
        var exist = memberGroupService.lambdaQuery().eq(MemberGroup::getId, groupId).exists();
        Assert.isConflict(!exist, "组不存在!");
        var groupDevices = this.list(Wrappers.<MemberGroupDevice>lambdaQuery()
                                             .in(MemberGroupDevice::getDeviceId, param.getDeviceIds()));
        Assert.isConflict(CollectionUtils.isNotEmpty(groupDevices), "设备已被绑定");
        // 组装po
        var insertList = new ArrayList<MemberGroupDevice>();
        param.getDeviceIds().forEach(deviceId -> {
            var po = new MemberGroupDevice();
            po.setId(IdWorker.getId());
            po.setGroupId(param.getGroupId());
            po.setDeviceId(deviceId);
            po.setLatitude(param.getLatitude());
            po.setLongitude(param.getLongitude());
            po.setDistrictLocation(param.getDistrictLocation());
            po.setLocationDetail(param.getLocationDetail());
            insertList.add(po);
        });
        var repList = this.list(Wrappers.<MemberGroupDevice>lambdaQuery().select(MemberGroupDevice::getDeviceId)
                                        .eq(MemberGroupDevice::getGroupId, param.getGroupId())
                                        .in(MemberGroupDevice::getDeviceId, param.getDeviceIds())
                               );
        if (CollectionUtils.isNotEmpty(repList)) {
            param.getDeviceIds().removeAll(repList.stream().map(MemberGroupDevice::getDeviceId).toList());
        }
        // 入库
        if (CollectionUtils.isNotEmpty(insertList)) {
            this.saveBatch(insertList);
            var event = new DisinfectionMemberGroupTotalEvent(this, param.getGroupId(), false, true);
            applicationEventPublisher.publishEvent(event);
            if (param.getLatitude() != null && param.getLongitude() != null || StringUtils.isNotBlank(param.getLocationDetail())) {
                insertList.forEach(po -> pushLntLatToDeviceCenter(po.getDeviceId(), param.getLatitude(), param.getLongitude(), param.getLocationDetail()));
            }
        }
        var bindingLogs = insertList
                .stream()
                .map(po -> new DisinfectionMemberGroupDeviceBindingLogAddEvent.BindingLog(SecurityContextHolder.getPrincipal()
                                                                                                  .getAccount(), po.getGroupId(), po.getDeviceId()))
                .collect(Collectors.toList());
        //发送绑定事件
        applicationEventPublisher.publishEvent(new DisinfectionMemberGroupDeviceBindingLogAddEvent(this, bindingLogs));
        return true;
    }

    /**
     * 将绑定位置同步到设备中心扩展表 {@code lnt_and_lat}、{@code location_des}（与 {@link ChangeDeviceLntLatCmd} 一致）
     */
    private void pushLntLatToDeviceCenter(Long deviceId, Double latitude, Double longitude, String locationDetail) {
        if (deviceId == null) {
            return;
        }
        var hasCoords = latitude != null && longitude != null;
        var hasLocation = StringUtils.isNotBlank(locationDetail);
        if (!hasCoords && !hasLocation) {
            return;
        }
        try {
            var cmd = new ChangeDeviceLntLatCmd();
            cmd.setDeviceId(deviceId);
            if (hasCoords) {
                cmd.setLntAndLat(longitude + "," + latitude);
            }
            if (hasLocation) {
                cmd.setLocationDes(locationDetail);
            }
            iotDeviceClient.changeDeviceLntLat(cmd);
        } catch (Exception e) {
            log.warn("同步设备中心经纬度/位置失败: deviceId={}", deviceId, e);
        }
    }

    /**
     * 按前端传入的腾讯地图坐标（GCJ-02）调用天地图逆地理：请求前转为 WGS84；{@code param} 内经纬度不修改，入库与同步设备中心仍为腾讯坐标。
     */
    private void fillLocationByCoordinate(MemberGroupDeviceCommandParam param) {
        if (param.getLatitude() == null || param.getLongitude() == null) {
            return;
        }
        var tk = getTiandituTk();
        if (StringUtils.isBlank(tk)) {
            return;
        }
        try {
            double[] wgs84 = Gcj02ToWgs84.convert(param.getLatitude(), param.getLongitude());
            var postStr = "{\"lon\":" + wgs84[1] + ",\"lat\":" + wgs84[0] + ",\"ver\":1}";
            var url = TIANDITU_GEOCODER_URL + "?postStr=" + URLEncoder.encode(postStr, StandardCharsets.UTF_8)
                      + "&type=geocode&tk=" + tk;
            var resp = restTemplate.getForObject(url, String.class);
            if (StringUtils.isBlank(resp)) {
                return;
            }
            var root = JsonUtils.deserialize(resp);
            if (root == null || !"0".equals(JsonUtils.getNodeValue(root, "status"))) {
                return;
            }
            var resultNode = root.get("result");
            if (resultNode == null) {
                return;
            }
            var formattedAddress = JsonUtils.getNodeValue(resultNode, "formatted_address");
            if (StringUtils.isNotBlank(formattedAddress)) {
                param.setLocationDetail(formattedAddress);
            }
            var addressComponentNode = resultNode.get("addressComponent");
            if (addressComponentNode == null) {
                return;
            }
            var district = JsonUtils.getNodeValue(addressComponentNode, "county");
            if (StringUtils.isBlank(district)) {
                district = JsonUtils.getNodeValue(addressComponentNode, "district");
            }
            if (StringUtils.isBlank(district)) {
                district = JsonUtils.getNodeValue(addressComponentNode, "city");
            }
            if (StringUtils.isNotBlank(district)) {
                param.setDistrictLocation(district);
            }
        } catch (Exception e) {
            log.warn("绑定设备时逆地理解析失败: lat={}, lon={}", param.getLatitude(), param.getLongitude(), e);
        }
    }

    private String getTiandituTk() {
        try {
            var query = new DataDictionaryaramByItemAndCodeParam();
            query.setItemCode(TIANDITU_TK_ITEM_CODE);
            query.setCode(TIANDITU_TK_CODE);
            var dict = dataDictionaryClient.getDataDictionaryByItemAndCode(query);
            if (dict == null || StringUtils.isBlank(dict.getVariable())) {
                return null;
            }
            var node = JsonUtils.deserialize(dict.getVariable());
            if (node == null) {
                return null;
            }
            var tk = JsonUtils.getNodeValue(node, "serverKey");
            return StringUtils.isNotBlank(tk) ? tk : null;
        } catch (Exception e) {
            log.warn("读取天地图TK失败", e);
            return null;
        }
    }

    /**
     * 批量解绑设备
     *
     * @param param 解绑信息
     *
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean unbindDeviceBatch(MemberGroupDeviceCommandParam param) {
        Assert.isParameterError(param.getGroupId() == null, "分组不能为空");
        if (CollectionUtils.isEmpty(param.getDeviceIds())) {
            log.warn("移除设备为空无需操作：{}", param.getGroupId());
            return true;
        }

        this.remove(Wrappers.<MemberGroupDevice>lambdaQuery()
                            .eq(MemberGroupDevice::getGroupId, param.getGroupId())
                            .in(MemberGroupDevice::getDeviceId, param.getDeviceIds()));
        // 修改组人数
        var event = new DisinfectionMemberGroupTotalEvent(this, param.getGroupId(), false, true);
        applicationEventPublisher.publishEvent(event);
        return true;
    }

    /**
     * 获取分组下产品设备数量
     *
     * @return 产品设备数量集合
     */
    @Override
    public List<DeviceProductCountDTO> getGroupDeviceProductList(MemberGroupDeviceQueryParam param) {
        var productDTOList = this.getProductAllList();
        if (productDTOList.isEmpty()) {
            return Collections.emptyList();
        }
        /* 获取消杀设备id */
        var deviceIdSet = new HashSet<Long>();
        var memberGroupDeviceList = this.list(Wrappers.<MemberGroupDevice>lambdaQuery()
                                                      .eq(MemberGroupDevice::getGroupId, param.getGroupId()));
        if (CollectionUtils.isNotEmpty(memberGroupDeviceList)) {
            var deviceIdList = memberGroupDeviceList.stream().map(MemberGroupDevice::getDeviceId).toList();
            deviceIdSet.addAll(deviceIdList);
        } else {
            deviceIdSet.add(-1L);
        }
        /* 查询产品设备数量 */
        var countQueryParam = new DeviceProductCountQueryParam();
        countQueryParam.setDeviceIds(deviceIdSet);
        return deviceCenterStdClient.productDeviceCount(countQueryParam);
    }

    /**
     * 设备列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @Override
    public Pagination<MemberDevicePageResult> memberGroupDevicePage(MemberGroupDeviceQueryParam param) {
        // 获取消杀设备id
        var memberGroupDeviceList = this.list(Wrappers.<MemberGroupDevice>lambdaQuery()
                                                      .select(MemberGroupDevice::getDeviceId).eq(param.getGroupId() !=
                                                                                                 null, MemberGroupDevice::getGroupId, param.getGroupId())
                                                      .groupBy(MemberGroupDevice::getDeviceId));
        if (CollectionUtils.isEmpty(memberGroupDeviceList)) {
            return Pagination.empty();
        }
        var deviceIdList = memberGroupDeviceList.stream().map(MemberGroupDevice::getDeviceId).collect(Collectors.toList());
        // 查询设备分页列表(iot)
        var deviceFilterPageParam = new DeviceFilterPageParam();
        deviceFilterPageParam.setProductId(param.getProductId());
        deviceFilterPageParam.setMatchedDeviceIds(deviceIdList);
        deviceFilterPageParam.setKeyword(param.getKeyword());
        deviceFilterPageParam.setPageSize(param.getPageSize());
        deviceFilterPageParam.setPageNumber(param.getPageNumber());
        var deviceVOPagination = deviceCenterStdClient.deviceInfoPage(deviceFilterPageParam);
        if (CollectionUtils.isEmpty(deviceVOPagination.getRecords())) {
            return Pagination.empty();
        }
        var resultList = ConvertUtils.toList(MemberDevicePageResult.class, deviceVOPagination.getRecords());
        // 提取设备id和产品id
        var idList = new ArrayList<Long>();
        var productIdSet = new HashSet<String>();
        // 设备id和产品id对应的map
        var deviceProductIdMap = new HashMap<Long, String>();
        resultList.forEach(d -> {
            idList.add(d.getId());
            productIdSet.add(String.valueOf(d.getProductInfo().getId()));
            deviceProductIdMap.put(d.getId(), String.valueOf(d.getProductInfo().getId()));

        });
        /* 获取属性单位等信息(属性在产品上)*/
        var productPropMap = new HashMap<String, List<ProductPropDTO>>();
        var productDTOList = iotProductClient.prodListDetail(new ArrayList<>(productIdSet));
        Optional.ofNullable(productDTOList).orElse(Collections.emptyList())
                .forEach(productDTO -> productPropMap.put(productDTO.getProdId(), productDTO.getProductPropVoList()));
        /* 获取属性当前值 */
        Map<String, List<KeyValueWithTsDTO>> propValueMap;
        var propCodeStr = new StringBuffer();
        if (CollectionUtils.isNotEmpty(idList)) {
            for (var deviceId : idList) {
                var productId = deviceProductIdMap.get(deviceId);
                if (StringUtils.isBlank(productId)) {
                    continue;
                }
                var productPropDTOS = productPropMap.get(productId);
                if (CollectionUtils.isEmpty(productPropDTOS)) {
                    continue;
                }
                productPropDTOS.forEach(productPropDTO -> propCodeStr.append(deviceId).append(".").append(productPropDTO.getPropCode())
                        .append(","));
            }
        }
        if (StringUtils.isNotBlank(propCodeStr)) {
            var multiDevicePropParam = new MultiDevicePropParam();
            multiDevicePropParam.setDevIdAndPropCodes(propCodeStr.substring(0, propCodeStr.length() - 1));
            propValueMap = iotDevicePropClient.multiProp(multiDevicePropParam);
        } else {
            propValueMap = Collections.emptyMap();
        }

        /* 设值 */
        if (propValueMap != null && !productPropMap.isEmpty()) {
            resultList.forEach(d -> {
                if (propValueMap.containsKey(String.valueOf(d.getId())) && d.getProductInfo() != null &&
                    productPropMap.containsKey(String.valueOf(d.getProductInfo().getId()))) {
                    // 值map
                    var devicePropList = propValueMap.get(String.valueOf(d.getId()));
                    var devicePropMap = devicePropList.stream()
                            .collect(Collectors.toMap(KeyValueWithTsDTO::getKey, KeyValueWithTsDTO::getValue, (o, n) -> n));
                    // 属性集合
                    var productPropDTOS = productPropMap.get(String.valueOf(d.getProductInfo().getId()));
                    var list = ObjectUtils.copying(productPropDTOS, MemberDevicePageResult.PropResult.class);
                    // 给属性设置对应值
                    list.forEach(p -> p.setValue(String.valueOf(devicePropMap.get(p.getPropCode()))));
                    d.setPropList(list);
                }
            });
        }
        return new Pagination<>(deviceVOPagination.getTotal(), deviceVOPagination.getPages(), resultList);
    }

    @Override
    public Set<Long> getAllBindDeviceIdList() {
        return lambdaQuery().select(MemberGroupDevice::getDeviceId).list().stream().map(MemberGroupDevice::getDeviceId)
                .collect(Collectors.toSet());
    }

    @Override
    public List<Integer> getTenantsByDeviceId(long iotDeviceId) {
        return this.baseMapper.getTenantsByDeviceId(iotDeviceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean manualControl(ManualControlParam param) {
        try {
            var command = param.getCommand();
            Assert.isParameterError(command != ManualControlCommandEnum.DRIVE_MOSQUITO
                                    && command != ManualControlCommandEnum.MIST_SPRAY
                                    && command != ManualControlCommandEnum.DEVICE_OFF,
                                    "仅支持驱蚊指令、雾灌指令、设备关闭");
            var methodDto = new MemberDeviceMethodDTO();
            methodDto.setCode(command.getMethodCode());
            methodDto.setName(command.getName());
            var paramList = new ArrayList<MemberDeviceMethodDTO.MemberDeviceMethodParamDTO>();
            for (ManualControlMethodParamEnum methodParam : command.getMethodParams()) {
                var dtoParam = new MemberDeviceMethodDTO.MemberDeviceMethodParamDTO();
                dtoParam.setCode(methodParam.getCode());
                dtoParam.setName(methodParam.getName());
                if (methodParam == ManualControlMethodParamEnum.SWITCH) {
                    dtoParam.setExpectedValue(command == ManualControlCommandEnum.DEVICE_OFF ? 0L : 1L);
                } else if (methodParam == ManualControlMethodParamEnum.TIMED_OFF
                           && (command == ManualControlCommandEnum.DRIVE_MOSQUITO || command == ManualControlCommandEnum.MIST_SPRAY)
                           && param.getDuration() != null) {
                    dtoParam.setExpectedValue(param.getDuration().longValue());
                } else {
                    continue;
                }
                paramList.add(dtoParam);
            }
            methodDto.setParams(paramList);
            var batchParam = new DeviceMethodControlCommandParam();
            batchParam.setDeviceId(param.getDeviceId());
            batchParam.setDeviceName(param.getDeviceName());
            batchParam.setMethodList(List.of(methodDto));
            var result = deviceMethodControlBatch(batchParam);
            if (Boolean.TRUE.equals(result) && command.isNeedDuration()) {
                saveManualControlDurationSetting(param);
            }
            return result;
        } catch (Exception e) {
            log.error("设备手动控制失败，deviceId={}, command={}", param == null ? null : param.getDeviceId(),
                      param == null ? null : param.getCommand(), e);
            throw new ServiceException("设备控制失败！");
        }
    }

    @Override
    public List<ManualControlDurationSettingResult> getManualControlDurationSettings(Long deviceId) {
        Assert.isParameterError(deviceId == null, "设备id不能为空");
        var account = SecurityContextHolder.getPrincipal().getAccount();
        return manualControlSettingService.lambdaQuery()
                .select(MemberDeviceManualControlSetting::getDeviceId, MemberDeviceManualControlSetting::getCommand,
                        MemberDeviceManualControlSetting::getDuration)
                .eq(MemberDeviceManualControlSetting::getDeviceId, deviceId)
                .in(MemberDeviceManualControlSetting::getCommand, ManualControlCommandEnum.DRIVE_MOSQUITO,
                        ManualControlCommandEnum.MIST_SPRAY)
                .eq(MemberDeviceManualControlSetting::getCreateBy, account)
                .list()
                .stream()
                .map(setting -> {
                    var result = new ManualControlDurationSettingResult();
                    result.setDeviceId(setting.getDeviceId());
                    result.setCommand(setting.getCommand());
                    result.setDuration(setting.getDuration());
                    return result;
                })
                .toList();
    }

    private void saveManualControlDurationSetting(ManualControlParam param) {
        var account = SecurityContextHolder.getPrincipal().getAccount();
        var updated = manualControlSettingService.lambdaUpdate()
                .eq(MemberDeviceManualControlSetting::getDeviceId, param.getDeviceId())
                .eq(MemberDeviceManualControlSetting::getCommand, param.getCommand())
                .eq(MemberDeviceManualControlSetting::getCreateBy, account)
                .set(MemberDeviceManualControlSetting::getDuration, param.getDuration())
                .update();
        if (updated) {
            return;
        }
        var setting = new MemberDeviceManualControlSetting();
        setting.setId(IdWorker.getId());
        setting.setDeviceId(param.getDeviceId());
        setting.setCommand(param.getCommand());
        setting.setCreateBy(account);
        setting.setDuration(param.getDuration());
        manualControlSettingService.save(setting);
    }

    @Override
    public Boolean deviceMethodControlBatch(DeviceMethodControlCommandParam param) {

        Assert.isParameterError(CollectionUtils.isEmpty(param.getMethodList()), "设备控制方法参数不能为空！");
        var params = ListUtils.convertItem(param.getMethodList(), dto -> {
            var methodInvokeRequestDTO = new MethodInvokeRequestDTO();
            methodInvokeRequestDTO.setDeviceId(param.getDeviceId().toString());
            methodInvokeRequestDTO.setMethodCode(dto.getCode());
            methodInvokeRequestDTO.setCommandSeq(IdWorker.getIdStr());
            var map = new HashMap<String, Object>();
            dto.getParams().forEach(d -> map.put(d.getCode(), d.getExpectedValue()));
            methodInvokeRequestDTO.setInputParams(map);
            return methodInvokeRequestDTO;
        });
        var responses = iotProductMethodClient.batchInvokeMethod(params);
        var deviceIdsForCheck = Collections.nCopies(params.size(), param.getDeviceId());
        checkBatchInvokeResponses(responses, deviceIdsForCheck, "设备控制");
        /* 发送日志事件 */
        StringBuffer stringBuffer = new StringBuffer();
        var detail = param.getDeviceName();
        param.getMethodList().forEach(dto -> {
            var tem = new StringBuilder();
            for (MemberDeviceMethodDTO.MemberDeviceMethodParamDTO dtoParam : dto.getParams()) {
                tem.append(dtoParam.getName()).append(":").append(dtoParam.getExpectedValue()).append("，");
            }
            if (!tem.isEmpty()) {
                tem.deleteCharAt(tem.length() - 1);
            }

            stringBuffer.append(dto.getName()).append("(").append(tem).append(")").append("，");
        });
        if (!stringBuffer.isEmpty()) {
            var str = stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString();
            detail = detail + ":" + str;
        }
        var account = SecurityContextHolder.getPrincipal().getAccount();
        var name = SecurityContextHolder.getPrincipal().getName();
        var logAddEvent = new DisinfectionMemberLogAddEvent(this, account, MemberLogTypeEnum.USER_OPERATION_LOG, param.getDeviceId(), name, detail);
        applicationEventPublisher.publishEvent(logAddEvent);

        return true;
    }

    private static final Set<String> FAILED_STATUS = Set.of("REJECT", "FAIL", "EXPIRED");

    private void checkBatchInvokeResponses(List<MethodInvokeResponseDTO> responses, List<Long> deviceIds, String actionDesc) {
        if (CollectionUtils.isEmpty(responses)) {
            return;
        }
        var ids = ListUtils.orElse(deviceIds, List::of);
        var successDeviceIds = new ArrayList<Long>();
        var failedDeviceIds = new ArrayList<Long>();
        var failedDetails = new ArrayList<String>();
        for (var i = 0; i < responses.size(); i++) {
            var r = responses.get(i);
            var deviceId = i < ids.size() ? ids.get(i) : null;
            var statusCode = r != null ? r.getStatusCode() : null;
            var isFailed = statusCode != null && FAILED_STATUS.contains(statusCode);
            if (isFailed) {
                if (deviceId != null) {
                    failedDeviceIds.add(deviceId);
                }
                failedDetails.add("设备" + deviceId + ":" + statusCode + (r != null && r.getMessage() != null ? " " + r.getMessage() : ""));
            } else {
                if (deviceId != null) {
                    successDeviceIds.add(deviceId);
                }
            }
        }
        log.info("{}成功设备：{}", actionDesc, successDeviceIds);
        log.info("{}失败设备：{}", actionDesc, failedDeviceIds);
        if (CollectionUtils.isNotEmpty(failedDeviceIds)) {
            var msg = String.join("; ", failedDetails);
            Assert.isParameterError(true, actionDesc + "失败，失败设备：" + failedDeviceIds + "，详情：" + msg);
        }
    }

    private List<ProductDTO> getProductAllList() {
        var productDTOList = new ArrayList<ProductDTO>();
        var pageNumber = 1;
        var pageSize = 999;
        var queryParam = new ProductPageParam();
        queryParam.setPageNumber(pageNumber);
        queryParam.setPageSize(pageSize);
        long totalPages;
        do {
            var dtoPagination = iotProductClient.page(queryParam);
            if (CollectionUtils.isNotEmpty(dtoPagination.getRecords())) {
                productDTOList.addAll(dtoPagination.getRecords());
            }
            pageNumber++;
            totalPages = dtoPagination.getPages();
        } while (pageNumber <= totalPages);
        return productDTOList;
    }
}
