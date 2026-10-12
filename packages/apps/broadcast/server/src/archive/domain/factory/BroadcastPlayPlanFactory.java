package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.factory;

import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.commandgateway.command.BroadcastPlayTextCirculateCommand;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.aggregates.BroadcastPlayPlan;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.entity.BroadcastPlayText;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.po.BroadcastTemplatePO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.valueobject.BroadcastTemplate;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.valueobject.Circulate;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.valueobject.DeviceInfo;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.infrastruceture.repository.mapper.BroadcastTemplateMapper;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;

public class BroadcastPlayPlanFactory {

    /**
     * 创建广播计划
     */
    public static BroadcastPlayPlan newBroadcastPlayPlan(final BroadcastPlayTextCirculateCommand playTextCirculateCommand) {
        BroadcastTemplateMapper broadcastTemplateMapper = ApplicationContextHolder.getBean(BroadcastTemplateMapper.class);
        BroadcastTemplatePO broadcastTemplatePO = broadcastTemplateMapper.selectById(playTextCirculateCommand.getId());
        //模板信息
        BroadcastTemplate broadcastTemplate = new BroadcastTemplate(playTextCirculateCommand.getId(), broadcastTemplatePO.getName(), broadcastTemplatePO.getContent());
        //播放信息
        BroadcastPlayText broadcastPlayText = new BroadcastPlayText(broadcastTemplate,
                                                                    playTextCirculateCommand.getBroadcastPlayVariableParams(),
                                                                    1,
                                                                    ObjectUtils.copying(playTextCirculateCommand.getBroadcastPlayDevices(), DeviceInfo.class)
        );
        return new BroadcastPlayPlan(
                broadcastPlayText,
                new Circulate(playTextCirculateCommand.getStartTime(), playTextCirculateCommand.getEndTime()),
                SecurityContextHolder.getPrincipal().getId().toString(),
                null,
                null,
                null
        );
    }
}
