package com.guhecloud.cube.base.message2.server.sat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgSceneReceive;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgSceneReceiveModifyParam;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgSceneReceiveQueryParam;
import com.guhecloud.cube.base.message2.server.sat.vo.SatMsgApplicationVO;

import java.util.List;

public interface SatMsgSceneReceiveService extends IService<SatMsgSceneReceive> {

    void refreshApplication();

    void settingReceiveMode(Long id, SatMsgSceneReceiveModifyParam param);

    void settingReceiveChannel(Long id, ChannelEnum channelCode);

    void removeReceiveChannel(Long id, ChannelEnum channelCode);

    SatMsgSceneReceive getSatMsgSceneReceiveByCode(String code);

    Page<SatMsgSceneReceive> getSatMsgSceneReceivePage(SatMsgSceneReceiveQueryParam param);

    List<SatMsgApplicationVO> getSatMsgSceneApplications();
}
