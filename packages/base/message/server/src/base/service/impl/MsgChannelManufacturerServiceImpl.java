package com.guhecloud.cube.base.message2.server.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.message2.server.base.dao.MsgChannelManufacturerMapper;
import com.guhecloud.cube.base.message2.server.base.entity.MsgChannelManufacturer;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.base.param.MsgChannelManufacturerModifyParam;
import com.guhecloud.cube.base.message2.server.base.service.MsgChannelManufacturerService;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class MsgChannelManufacturerServiceImpl extends ServiceImpl<MsgChannelManufacturerMapper, MsgChannelManufacturer> implements MsgChannelManufacturerService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull ChannelEnum channelCode, @NonNull MsgChannelManufacturerModifyParam param) {
        MsgChannelManufacturer msgChannelManufacturer = ObjectUtils.copying(param, MsgChannelManufacturer.class);
        this.baseMapper.insert(msgChannelManufacturer);
        return msgChannelManufacturer.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull ChannelEnum channelCode, @NonNull ManufacturerCodeEnum manufacturerCode) {
        this.baseMapper.delete(Wrappers.<MsgChannelManufacturer>lambdaQuery()
                                       .eq(MsgChannelManufacturer::getChannelCode, channelCode)
                                       .eq(MsgChannelManufacturer::getManufacturerCode, manufacturerCode));
    }

    @Override
    public List<MsgChannelManufacturer> listMsgChannelManufacturersByCode(@NonNull ChannelEnum channelCode) {
        return this.lambdaQuery()
                .eq(MsgChannelManufacturer::getChannelCode, channelCode)
                .list();
    }

    @Override
    public MsgChannelManufacturer getMsgChannelManufacturer(@NonNull ChannelEnum channelCode,
                                                            @NonNull ManufacturerCodeEnum manufacturerCode
                                                           ) {
        return this.baseMapper.selectOne(Wrappers.<MsgChannelManufacturer>lambdaQuery()
                                                 .eq(MsgChannelManufacturer::getChannelCode, channelCode)
                                                 .eq(MsgChannelManufacturer::getManufacturerCode, manufacturerCode));
    }
}
