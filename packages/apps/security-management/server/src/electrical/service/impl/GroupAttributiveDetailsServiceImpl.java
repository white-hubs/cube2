package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.device.center.metadata.std.dto.DeviceLntAndLatCountDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterTagStdClient;
import com.guhecloud.cube.iot.north.bridge.client.IotDevicePropClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceTSDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.PropertyTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.HistorySampleParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.util.SecurityManagementCommonUtil;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.dao.GroupAttributiveDetailsMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity.GroupAttributiveDetails;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.param.HistoricalAttributeParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service.GroupAttributiveDetailsService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.AttributiveGroupVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.GroupAttributiveInfoVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.PropertyHistoryVo;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.MapUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("IdentityBinaryExpression")
public class GroupAttributiveDetailsServiceImpl extends ServiceImpl<GroupAttributiveDetailsMapper, GroupAttributiveDetails> implements GroupAttributiveDetailsService {

    private final GroupAttributiveDetailsMapper groupAttributiveDetailsMapper;

    private final DeviceCenterTagStdClient deviceCenterTagStdClient;

    @Override
    public List<GroupAttributiveInfoVo> getGroupAttributive(String applicationCode, String groupName) {
        return groupAttributiveDetailsMapper.getGroupAttributive(applicationCode, groupName);
    }

    @Override
    public List<DeviceLntAndLatCountDTO> getAreaPoint(String applicationCode) {
        List<DeviceLntAndLatCountDTO> deviceIdsGroupByLntAndLat = deviceCenterTagStdClient.getDeviceIdsGroupByLntAndLat(
                SecurityManagementCommonUtil.applicationConstants + applicationCode);
        return deviceIdsGroupByLntAndLat;
    }

    @Override
    public Map<String, List<AttributiveGroupVo>> getHistoricalAttribute(HistoricalAttributeParam historicalAttributeParam
                                                                       ) throws ExecutionException, InterruptedException {
        Assert.isNotAcceptable(CollectionUtils.isEmpty(historicalAttributeParam.getAttributives()), "该设备无属性");
        List<AttributiveGroupVo> groupAttributives = groupAttributiveDetailsMapper.getGroupAttributives("ELECTRICMANAGE", historicalAttributeParam.getAttributives());
        Map<String, List<AttributiveGroupVo>> collect = groupAttributives.stream()
                .collect(Collectors.groupingBy(AttributiveGroupVo::getName));
        if (!MapUtils.isEmpty(collect)) {
            String period = historicalAttributeParam.getPeriod();
            String[] split = period.split(",");
            var startTime = DateUtils.parse(split[0]);
            var endTime = DateUtils.parse(split[1]);
            var date = DateUtils.now();
            int compare = DateUtils.compare(endTime, date);
            long between = 0;
            if (compare == 1) {
                between = DateUtils.between(startTime, date, ChronoUnit.MINUTES);
            } else {
                between = DateUtils.between(startTime, endTime, ChronoUnit.MINUTES);
            }

            Integer tenant = SessionContextHolder.getRequester().getTenant();
            int size = groupAttributives.size();
            ExecutorService executorService = Executors.newFixedThreadPool(size);
            List<Future<List<Map<String, Object>>>> futures = new ArrayList<Future<List<Map<String, Object>>>>(size);
            for (int i = 0; i < size; i++) {
                //将数据分成threadNum份，线程同时执行
                int from = size / size * i;
                int to = size / size * (i + 1);
                if (i == size - 1) {
                    to = size;
                }
                List<AttributiveGroupVo> subList = groupAttributives.subList(from, to);
                Callable<List<Map<String, Object>>> task = new Callable<List<Map<String, Object>>>() {
                    @Override
                    public List<Map<String, Object>> call() {
                        return getAttributeCodes(2050, subList, startTime, endTime, historicalAttributeParam.getDeviceId(), tenant);
                    }
                };
                //添加线程到队列
                futures.add(executorService.submit(task));
            }
            Map<String, Object> mergedMap = new HashMap<>();
            for (int i = 0; i < futures.size(); i++) {
                List<Map<String, Object>> maps = futures.get(i).get();
                for (Map<String, Object> map : maps) {
                    mergedMap.putAll(map);
                }
            }

            //时间轴
            if (collect.keySet().size() > 0) {
                for (String s : collect.keySet()) {
                    List<AttributiveGroupVo> attributiveGroupVos = collect.get(s);
                    for (AttributiveGroupVo attributiveGroupVo : attributiveGroupVos) {
                        List<PropertyHistoryVo> propertyHistoryVos = new ArrayList<>();
                        for (int i = 0; i < between / 2; i++) {
                            PropertyHistoryVo propertyHistoryVo = new PropertyHistoryVo();
                            var offset = DateUtils.offset(startTime, ChronoUnit.MINUTES, 2 * i);
                            String s1 = DateUtils.format(offset, "yyyy-MM-dd HH:mm:00");
                            propertyHistoryVo.setTs(s1);
                            String key = attributiveGroupVo.getAttributeCode() + s1;
                            propertyHistoryVo.setValue(mergedMap.get(key) == null ? null : mergedMap.get(key).toString());
                            propertyHistoryVos.add(propertyHistoryVo);
                        }
                        attributiveGroupVo.setPropertyHistoryVos(propertyHistoryVos);
                    }
                }

            }

        }
        return collect;

    }

    private List<Map<String, Object>> getAttributeCodes(Integer timeNum, List<AttributiveGroupVo> list, LocalDateTime startTime,
                                                        LocalDateTime endTime,
                                                        Long deviceId, Integer tenant
                                                       ) {
        List<Map<String, Object>> objects = new ArrayList<>();
        SessionContextHolder.requestTenant(tenant);
        for (AttributiveGroupVo attributiveGroupVo : list) {
            Map<String, Object> map = new HashMap<>();
            //物联网获取历史数据
            IotDevicePropClient iotDevicePropClient = ApplicationContextHolder.getBean(IotDevicePropClient.class);
            HistorySampleParam historySampleParam = new HistorySampleParam();
            historySampleParam.setDeviceId(String.valueOf(deviceId));
            historySampleParam.setStep(2);
            historySampleParam.setStartTs(DateUtils.toTimestamp(startTime));
            historySampleParam.setEndTs(DateUtils.toTimestamp(endTime));
            historySampleParam.setMaxRecords(timeNum);
            historySampleParam.setAggType("avg");
            historySampleParam.setProperties(Collections.singletonList(attributiveGroupVo.getAttributeCode()));
            DeviceTSDTO deviceTSDTO = iotDevicePropClient.historySampleProp(historySampleParam);
            if (Objects.nonNull(deviceTSDTO)) {
                List<PropertyTsDTO> propertyTsBoList = deviceTSDTO.getPropertyTsBoList();
                if (CollectionUtils.isNotEmpty(propertyTsBoList)) {
                    propertyTsBoList.forEach(s -> {
                        List<KeyValueWithTsDTO> valueTsBoList = s.getValueTsBoList();
                        for (KeyValueWithTsDTO valueTs : valueTsBoList) {
                            BigDecimal bigDecimal = new BigDecimal(String.valueOf(valueTs.getValue()));
                            String s1 = bigDecimal.setScale(2, RoundingMode.HALF_UP).toString();
                            Long ts = valueTs.getTs();
                            var date = DateUtils.parse(ts);
                            String format = DateUtils.format(date, "yyyy-MM-dd HH:mm:00");
                            map.put(s.getPropertyCode() + format, s1);
                            objects.add(map);
                        }
                    });
                }

            }

        }

        return objects;
    }

}
