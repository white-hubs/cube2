package com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoWithDeviceDTO;
import com.guhecloud.cube.base.device.center.metadata.lang.Const;
import com.guhecloud.cube.base.device.center.metadata.param.DeviceBaseInfoPageQry;
import com.guhecloud.cube.base.device.center.metadata.param.TagBindDeviceCmd;
import com.guhecloud.cube.base.device.center.metadata.std.param.CreateTagCmd;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterTagStdClient;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainApplicationEnum;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.TagCode;
import com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.command.OutsourceDeviceImportCommand;
import com.guhecloud.cube.srv.gen.maintain.server.repair.dao.OutsourceDeviceInstanceMapper;
import com.guhecloud.cube.srv.gen.maintain.server.repair.interfaces.vo.OutsourceDeviceVO;
import com.guhecloud.cube.srv.gen.maintain.server.repair.po.OutsourceDeviceInstancePO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/5/19 14:28
 * @description
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OutsourceDeviceInstanceService
        extends ServiceImpl<OutsourceDeviceInstanceMapper, OutsourceDeviceInstancePO> {

    private final DeviceCenterTagStdClient deviceCenterTagStdClient;
    private final DeviceCenterStdClient deviceCenterStdClient;

    public void batchDelete(OutsourceDeviceImportCommand importCommand) {
        deviceCenterTagStdClient.unbindTag(TagBindDeviceCmd.of(
                TagCode.OUTSOURCE_REPAIR,
                importCommand.getDeviceInstanceIds()));
    }

    public void outsourceDeviceImport(OutsourceDeviceImportCommand importCommand) {
        checkAndSetDeviceTag();
        deviceCenterTagStdClient.bindTag(TagBindDeviceCmd.of(
                TagCode.OUTSOURCE_REPAIR,
                importCommand.getDeviceInstanceIds()));
    }

    /**
     * 检查委外设备tag是否已经存在,不存在则新建tag
     */
    private void checkAndSetDeviceTag() {
        if (deviceCenterTagStdClient.checkTag(MaintainApplicationEnum.OUTSOURCE_REPAIR.name()) == 0) {
            CreateTagCmd topTag = CreateTagCmd.createTopTag(Const.CODE_APP_INNER_TAG_TYPE,
                                                            TagCode.OUTSOURCE_REPAIR,
                                                            MaintainApplicationEnum.OUTSOURCE_REPAIR.getDescription());
            deviceCenterTagStdClient.createTag(topTag);
        }
    }

    public Pagination<OutsourceDeviceVO> outsourceDeviceList(DeviceBaseInfoPageQry pageQry) {
        pageQry.setTagCodes(Lists.newArrayList(TagCode.OUTSOURCE_REPAIR));
        Pagination<DeviceBaseArchivesInfoDTO> baseInfo = deviceCenterStdClient.pageOfBaseInfo(pageQry);
        List<OutsourceDeviceVO> deviceVOS = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(baseInfo.getRecords())) {
            Set<Long> deviceIds = baseInfo.getRecords().stream().map(DeviceBaseArchivesInfoDTO::getId).collect(Collectors.toSet());
            List<DeviceProductInfoWithDeviceDTO> productInfoByDeviceId = deviceCenterStdClient.getProductInfoByDeviceId(deviceIds);
            Map<Long, DeviceProductInfoDTO> map = new HashMap<>();
            if (ObjectUtils.isNotEmpty(productInfoByDeviceId)) {
                map = productInfoByDeviceId.stream()
                        .collect(Collectors.toMap(DeviceProductInfoWithDeviceDTO::getDeviceId, DeviceProductInfoWithDeviceDTO::getProductInfo));
            }
            for (DeviceBaseArchivesInfoDTO record : baseInfo.getRecords()) {
                DeviceProductInfoDTO productInfoDTO =
                        ObjectUtils.isNull(map.get(record.getId())) ? new DeviceProductInfoDTO() : map.get(record.getId());
                deviceVOS.add(OutsourceDeviceVO.builder()
                                      .deviceCode(record.getCode())
                                      .deviceName(record.getName())
                                      .deviceId(record.getId())
                                      .deviceBrand(productInfoDTO.getBrandName())
                                      .deviceModel(productInfoDTO.getModelName())
                                      .deviceCategory(ObjectUtils.isNull(productInfoDTO.getProductCategory()) ? "" : productInfoDTO
                                              .getProductCategory().getName())
                                      .areaName(ObjectUtils.isNull(record.getArea()) ? "" : record.getArea().getVal())
                                      .build());
            }
        }
        return PagingUtils.convert(baseInfo, deviceVOS);
    }
}
