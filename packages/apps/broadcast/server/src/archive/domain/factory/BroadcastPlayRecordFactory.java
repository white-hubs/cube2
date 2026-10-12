package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.factory;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.commandgateway.command.BroadcastPlayTextCommand;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.aggregates.BroadcastPlayRecord;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.entity.BroadcastPlayText;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.po.BroadcastTemplatePO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.valueobject.BroadcastTemplate;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.valueobject.DeviceInfo;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.infrastruceture.repository.mapper.BroadcastTemplateMapper;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;

import java.util.Objects;

public class BroadcastPlayRecordFactory {

    /**
     * 创建广播播放记录信息
     *
     * @param broadcastPlayTextCommand
     *
     * @return
     */
    public static BroadcastPlayRecord newBroadcastPlayRecord(final BroadcastPlayTextCommand broadcastPlayTextCommand) {
        BroadcastTemplateMapper broadcastTemplateMapper = ApplicationContextHolder.getBean(BroadcastTemplateMapper.class);
        BroadcastTemplatePO broadcastTemplatePO = broadcastTemplateMapper.selectById(broadcastPlayTextCommand.getId());
        //模板信息
        BroadcastTemplate broadcastTemplate = new BroadcastTemplate(broadcastPlayTextCommand.getId(), broadcastTemplatePO.getName(), broadcastTemplatePO.getContent());
        //播放信息
        BroadcastPlayText broadcastPlayText = new BroadcastPlayText(broadcastTemplate,
                                                                    broadcastPlayTextCommand.getBroadcastPlayVariableParams(),
                                                                    broadcastPlayTextCommand.getBroadcastPlayNum(),
                                                                    ObjectUtils.copying(broadcastPlayTextCommand.getBroadcastPlayDevices(), DeviceInfo.class)
        );
        //操作人信息
        UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
        UserDTO userDTO = null;
        if (Objects.nonNull(SecurityContextHolder.getPrincipal(true))) {
            userDTO = userClient.getUser(SecurityContextHolder.getPrincipal().getId());
        }
        return new BroadcastPlayRecord(
                broadcastPlayText,
                broadcastPlayTextCommand.getBroadcastPlayPlanId(),
                Objects.nonNull(userDTO) ? userDTO.getId().toString() : null,
                Objects.nonNull(userDTO) ? userDTO.getName() : null,
                Objects.nonNull(userDTO) && Objects.nonNull(userDTO.getOrganization()) ? userDTO.getOrganization().getId() : null,
                Objects.nonNull(userDTO) && Objects.nonNull(userDTO.getOrganization()) ? userDTO.getOrganization().getName() : null
        );
    }
}
