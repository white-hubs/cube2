package com.guhecloud.cube.base.message2.server.sat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgChannel;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgChannelModifyParam;

import java.util.Collection;
import java.util.List;

public interface SatMsgChannelService extends IService<SatMsgChannel> {

    Long save(SatMsgChannelModifyParam param);

    void update(SatMsgChannel satMsgChannel, SatMsgChannelModifyParam param);

    void updateManufacturer(SatMsgChannel satMsgChannel, SatMsgChannelModifyParam param);

    /**
     * 修改启用状态
     *
     * @param id
     * @param enable
     */
    void updateEnabled(Long id, Boolean enable);

    /**
     * 获取通道配置
     *
     * @param channelCode
     *
     * @return
     */
    SatMsgChannel getByChannelCode(ChannelEnum channelCode);

    /**
     * 获取通道下默认开启的配置
     *
     * @param channelCode
     *
     * @return
     */
    SatMsgChannel getEnableConfigByCode(ChannelEnum channelCode);

    /**
     * 获取通道下默认开启的配置列表
     *
     * @param channelCodes
     *
     * @return
     */
    List<SatMsgChannel> getEnableConfigByCodes(Collection<ChannelEnum> channelCodes);

    /**
     * 配置列表
     *
     * @return
     */
    List<SatMsgChannel> listSatMsgChannels();

    /**
     * 通道下配置列表
     *
     * @param channelCode
     *
     * @return
     */
    List<SatMsgChannel> listSatMsgChannels(ChannelEnum channelCode);

    void remove(Long id);
}
