package com.guhecloud.cube.base.message2.server.bff.controller;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.param.MsgRecordQueryParam;
import com.guhecloud.cube.base.message2.server.base.vo.MsgRecordPageVO;
import com.guhecloud.cube.base.message2.server.bff.constant.BffConstants;
import com.guhecloud.cube.base.message2.server.bff.param.MsgCustomGroupParam;
import com.guhecloud.cube.base.message2.server.bff.param.MsgCustomReadAllParam;
import com.guhecloud.cube.base.message2.server.bff.service.MsgRecordBffService;
import com.guhecloud.cube.base.message2.server.bff.vo.MsgCustomGroupVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.DateUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 消息统计接口管理
 *
 * @author lanchun
 * @date 2024/7/8 14:00
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MsgRecordBffController {

    private final MsgRecordBffService msgRecordBffService;

    private static final Set<MsgCustomGroupParam> MSG_CUSTOM_GROUP_PARAMS = new HashSet<>();

    private static final Map<String, MsgCustomGroupParam> MSG_CUSTOM_GROUP_PARAMS_MAP = new HashMap<>();

    static {
        //报警消息
        //旧报警消息
        Set<MsgCustomGroupParam.CustomGroups> oldCustomGroups = new HashSet<>();
        oldCustomGroups.add(MsgCustomGroupParam.CustomGroups.builder().sceneCode("ALARM_ASSIGN_NOTIFY").build());
        oldCustomGroups.add(MsgCustomGroupParam.CustomGroups.builder().sceneCode("EMERGENCY_ALARM").build());
        oldCustomGroups.add(MsgCustomGroupParam.CustomGroups.builder().sceneCode("ALARM_TIMEOUT_NOTIFY").build());
        oldCustomGroups.add(MsgCustomGroupParam.CustomGroups.builder().msgType("ALARM_NOTICE").build());
        MsgCustomGroupParam alarmGroup = MsgCustomGroupParam.builder()
                .type("ALARM_NOTICE")
                .typeName("安全报警")
                .customGroups(oldCustomGroups)
                .build();
        MSG_CUSTOM_GROUP_PARAMS.add(alarmGroup);
        MSG_CUSTOM_GROUP_PARAMS_MAP.put("ALARM_NOTICE", alarmGroup);
        //审批消息
        Set<MsgCustomGroupParam.CustomGroups> customGroups = new HashSet<>();
        customGroups.add(MsgCustomGroupParam.CustomGroups.builder().sceneCode("TASK_COPY").build());
        customGroups.add(MsgCustomGroupParam.CustomGroups.builder().sceneCode("PROCESS_UNDO").build());
        customGroups.add(MsgCustomGroupParam.CustomGroups.builder().sceneCode("TASK_TRANSFER").build());
        customGroups.add(MsgCustomGroupParam.CustomGroups.builder().sceneCode("APPROVAL_PASSED_START_USER").build());
        customGroups.add(MsgCustomGroupParam.CustomGroups.builder().sceneCode("TASK_BACKLOG").build());
        customGroups.add(MsgCustomGroupParam.CustomGroups.builder().sceneCode("TASK_REFUSE").build());
        customGroups.add(MsgCustomGroupParam.CustomGroups.builder().sceneCode("TASK_END").build());
        customGroups.add(MsgCustomGroupParam.CustomGroups.builder().sceneCode("TASK_OVERDUE").build());
        customGroups.add(MsgCustomGroupParam.CustomGroups.builder().sceneCode("AUTOMATIC_APPROVAL").build());
        MsgCustomGroupParam approveGroup = MsgCustomGroupParam.builder()
                .type("APPROVAL_MESSAGE")
                .typeName("审批工单")
                .customGroups(customGroups)
                .build();
        MSG_CUSTOM_GROUP_PARAMS.add(approveGroup);
        MSG_CUSTOM_GROUP_PARAMS_MAP.put("APPROVAL_MESSAGE", approveGroup);
        //系统消息
        MsgCustomGroupParam sysGroup = MsgCustomGroupParam.builder()
                .type("SYSTEM_MESSAGE")
                .typeName("系统消息")
                .customGroups(Collections.singleton(MsgCustomGroupParam.CustomGroups.builder().sceneCode("APP_VERSION_UPGRADE_NOTICE")
                                                            .build()))
                .build();
        MSG_CUSTOM_GROUP_PARAMS.add(sysGroup);
        MSG_CUSTOM_GROUP_PARAMS_MAP.put("SYSTEM_MESSAGE", sysGroup);
    }

    /**
     * 消息自定义分组统计
     *
     * @param channelType
     *
     * @return
     */
    @GetMapping("/msg-record/custom-group/{channelType}/statistics")
    public List<MsgCustomGroupVO> getMsgCustomGroups(@PathVariable("channelType") ChannelEnum channelType) {
        List<MsgCustomGroupVO> msgCustomGroups = this.msgRecordBffService.getMsgCustomGroups(channelType, MSG_CUSTOM_GROUP_PARAMS);
        return msgCustomGroups.stream()
                .sorted(Comparator.comparing(
                        x -> Optional.ofNullable(((MsgCustomGroupVO) x).getLastMsgRecord())
                                .map(msgRecordPageVO -> DateUtils.toTimestamp(msgRecordPageVO.getCreateTime())).orElse(0L),
                        Comparator.naturalOrder()
                                            ).reversed())
                .collect(Collectors.toList());

    }

    /**
     * 自定义分组的消息列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/msg-record/custom-group/page")
    public Pagination<MsgRecordPageVO> listMsgRecords(@Valid MsgRecordQueryParam param) {
        if (BffConstants.CUSTOM.equals(param.getCategory())) {
            MsgCustomGroupParam msgCustomGroupParam = MSG_CUSTOM_GROUP_PARAMS_MAP.get(param.getCustomType());
            param.setMsgCustomGroup(msgCustomGroupParam);
        } else if (BffConstants.APPLICATION.equals(param.getCategory())) {
            param.setApplicationCode(param.getCustomType());
            param.setFilterMsgCustomGroups(MSG_CUSTOM_GROUP_PARAMS);
        }
        return this.msgRecordBffService.listMsgRecords(param);
    }

    /**
     * 自定义消息分组一键已读
     *
     * @param channelTypeEnum
     */
    @PutMapping("/msg-record/custom-group/read-all/{channelType}")
    public void readAll(@NotNull(message = "通道类型不能为空") @PathVariable("channelType") ChannelEnum channelTypeEnum,
                        @Valid @RequestBody MsgCustomReadAllParam param
                       ) {
        if (BffConstants.CUSTOM.equals(param.getCategory())) {
            MsgCustomGroupParam msgCustomGroupParam = MSG_CUSTOM_GROUP_PARAMS_MAP.get(param.getCustomType());
            param.setMsgCustomGroup(msgCustomGroupParam);
        } else if (BffConstants.APPLICATION.equals(param.getCategory())) {
            param.setApplicationCode(param.getCustomType());
            param.setFilterMsgCustomGroups(MSG_CUSTOM_GROUP_PARAMS);
        }
        this.msgRecordBffService.readAll(channelTypeEnum, param);
    }

}