package com.guhecloud.cube.base.message2.server.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.message2.server.base.entity.MsgChannel;
import com.guhecloud.cube.base.message2.server.base.param.MsgChannelModifyParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgChannelQueryParam;

import java.util.List;

public interface MsgChannelService extends IService<MsgChannel> {

    Long save(MsgChannelModifyParam param);

    MsgChannel getByCode(String code);

    List<MsgChannel> listMsgChannels(MsgChannelQueryParam param);

    void remove(String code);
}
