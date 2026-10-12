package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.querygateway;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceDetailDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.valueobject.AttClockWayEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttClockDeviceMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttClockLocationMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttClockRulesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockDevicePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockLocationPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.AttClockRulesPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttAreaResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttClockLocationResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttClockRuleDetailResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttClockRuleListResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttDepartmentResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttDeviceResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttProductCategoryResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttProductInfoResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 打卡规则服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttClockRuleQueryService {

    private final AttClockRulesMapper attClockRulesMapper;
    private final AttClockLocationMapper attClockLocationMapper;
    private final AttClockDeviceMapper attClockDeviceMapper;
    private final DeviceCenterStdClient deviceCenterStdClient;

    /**
     * 分页查询
     *
     * @param param 条件入参
     *
     * @return 分页列表
     */
    public Pagination<AttClockRuleListResult> pageAttClockRules(AttClockRulesPageParam param) {

        Page<AttClockRulePO> page = attClockRulesMapper.selectPage(PagingUtils.convert(param), Wrappers.<AttClockRulePO>lambdaQuery()
                                                                           .like(StringUtils.isNotBlank(param.getName()), AttClockRulePO::getName, param.getName())
                                                                           .orderByDesc(AttClockRulePO::getUpdateTime)
                                                                  );
        return new Pagination<>(page.getTotal(), page.getPages(),
                                page.getRecords().stream().map(po -> {
                                    AttClockRuleListResult attClockRuleListResult = new AttClockRuleListResult();
                                    BeanUtils.copyProperties(po, attClockRuleListResult);
                                    return attClockRuleListResult;
                                }).collect(Collectors.toList())
        );
    }

    /**
     * 详情
     *
     * @param id 主键
     *
     * @return 详情
     */
    public AttClockRuleDetailResult infoClockRule(Long id) {
        // 查询规则
        AttClockRulePO po = attClockRulesMapper.selectById(id);
        AttClockRuleDetailResult result = new AttClockRuleDetailResult();
        BeanUtils.copyProperties(po, result);
        // 查询规则的打卡地点
        if (StringUtils.isNotBlank(result.getWays()) && result.getWays().contains(AttClockWayEnum.GPS.name())) {
            List<AttClockLocationPO> poList = attClockLocationMapper.selectLocationPo(Wrappers.<AttClockLocationPO>lambdaQuery()
                                                                                              .eq(AttClockLocationPO::getRulesId, id));
            List<AttClockLocationResult> locationResults = poList.stream().map(locationPo -> {
                AttClockLocationResult locationResult = new AttClockLocationResult();
                BeanUtils.copyProperties(locationPo, locationResult);
                return locationResult;
            }).collect(Collectors.toList());
            result.setLocationList(locationResults);
        }
        // 查询规则的打卡设备
        if (StringUtils.isNotBlank(result.getWays()) && result.getWays().contains(AttClockWayEnum.DEVICE.name())) {
            Set<Long> poList = attClockDeviceMapper.selectDevicePo(Wrappers.<AttClockDevicePO>lambdaQuery()
                                                                           .eq(AttClockDevicePO::getRulesId, id));
            List<AttDeviceResult> deviceList = new ArrayList<>();
            for (Long deviceId : poList) {
                DeviceDetailDTO deviceDetail = deviceCenterStdClient.getDeviceById(deviceId);
                if (ObjectUtils.isNotEmpty(deviceDetail) && ObjectUtils.isNotEmpty(deviceDetail.getBaseArchivesInfo())) {
                    AttDeviceResult attDeviceDTO = new AttDeviceResult();
                    attDeviceDTO.setId(deviceId);
                    attDeviceDTO.setName(Optional.ofNullable(deviceDetail.getBaseArchivesInfo()).orElse(new DeviceBaseArchivesInfoDTO())
                                                 .getName());
                    AttProductInfoResult productInfo = new AttProductInfoResult();
                    AttProductCategoryResult attProductCategoryResult = new AttProductCategoryResult();
                    attProductCategoryResult.setName(Optional.ofNullable(deviceDetail.getProductInfo()).orElse(new DeviceProductInfoDTO())
                                                             .getProductCategory().getName());
                    productInfo.setProductCategory(attProductCategoryResult);
                    attDeviceDTO.setProductInfo(productInfo);

                    AttAreaResult attArea = new AttAreaResult();
                    attArea.setPathName(
                            ObjectUtils.isNotEmpty(deviceDetail.getBaseArchivesInfo().getArea()) ? deviceDetail.getBaseArchivesInfo()
                                    .getArea().getVal() : "");
                    attDeviceDTO.setArea(attArea);

                    AttDepartmentResult attDepartment = new AttDepartmentResult();
                    attDepartment.setName(
                            ObjectUtils.isNotEmpty(deviceDetail.getBaseArchivesInfo().getDepartment()) ? deviceDetail.getBaseArchivesInfo()
                                    .getDepartment().getVal() : "");
                    attDeviceDTO.setDepartment(attDepartment);

                    deviceList.add(attDeviceDTO);
                }
            }
            result.setDeviceList(deviceList);
        }
        return result;
    }
}
