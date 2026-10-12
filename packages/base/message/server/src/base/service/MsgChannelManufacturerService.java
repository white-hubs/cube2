package com.guhecloud.cube.base.message2.server.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.message2.server.base.entity.MsgChannelManufacturer;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.base.param.MsgChannelManufacturerModifyParam;

import java.util.List;

public interface MsgChannelManufacturerService extends IService<MsgChannelManufacturer> {

    /**
     * 通道关联厂家
     *
     * @param channelCode
     * @param param
     *
     * @return
     */
    Long save(ChannelEnum channelCode, MsgChannelManufacturerModifyParam param);

    /**
     * 移除通道厂家
     *
     * @param channelCode
     * @param manufacturerCode
     */
    void remove(ChannelEnum channelCode, ManufacturerCodeEnum manufacturerCode);

    /**
     * 通道厂家列表
     *
     * @param channelCode
     *
     * @return
     */
    List<MsgChannelManufacturer> listMsgChannelManufacturersByCode(ChannelEnum channelCode);

    /**
     * 获取通道厂家配置
     *
     * @param channelCode
     * @param manufacturerCode
     *
     * @return
     */
    MsgChannelManufacturer getMsgChannelManufacturer(ChannelEnum channelCode, ManufacturerCodeEnum manufacturerCode);
}
