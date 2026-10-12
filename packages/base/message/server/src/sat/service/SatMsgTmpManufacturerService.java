package com.guhecloud.cube.base.message2.server.sat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgTmpManufacturer;

public interface SatMsgTmpManufacturerService extends IService<SatMsgTmpManufacturer> {

    SatMsgTmpManufacturer getSatMsgTmpManufacturer(Long satMsgTmpId, ChannelEnum channelCode, Long satMsgManufacturerId);

    void settingTmpManufacturerConfig(Long satMsgTmpId, ChannelEnum channelCode, Long satMsgManufacturerId, String tmpConfig);
}
