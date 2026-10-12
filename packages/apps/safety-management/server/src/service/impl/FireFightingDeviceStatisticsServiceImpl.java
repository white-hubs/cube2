package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.dao.DeviceManageMapper;
import com.guhecloud.cube.srv.gen.safety.management.dao.FireFightingDeviceMapper;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.FireFightingDeviceQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.FireFightingDeviceVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.FireFightingDeviceStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.device.DeviceCategoryCountVO;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.AlarmTypeEnum;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.AttributeStatusEnum;
import com.guhecloud.cube.srv.gen.safety.management.po.DeviceManagePO;
import com.guhecloud.cube.srv.gen.safety.management.service.FireFightingDeviceStatisticsService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 消防设备统计
 * @date 2023/9/8 13:58
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FireFightingDeviceStatisticsServiceImpl extends ServiceImpl<FireFightingDeviceMapper, DeviceManagePO> implements FireFightingDeviceStatisticsService {

    private final DeviceManageMapper deviceManageMapper;

    @Override
    public FireFightingDeviceStatisticsVO geFireFightingDeviceStatistics(BaseQuery query) {
        List<DeviceManagePO> list = deviceManageMapper.getApplicationDevices(query, Collections.singletonList(AlarmTypeEnum.FIRECONTROL.name()));
        FireFightingDeviceStatisticsVO vo = new FireFightingDeviceStatisticsVO();
        if (ObjectUtils.isNotEmpty(list)) {
            int total = list.size();
            vo.setTotalNum(total);
            Map<String, Long> collect = list.stream().collect(Collectors.groupingBy(DeviceManagePO::getStatus, Collectors.counting()));
            List<FireFightingDeviceStatisticsVO.FireFightingDeviceState> ratioList = new ArrayList<>();
            collect.keySet().forEach(key -> {
                Long num = collect.get(key);
                ratioList.add(FireFightingDeviceStatisticsVO.FireFightingDeviceState.builder()
                                      .state(key)
                                      .stateName(StringUtils.isNotBlank(key) && "1".equals(key) ? AttributeStatusEnum.ONLINE.getName()
                                                                                                : AttributeStatusEnum.OFFLINE.getName())
                                      .stateNum(collect.get(key))
                                      .ratio(BigDecimal.valueOf(num).divide(BigDecimal.valueOf(total), 4, RoundingMode.HALF_UP)
                                                     .multiply(BigDecimal.valueOf(100)).doubleValue())
                                      .build());
            });
            vo.setDeviceStateList(ratioList);
        }
        return vo;
    }

    @Override
    public Pagination<FireFightingDeviceVO> getFireFightingDeviceList(FireFightingDeviceQuery fireFightingDevice) {
        Page<DeviceManagePO> page = this.deviceManageMapper.getApplicationDevicePage(PagingUtils.convert(fireFightingDevice),
                                                                                     fireFightingDevice,
                                                                                     Collections.singletonList(AlarmTypeEnum.FIRECONTROL.name()));
        List<FireFightingDeviceVO> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(page)) {
            page.getRecords().forEach(po -> {
                vos.add(FireFightingDeviceVO.builder()
                                .deviceName(po.getDeviceName())
                                .marketTenant(po.getMarketTenant())
                                .marketName(po.getMarketName())
                                .deviceType(po.getIotDeviceProdName())
                                .dep(po.getDepartmentName())
                                .area(po.getAreaName())
                                .state(StringUtils.isNotBlank(po.getStatus()) && "1".equals(po.getStatus())
                                       ? AttributeStatusEnum.ONLINE.getName() : AttributeStatusEnum.OFFLINE.getName())
                                .build());
            });
        }
        return PagingUtils.convert(page, vos);
    }

    @Override
    public List<String> getDeviceTypeList() {
        return this.deviceManageMapper.getApplicationDeviceTypes(Collections.singletonList(AlarmTypeEnum.FIRECONTROL.name()));
    }

    @Override
    public Long countDeviceTotalNum(List<String> list) {
        return this.lambdaQuery().in(ObjectUtils.isNotEmpty(list), DeviceManagePO::getApplicationCode, list).count();
    }

    @Override
    public List<DeviceCategoryCountVO> getDeviceCategoryCount(FireFightingDeviceQuery fireFightingDevice) {
        return this.deviceManageMapper.getDeviceCategoryCount(fireFightingDevice, Collections.singletonList(AlarmTypeEnum.FIRECONTROL.name()));
    }

}
