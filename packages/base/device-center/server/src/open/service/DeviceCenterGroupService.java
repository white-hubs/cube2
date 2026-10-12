package com.guhecloud.cube.base.device.center.open.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizTagMapper;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizTagRefMapper;
import com.guhecloud.cube.base.device.center.core.model.BizGroup;
import com.guhecloud.cube.base.device.center.core.model.BizTag;
import com.guhecloud.cube.base.device.center.core.service.CenterDeviceGroupService;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.param.DeviceGroupBindDeviceCmd;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceGroupParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterParam;
import com.guhecloud.cube.base.device.center.open.interfaces.web.query.DeviceGroupQueryParam;
import com.guhecloud.cube.base.device.center.open.interfaces.web.vo.DeviceGroupPageVO;
import com.guhecloud.cube.base.device.center.open.interfaces.web.vo.DeviceGroupVO;
import com.guhecloud.cube.base.device.center.open.interfaces.web.vo.DevicePageInfoVO;
import com.guhecloud.cube.base.device.center.open.mapper.DeviceCenterMapper;
import com.guhecloud.cube.base.device.center.open.mapper.DeviceGroupMapper;
import com.guhecloud.cube.base.device.center.open.mapper.ProductInfoMapper;
import com.guhecloud.cube.base.device.center.open.vo.DeviceTag;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceCenterGroupService {

    private final CenterDeviceGroupService centerDeviceGroupService;

    private final CenterBizTagMapper centerBizTagMapper;

    private final CenterBizTagRefMapper centerBizTagRefMapper;

    private final DeviceCenterMapper deviceCenterMapper;

    private final DeviceGroupMapper deviceGroupMapper;

    private final ProductInfoMapper productInfoMapper;

    private final FileClient fileClient;

    /**
     * 分组关联设备
     *
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void addGroupDevice(@NonNull DeviceGroupBindDeviceCmd cmd) {
        BizGroup bizGroup = this.centerDeviceGroupService.getGroup(cmd.getId());
        Assert.isNotFound(bizGroup, "分组不存在或已被删除");
        //获取组绑定的标签
        List<DeviceTag> groupRefTag = centerBizTagRefMapper.getTagCodeByBusinessKey(bizGroup.getId());
        DeviceFilterParam deviceFilterParam = cmd.getDeviceFilterParam();
        if (Objects.isNull(deviceFilterParam)) {
            deviceFilterParam = new DeviceFilterParam();
        }
        List<BizTag> bizTags = getBizTags(groupRefTag.get(0).getTagCode());
        List<Long> tagIds = bizTags.stream().map(BizTag::getId).collect(Collectors.toList());
        //过滤获取设备id
        Set<Long> deviceIds = this.deviceCenterMapper.selectDeviceIds(deviceFilterParam, tagIds);
        if (!CollectionUtils.isEmpty(deviceIds)) {
            centerDeviceGroupService.addGroupDevice(cmd.getId(), deviceIds);
        }
    }

    private List<BizTag> getBizTags(String tagCode) {
        return centerBizTagMapper
                .selectList(new LambdaQueryWrapper<BizTag>()
                                    .in(BizTag::getTagCode, tagCode)
                                    .select(BizTag::getId));
    }

    /**
     * 获取标签下的设备分组列表
     *
     * @param param
     *
     * @return
     */
    public Pagination<DeviceGroupPageVO> deviceGroupPageByTag(@NonNull DeviceGroupQueryParam param) {
        List<BizTag> bizTags = getBizTags(param.getTagCode());
        if (CollectionUtils.isEmpty(bizTags)) {
            return Pagination.empty();
        }
        Page<DeviceGroupPageVO> page = this.deviceGroupMapper.deviceGroupPageByTag(PagingUtils.convert(param),
                                                                                   param,
                                                                                   bizTags.get(0).getId());
        return PagingUtils.convert(page);
    }

    /**
     * 获取标签下的分组列表
     *
     * @param tagCode 设备标签
     *
     * @return List<DeviceGroupVO>
     */
    public List<DeviceGroupVO> deviceGroupsByTag(@NonNull String tagCode) {
        return deviceGroupsByTag(tagCode, new QueryDeviceGroupParam());
    }

    public List<DeviceGroupVO> deviceGroupsByTag(@NonNull String tagCode, QueryDeviceGroupParam param) {
        List<BizTag> bizTags = getBizTags(tagCode);
        if (CollectionUtils.isEmpty(bizTags)) {
            return Collections.emptyList();
        }
        List<Long> groupIds = this.centerBizTagMapper.queryDeviceIdsByTagCode(tagCode);
        if (CollectionUtils.isEmpty(groupIds)) {
            return Collections.emptyList();
        }
        List<DeviceGroupVO> deviceGroupVOS = this.deviceGroupMapper.deviceGroupsByTag(groupIds,
                                                                                      bizTags.getFirst().getId(), param);
        if (CollectionUtils.isEmpty(deviceGroupVOS)) {
            return Collections.emptyList();
        }
        Set<Long> prodIds = deviceGroupVOS.stream().map(DeviceGroupVO::getDevices)
                .flatMap(Collection::stream)
                .map(DevicePageInfoVO::getIotProductId).collect(Collectors.toSet());
        Map<Long, DeviceProductInfoDTO> productInfoDTOMap = new HashMap<>();
        Map<Long, FileVO> productFileMaps = new HashMap<>();
        if (CollectionUtils.isNotEmpty(prodIds)) {
            List<DeviceProductInfoDTO> deviceProductInfoDTOS = this.productInfoMapper.selectList(prodIds);
            if (CollectionUtils.isNotEmpty(deviceProductInfoDTOS)) {
                Set<Long> fileIds = deviceProductInfoDTOS.stream().filter(t -> Objects.nonNull(t.getPicture()))
                        .map(DeviceProductInfoDTO::getPicture).collect(Collectors.toSet());
                if (CollectionUtils.isNotEmpty(fileIds)) {
                    productFileMaps = this.fileClient.getByIds(new ArrayList<>(fileIds)).stream()
                            .collect(Collectors.toMap(FileVO::getId, Function.identity()));
                }

                productInfoDTOMap =
                        deviceProductInfoDTOS.stream().collect(Collectors.toMap(DeviceProductInfoDTO::getId, s -> s));
            }
        }
        for (DeviceGroupVO deviceGroupVO : deviceGroupVOS) {
            for (DevicePageInfoVO device : deviceGroupVO.getDevices()) {
                device.setProductInfo(productInfoDTOMap.get(device.getIotProductId()));
                if (Objects.nonNull(device.getProductInfo()) && Objects.nonNull(device.getProductInfo().getPicture())) {
                    device.getProductInfo().setImage(productFileMaps.get(device.getProductInfo().getPicture()));
                }
            }
        }
        return deviceGroupVOS;
    }
}
