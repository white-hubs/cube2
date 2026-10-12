package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.service.impl;

import com.guhecloud.cube.base.device.center.metadata.dto.CenterBizDeviceDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.CenterBizDeviceParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.service.MedicalFeignService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.DeptAndDeviceTreeVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Feign调用处理
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalFeignServiceImpl implements MedicalFeignService {
    private final OrganizationClient organizationClient;
    private final DeviceCenterStdClient deviceCenterStdClient;

    @Override
    public List<DeptAndDeviceTreeVO> getDeptAndDeviceTree() {
        List<DeptAndDeviceTreeVO> result = new ArrayList<>();
        OrganizationQueryParam deptParam = new OrganizationQueryParam();
        deptParam.setEnabled(true);
        List<OrganizationDTO> organizationDTOList = organizationClient.getOrganizations(deptParam);
        if (CollectionUtils.isEmpty(organizationDTOList)) {
            return result;
        }
        result = organizationDTOList.stream().map(dept -> {
            DeptAndDeviceTreeVO vo = new DeptAndDeviceTreeVO();
            vo.setId(dept.getId());
            vo.setCode(dept.getSequence());
            vo.setDeviceBool(false);
            vo.setName(dept.getName());
            vo.setParentId(dept.getParentId());
            vo.setLevel(dept.getLevel());
            return vo;
        }).collect(Collectors.toList());
        result = ObjectUtils.copying(TreeUtils.assembleTrees(result), DeptAndDeviceTreeVO.class);
        Map<Long, List<CenterBizDeviceDTO>> deviceDTOMap = new HashMap<>();
        List<Long> deptIdList = new ArrayList<>();
        this.loopDeptTree(organizationDTOList, deptIdList);
        CenterBizDeviceParam deviceParam = new CenterBizDeviceParam();
        deviceParam.setOrganizationList(deptIdList);
        List<CenterBizDeviceDTO> deviceList = deviceCenterStdClient.getCenterBizDeviceByOrganizations(deviceParam);
        if (deviceList != null) {
            deviceDTOMap = deviceList.stream().collect(Collectors.groupingBy(CenterBizDeviceDTO::getDepartmentId));
        }
        // 设置设备
        for (DeptAndDeviceTreeVO deptAndDeviceTreeVO : result) {
            this.loopDeptSetDevice(deptAndDeviceTreeVO, deviceDTOMap);
        }
        return result;
    }

    private void loopDeptSetDevice(DeptAndDeviceTreeVO parent, Map<Long, List<CenterBizDeviceDTO>> deviceDTOMap) {
        if (parent.getChildren() != null) {
            for (DeptAndDeviceTreeVO child : parent.getChildren()) {
                this.loopDeptSetDevice(child, deviceDTOMap);
            }
        }
        List<CenterBizDeviceDTO> deviceList = deviceDTOMap.get(parent.getId());
        if (deviceList != null) {
            List<DeptAndDeviceTreeVO> deviceChlist = deviceList.stream().map(device -> {
                DeptAndDeviceTreeVO v = new DeptAndDeviceTreeVO();
                v.setId(device.getId());
                v.setCode(device.getDeviceCode());
                v.setName(device.getDeviceName());
                v.setDeviceBool(true);
                v.setParentId(parent.getId());
                v.setLevel(parent.getLevel() + 1);
                return v;
            }).collect(Collectors.toList());
            if (parent.getChildren() != null) {
                parent.getChildren().addAll(deviceChlist);
            } else {
                parent.setChildren(deviceChlist);
            }
        }
    }

    private void loopDeptTree(List<OrganizationDTO> deptList, List<Long> list) {
        if (CollectionUtils.isEmpty(deptList)) {
            return;
        }
        deptList.forEach(dept -> {
            list.add(dept.getId());
            if (!CollectionUtils.isEmpty(dept.getChildren())) {
                loopDeptTree(dept.getChildren(), list);
            }
        });
    }
}
