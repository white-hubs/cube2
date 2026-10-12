package com.guhecloud.cube.iot.mgt.north.controller;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.cube.iot.mgt.comm.util.Assert;
import com.guhecloud.cube.iot.mgt.comm.util.ParameterCheck;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.KeyValueWithTs;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DevicePropHistoryQueryVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceSamplingHistoryQueryVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceSamplingHistoryResultVo;
import com.guhecloud.cube.iot.mgt.north.param.DevicePropHistoryPageParam;
import com.guhecloud.cube.iot.mgt.north.param.DevicePropHistoryParam;
import com.guhecloud.cube.iot.mgt.north.param.DevicePropParam;
import com.guhecloud.cube.iot.mgt.north.param.DeviceSamplingPageParam;
import com.guhecloud.cube.iot.mgt.north.param.HistorySampleDto;
import com.guhecloud.cube.iot.mgt.north.param.MultiDevicePropParam;
import com.guhecloud.cube.iot.mgt.north.param.MultiHistorySampleDto;
import com.guhecloud.cube.iot.mgt.north.server.impl.NorthDevicePropService;
import com.guhecloud.cube.iot.mgt.north.vo.ts.DeviceTSBo;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.standard.exception.ResourcePreconditionFailedException;
import com.guhecloud.platform.base.util.BeanUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/north/device/prop")
public class NorthDevicePropController {

    @Resource
    private NorthDevicePropService northDevicePropService;

    /**
     * 单设备-实时属性获取
     *
     * @param devicePropParam devicePropParam
     *
     * @author Fl
     **/
    @GetMapping("")
    public List<KeyValueWithTs> prop(@SpringQueryMap DevicePropParam devicePropParam) {
        return northDevicePropService.deviceRealTimeStats(devicePropParam.getDeviceId(), devicePropParam.getPropertyCodes(), devicePropParam.getNotBeforeTs());
    }

    /**
     * 多设备-实时属性获取
     *
     * @param multiDevicePropParam 多设备实时属性查询
     *
     * @return map key:设备id value:属性列表
     */
    @GetMapping("/multi-prop")
    public Map<String, List<KeyValueWithTs>> multiProp(@SpringQueryMap MultiDevicePropParam multiDevicePropParam) {
        String deviceIdAndPropCodes = multiDevicePropParam.getDevIdAndPropCodes();
        if (StringUtils.isBlank(deviceIdAndPropCodes)) {
            return Collections.emptyMap();
        }

        String[] devicePropPair = deviceIdAndPropCodes.split(",");
        if (devicePropPair.length < 1) {
            return Collections.emptyMap();
        }

        //key 设备id value
        //val 属性列表
        Map<String, Set<String>> deviceProp = new HashMap<>();
        for (String pair : devicePropPair) {
            String[] temp = pair.split("\\.");
            Set<String> propSet = deviceProp.computeIfAbsent(temp[0], k -> new HashSet<>());
            if (temp.length > 1) {
                propSet.add(temp[1]);
            }
        }

        Map<String, List<KeyValueWithTs>> result = new HashMap<>();
        for (Map.Entry<String, Set<String>> en : deviceProp.entrySet()) {
            String deviceId = en.getKey();
            String propCodes = String.join(",", en.getValue());
            result.put(deviceId,
                       northDevicePropService.deviceRealTimeStats(
                               Long.valueOf(deviceId),
                               propCodes,
                               multiDevicePropParam.getNotBeforeTs())
                      );
        }
        return result;
    }

    /**
     * 多设备-实时属性获取
     */
    @PostMapping("/multi-prop")
    Map<String, List<KeyValueWithTs>> multiPropPost(@RequestBody @Valid MultiDevicePropParam multiDevicePropParam) {
        return multiProp(multiDevicePropParam);
    }

    /**
     * 单设备-历史属性分页获取
     *
     * @param devicePropHistoryPageParam devicePropHistoryPageParam
     *
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DevicePropHistoryResultVo>
     */
    @GetMapping("/history-prop-page")
    public Pagination<KeyValueWithTs> devicePropHistoryPropPage(@SpringQueryMap DevicePropHistoryPageParam devicePropHistoryPageParam) {
        Integer tenant = SessionContextHolder.getRequesterProperty(Requester::getTenant);

        if (devicePropHistoryPageParam.getPageNumber() == null || devicePropHistoryPageParam.getPageNumber() < 1) {
            devicePropHistoryPageParam.setPageNumber(1);
        }

        if (devicePropHistoryPageParam.getPageSize() == null || devicePropHistoryPageParam.getPageSize() < 1) {
            devicePropHistoryPageParam.setPageSize(1000);
        }

        return northDevicePropService.devicePropHistoryPropPage(toPropHistoryQueryVo(tenant, devicePropHistoryPageParam), devicePropHistoryPageParam.getPageNumber(), devicePropHistoryPageParam.getPageSize());
    }

    /**
     * 单设备-采样分页获取<br/>
     * 目前只支持单个设备的多个属性的历史采样数据，且只支持10分钟的时间粒度
     *
     * @param deviceSamplingPageParam deviceSamplingPageParam
     *
     * @return Pagination<DeviceSamplingTSBo>
     */
    @GetMapping("/device-history-page")
    public Pagination<DeviceSamplingHistoryResultVo> deviceHistorySamplingPage(
            @SpringQueryMap @Valid DeviceSamplingPageParam deviceSamplingPageParam
                                                                              ) {
        DeviceSamplingHistoryQueryVo propHistoryQueryVo = buildDeviceSamplingHistoryQueryVo(deviceSamplingPageParam);
        return northDevicePropService.deviceHistorySamplingPage(propHistoryQueryVo, deviceSamplingPageParam.getPageNumber(), deviceSamplingPageParam.getPageSize());
    }

    private DeviceSamplingHistoryQueryVo buildDeviceSamplingHistoryQueryVo(DeviceSamplingPageParam param) {
        Integer tenant = SessionContextHolder.getRequesterProperty(Requester::getTenant);
        DeviceSamplingHistoryQueryVo vo = new DeviceSamplingHistoryQueryVo();
        BeanUtils.copyProperties(param, vo);
        vo.setTenantId(String.valueOf(tenant));
        if (ObjectUtils.isNotNull(param.getStartTs())) {
            vo.setStartTime(DateUtils.format(DateUtils.parse(param.getStartTs())));
        }
        if (ObjectUtils.isNotNull(param.getEndTs())) {
            vo.setEndTime(DateUtils.format(DateUtils.parse(param.getEndTs())));
        }
        if (param.getPageSize() == null || param.getPageSize() > 1000) {
            param.setPageSize(param.getMaxRecords());
        }
        if (param.getPageNumber() == null) {
            param.setPageNumber(1);
        }

        return vo;
    }

    /**
     * 单设备-历史属性获取
     *
     * @param devicePropHistoryParam devicePropHistoryParam
     *
     * @return List<KeyValueWithTs>
     */
    @GetMapping("/history")
    public List<KeyValueWithTs> devicePropHistoryProp(@SpringQueryMap DevicePropHistoryParam devicePropHistoryParam) {
        Integer tenant = SessionContextHolder.getRequesterProperty(Requester::getTenant);

        Integer maxRecords = devicePropHistoryParam.getMaxRecords();
        if (maxRecords == null || maxRecords < 1) {
            maxRecords = 1000;
        }

        return northDevicePropService.devicePropHistoryPropPage(toPropHistoryQueryVo(tenant, devicePropHistoryParam), 1, maxRecords)
                .getRecords();
    }

    /**
     * 单设备-历史属性获取<br/>
     * 将多个属性进行归类，便于前端展示,注意:最大返回记录数为单个属性的最大记录数
     *
     * @param devicePropHistoryParam devicePropHistoryParam
     *
     * @return List<KeyValueWithTs>
     */
    @GetMapping("/history-group")
    public Map<String, List<KeyValueWithTs>> devicePropHistoryPropGroup(@SpringQueryMap DevicePropHistoryParam devicePropHistoryParam) {
        Integer tenant = SessionContextHolder.getRequesterProperty(Requester::getTenant);

        Integer maxRecords = devicePropHistoryParam.getMaxRecords();
        if (maxRecords == null || maxRecords < 1) {
            maxRecords = 1000;
        }

        // key:propCode value:List<KeyValueWithTs>
        Map<String, List<KeyValueWithTs>> groupProp = new HashMap<>();

        List<String> propCodes = devicePropHistoryParam.getPropCodes();
        if (Objects.isNull(propCodes) || propCodes.isEmpty()) {
            throw new ResourcePreconditionFailedException(JSON.toJSONString(devicePropHistoryParam), "propCodes is blank");
        }

        for (String propCode : propCodes) {
            devicePropHistoryParam.setPropCodes(Collections.singletonList(propCode));
            Pagination<KeyValueWithTs> pagination = northDevicePropService.devicePropHistoryPropPage(toPropHistoryQueryVo(tenant, devicePropHistoryParam), 1, maxRecords);

            groupProp.put(propCode, pagination.getRecords());
        }

        return groupProp;
    }

    /**
     * 单设备-历史属性采样获取
     *
     * @param historySampleDto history
     *
     * @return DeviceTSBo
     */
    @GetMapping("/history-sample-prop")
    public DeviceTSBo devicePropSampleHistoryTS(@SpringQueryMap HistorySampleDto historySampleDto) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();

        Assert.notNull(historySampleDto, "historySampleItemDto is null");
        return northDevicePropService.devicePropSampleHistoryTS(toPropHistoryQueryVo(tenant, historySampleDto));
    }

    /**
     * 多设备-历史属性采样获取
     *
     * @param multiHistorySampleDto 查询参数
     *
     * @return map key:设备id value:设备采样数据
     */
    private Map<String, DeviceTSBo> multiDevicePropSampleHistoryTS(@SpringQueryMap MultiHistorySampleDto multiHistorySampleDto) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        ParameterCheck.checkIsNotNull(multiHistorySampleDto, "multiHistorySampleDto is null");

        String deviceIdAndPropCodes = multiHistorySampleDto.getDevIdAndPropCodes();
        if (StringUtils.isBlank(deviceIdAndPropCodes)) {
            return Collections.emptyMap();
        }

        String[] devicePropPair = deviceIdAndPropCodes.split(",");
        if (devicePropPair.length < 1) {
            return Collections.emptyMap();
        }

        //key 设备id
        //val 属性列表
        Map<Long, Set<String>> deviceProp = new HashMap<>();
        for (String pair : devicePropPair) {
            if (StringUtils.isBlank(pair)) {
                continue;
            }
            String[] temp = pair.split("\\.");
            if (temp.length < 2 || StringUtils.isBlank(temp[0]) || StringUtils.isBlank(temp[1])) {
                throw new ResourcePreconditionFailedException(pair, "devIdAndPropCodes格式错误，应为deviceId.propCode");
            }

            long deviceId;
            try {
                deviceId = Long.parseLong(temp[0]);
            } catch (Exception e) {
                throw new ResourcePreconditionFailedException(temp[0], "deviceId格式错误");
            }

            Set<String> propSet = deviceProp.computeIfAbsent(deviceId, k -> new HashSet<>());
            propSet.add(temp[1]);
        }

        if (deviceProp.isEmpty()) {
            return Collections.emptyMap();
        }

        return northDevicePropService.multiDevicePropSampleHistoryTS(tenant, deviceProp, multiHistorySampleDto);
    }

    /**
     * 多设备-历史属性采样获取
     */
    @PostMapping("/multi-history-sample-prop")
    public Map<String, DeviceTSBo> multiDevicePropSampleHistoryTSPost(@RequestBody @Valid MultiHistorySampleDto multiHistorySampleDto) {
        return multiDevicePropSampleHistoryTS(multiHistorySampleDto);
    }

    /**
     * 转换为查询参数
     *
     * @param tenant                 租户
     * @param devicePropHistoryParam 参数
     *
     * @return 查询参数
     */
    private DevicePropHistoryQueryVo toPropHistoryQueryVo(Integer tenant, DevicePropHistoryParam devicePropHistoryParam) {
        if (Objects.isNull(devicePropHistoryParam.getDeviceId()) || devicePropHistoryParam.getDeviceId() == 0) {
            throw new ResourcePreconditionFailedException(JSON.toJSONString(devicePropHistoryParam), "deviceId is blank");
        }

        if (Objects.isNull(devicePropHistoryParam.getPropCodes()) || devicePropHistoryParam.getPropCodes().isEmpty()) {
            throw new ResourcePreconditionFailedException(JSON.toJSONString(devicePropHistoryParam), "propCodes is blank");
        }

        DevicePropHistoryQueryVo devicePropHistoryQueryVo = new DevicePropHistoryQueryVo();
        devicePropHistoryQueryVo.setTenantId(String.valueOf(tenant));
        devicePropHistoryQueryVo.setDeviceId(devicePropHistoryParam.getDeviceId());
        devicePropHistoryQueryVo.setPropCodes(devicePropHistoryParam.getPropCodes());

        if (Objects.nonNull(devicePropHistoryParam.getStartTs()) && devicePropHistoryParam.getStartTs() >= 0 &&
            Objects.nonNull(devicePropHistoryParam.getEndTs()) && devicePropHistoryParam.getEndTs() > 0) {
            devicePropHistoryQueryVo.setStartTime(toDateStr(new Date(devicePropHistoryParam.getStartTs())));
            devicePropHistoryQueryVo.setEndTime(toDateStr(new Date(devicePropHistoryParam.getEndTs())));
        }

        if (StringUtils.isNotBlank(devicePropHistoryParam.getOrder())) {
            devicePropHistoryQueryVo.setOrder(devicePropHistoryParam.getOrder());
        }
        if (Objects.nonNull(devicePropHistoryParam.getMaxRecords()) && devicePropHistoryParam.getMaxRecords() > 0) {
            devicePropHistoryQueryVo.setMaxRecords(devicePropHistoryParam.getMaxRecords());
        }
        return devicePropHistoryQueryVo;
    }

    private DevicePropHistoryQueryVo toPropHistoryQueryVo(Integer tenant, HistorySampleDto historySampleDto) {
        if (StringUtils.isBlank(historySampleDto.getDeviceId())) {
            throw new ResourcePreconditionFailedException(JSON.toJSONString(historySampleDto), "deviceId is blank");
        }
        if (Objects.isNull(historySampleDto.getProperties()) || historySampleDto.getProperties().isEmpty()) {
            throw new ResourcePreconditionFailedException(JSON.toJSONString(historySampleDto), "properties is blank");
        }

        DevicePropHistoryQueryVo devicePropHistoryQueryVo = new DevicePropHistoryQueryVo();
        devicePropHistoryQueryVo.setTenantId(String.valueOf(tenant));
        devicePropHistoryQueryVo.setDeviceId(Long.valueOf(historySampleDto.getDeviceId()));
        devicePropHistoryQueryVo.setPropCodes(historySampleDto.getProperties());

        if (Objects.nonNull(historySampleDto.getStartTs()) && historySampleDto.getStartTs() >= 0 &&
            Objects.nonNull(historySampleDto.getEndTs()) && historySampleDto.getEndTs() > 0) {
            devicePropHistoryQueryVo.setStartTime(toDateStr(new Date(historySampleDto.getStartTs())));
            devicePropHistoryQueryVo.setEndTime(toDateStr(new Date(historySampleDto.getEndTs())));
        }

        if (StringUtils.isNotBlank(historySampleDto.getOrder())) {
            devicePropHistoryQueryVo.setOrder(historySampleDto.getOrder());
        }
        if (Objects.nonNull(historySampleDto.getMaxRecords()) && historySampleDto.getMaxRecords() > 0) {
            devicePropHistoryQueryVo.setMaxRecords(historySampleDto.getMaxRecords());
        }

        if (Objects.nonNull(historySampleDto.getStep()) && historySampleDto.getStep() > 0) {
            devicePropHistoryQueryVo.setStep(historySampleDto.getStep());
        }
        if (StringUtils.isNotBlank(historySampleDto.getAggType())) {
            devicePropHistoryQueryVo.setAggType(historySampleDto.getAggType());
        }

        return devicePropHistoryQueryVo;
    }

    /**
     * 转时间
     *
     * @param date date
     *
     * @return String
     */
    private String toDateStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
