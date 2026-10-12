package com.guhecloud.cube.base.alarm.server.flow.interfaces;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.AlarmRecordQueryService;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.query.AlarmRecordQueryParam;
import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordPO;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmHandlerUser;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.message.AlarmMessageSender;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.AlarmRecordRepository;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmRecordVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.UnConfirmedCountByStatusVO;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 报警消息接口
 *
 * @author lanchun
 * @date 2022/6/7 14:41
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmMsgController {

    private final AlarmRecordQueryService alarmRecordQueryService;

    private final FileClient fileClient;

    private final AlarmMessageSender alarmMessageSender;

    private final AlarmRecordRepository alarmRecordRepository;

    /**
     * 获取当前用户未处理的报警消息数量
     *
     * @return
     */
    @GetMapping("/alarm-msg/un-confirmed/count")
    public Long getUserUnConfirmedCount() {
        return this.alarmRecordQueryService.getUserUnConfirmedCount();
    }

    /**
     * 获取当前用户未处理的报警消息列表
     *
     * @return
     */
    @GetMapping("/alarm-msg/un-confirmed/records")
    public Pagination<AlarmRecordVO> listUserUnConfirmedAlarmRecords(@Valid AlarmRecordQueryParam param) {
        Page<AlarmRecordPO> alarmRecordPOPage = this.alarmRecordQueryService.listUserUnConfirmedAlarmRecords(param);
        if (CollectionUtils.isEmpty(alarmRecordPOPage.getRecords())) {
            return Pagination.empty();
        }
        List<AlarmRecordVO> alarmRecordVOS = ObjectUtils.copying(alarmRecordPOPage.getRecords(), AlarmRecordVO.class);
        List<Long> fileIds = alarmRecordVOS.stream().map(alarmRecordVO -> alarmRecordVO.getAlarmTypeFileId()).distinct()
                .collect(Collectors.toList());
        Map<Long, List<FileVO>> fileVOS = this.fileClient.getByIds(fileIds).stream()
                .collect(Collectors.groupingBy(FileVO::getId));
        alarmRecordVOS.parallelStream().forEach(alarmRecordVO -> {
            if (Objects.nonNull(alarmRecordVO.getAlarmTypeFileId())) {
                List<FileVO> fileVOList = fileVOS.get(alarmRecordVO.getAlarmTypeFileId());
                if (!CollectionUtils.isEmpty(fileVOList)) {
                    alarmRecordVO.setFileVO(fileVOList.get(0));
                }
            }
        });
        return PagingUtils.convert(alarmRecordPOPage, alarmRecordVOS);
    }

    /**
     * 获取当前用户任务类型数据统计
     *
     * @return
     */
    @DataPermissionSwitch(dataModel = "alarmCenter:alarmRecordHandlerUser")
    @GetMapping("/alarm-msg/un-confirmed/countByStatus")
    public UnConfirmedCountByStatusVO getUserUnConfirmedCountByStatus(@Valid AlarmRecordQueryParam param) {
        return this.alarmRecordQueryService.getUserUnConfirmedCountByStatus(param);
    }

    /**
     * 报警消息再次发送
     *
     * @param id
     */
    @GetMapping("/alarm-msg/re-send/{id}")
    public void reSendAlarmMsg(@PathVariable("id") Long id) {
        AlarmRecord detail = this.alarmRecordRepository.detail(id);
        if (!CollectionUtils.isEmpty(detail.getAlarmHandlerUsers())) {
            //发送通知
            this.alarmMessageSender.alarmAssignSend(detail,
                                                    detail.getAlarmHandlerUsers()
                                                            .stream().map(AlarmHandlerUser::getId).collect(Collectors.toSet()));
        }
        //发送报警广播通知
        this.alarmMessageSender.alarmBroadcastSend(detail);
    }
}
