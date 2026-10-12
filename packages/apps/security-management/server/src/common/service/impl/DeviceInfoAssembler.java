package com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.ProductClient;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.SecurityCommonMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.dto.SecurityDeviceInfoDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.query.DeviceInfoPageQuery;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2023/11/1 14:19
 * @description 设备信息组装器
 */
@Component
public class DeviceInfoAssembler {

    @Autowired
    private SecurityCommonMapper securityCommonMapper;
    @Autowired
    private ProductClient productClient;

    @Autowired
    private FileClient fileClient;

    public Pagination<SecurityDeviceInfoDTO> page(DeviceInfoPageQuery deviceInfoPageQuery) {
        Page<SecurityDeviceInfoDTO> securityDeviceInfoDTOPage = securityCommonMapper.selectDeviceInfoPage(PagingUtils.convert(deviceInfoPageQuery), deviceInfoPageQuery);
        List<SecurityDeviceInfoDTO> records = securityDeviceInfoDTOPage.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            Set<Long> productIds = records.stream()
                    .map(SecurityDeviceInfoDTO::getDeviceProdId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            if (CollectionUtils.isEmpty(productIds)) {
                return PagingUtils.convert(securityDeviceInfoDTOPage);
            }

            List<DeviceProductInfoDTO> productList = productClient.getProductList(productIds);
            if (CollectionUtils.isEmpty(productList)) {
                return PagingUtils.convert(securityDeviceInfoDTOPage);
            }

            Map<Long, DeviceProductInfoDTO> productInfoMap = new HashMap<>();
            Set<Long> fileIds = new HashSet<>();
            for (DeviceProductInfoDTO deviceProductInfoDTO : productList) {
                productInfoMap.put(deviceProductInfoDTO.getId(), deviceProductInfoDTO);
                if (Objects.nonNull(deviceProductInfoDTO.getPicture())) {
                    fileIds.add(deviceProductInfoDTO.getPicture());
                }
                if (Objects.nonNull(deviceProductInfoDTO.getIcon())) {
                    fileIds.add(deviceProductInfoDTO.getIcon());
                }
            }

            Map<Long, FileVO> fileMap = CollectionUtils.isEmpty(fileIds)
                                        ? Collections.emptyMap()
                                        : fileClient.getByIds(new ArrayList<>(fileIds)).stream()
                                                .collect(Collectors.toMap(FileVO::getId, file -> file, (o, n) -> n));

            for (SecurityDeviceInfoDTO securityDeviceInfoDTO : records) {
                DeviceProductInfoDTO deviceProductInfoDTO = productInfoMap.get(securityDeviceInfoDTO.getDeviceProdId());
                if (Objects.isNull(deviceProductInfoDTO)) {
                    continue;
                }
                if (Objects.nonNull(deviceProductInfoDTO.getPicture())) {
                    securityDeviceInfoDTO.setPicture(fileMap.get(deviceProductInfoDTO.getPicture()));
                }
                if (Objects.nonNull(deviceProductInfoDTO.getIcon())) {
                    securityDeviceInfoDTO.setIcon(fileMap.get(deviceProductInfoDTO.getIcon()));
                }
            }
        }
        return PagingUtils.convert(securityDeviceInfoDTOPage);
    }

}
