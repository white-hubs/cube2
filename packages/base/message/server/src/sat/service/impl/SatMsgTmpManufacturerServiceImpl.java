package com.guhecloud.cube.base.message2.server.sat.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.sat.dao.SatMsgTmpManufacturerMapper;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgTmpManufacturer;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgTmpManufacturerService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
public class SatMsgTmpManufacturerServiceImpl extends ServiceImpl<SatMsgTmpManufacturerMapper, SatMsgTmpManufacturer> implements SatMsgTmpManufacturerService {

    @Override
    public SatMsgTmpManufacturer getSatMsgTmpManufacturer(@NonNull Long satMsgTmpId, @NonNull ChannelEnum channelCode,
                                                          @NonNull Long satMsgManufacturerId
                                                         ) {
        return this.baseMapper.selectOne(Wrappers.<SatMsgTmpManufacturer>lambdaQuery()
                                                 .eq(SatMsgTmpManufacturer::getSatMsgTmpId, satMsgTmpId)
                                                 .eq(SatMsgTmpManufacturer::getChannelCode, channelCode)
                                                 .eq(SatMsgTmpManufacturer::getSatMsgManufacturerId, satMsgManufacturerId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void settingTmpManufacturerConfig(@NonNull Long satMsgTmpId, @NonNull ChannelEnum channelCode,
                                             @NonNull Long satMsgManufacturerId, @NonNull String tmpConfig
                                            ) {
        SatMsgTmpManufacturer satMsgTmpManufacturer = this.getSatMsgTmpManufacturer(satMsgTmpId, channelCode, satMsgManufacturerId);
        if (Objects.nonNull(satMsgTmpManufacturer)) {
            satMsgTmpManufacturer.setManufacturerTmpConfig(tmpConfig);
            this.baseMapper.updateById(satMsgTmpManufacturer);
        }
    }

}
