package com.guhecloud.cube.base.alarm.server.flow.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.alarm.server.common.facade.device.AlarmBimStatisticsQueryConvert;
import com.guhecloud.cube.base.alarm.server.flow.application.AlarmRecordHandlerReasonService;
import com.guhecloud.cube.base.alarm.server.flow.application.AlarmRecordPushConfigService;
import com.guhecloud.cube.base.alarm.server.flow.application.AlarmStateEventOperateService;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.query.AlarmRecordPageNumberQueryParam;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.query.AlarmRecordQueryParam;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.query.AlarmRecordStandardQueryParam;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmHandlerResultEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordPO;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordPushConfigPO;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmStateEventOperatePO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordMapper;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.bo.UnConfirmedCountByStatusBO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmRecordExportVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmRecordFollowUpVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmRecordHandlerReasonVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmRecordPageVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmRecordVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.HandlerVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.SuspendVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.UnConfirmedCountByStatusVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmRecordQueryService {

    private final AlarmRecordMapper alarmRecordMapper;
    private final AlarmStateEventOperateService alarmStateEventOperateService;
    private final AlarmRecordFollowUpQueryService alarmRecordFollowUpQueryService;
    private final AlarmRecordHandlerReasonService alarmRecordHandlerReasonService;
    private final AlarmBimStatisticsQueryConvert alarmBimStatisticsQueryConvert;
    private final AlarmRecordPushConfigService alarmRecordPushConfigService;
    private final UserClient userClient;

    /**
     * 报警详情
     *
     * @param id
     *
     * @return
     */
    public AlarmRecordPO getById(@NonNull Serializable id) {
        return this.alarmRecordMapper.selectById(id);
    }

    /**
     * 报警详情
     *
     * @param businessKey
     *
     * @return
     */
    public AlarmRecordPO getByBusinessKey(@NonNull String businessKey) {
        return this.alarmRecordMapper.selectOne(Wrappers.<AlarmRecordPO>lambdaQuery()
                                                        .eq(AlarmRecordPO::getBusinessKey, businessKey));
    }

    /**
     * 报警分页列表
     *
     * @param param
     *
     * @return
     */
    public Page<AlarmRecordPageVO> getPage(AlarmRecordQueryParam param) {
        String area = "";
        if (!CollectionUtils.isEmpty(param.getAreaIds())) {
            area = String.join("-", param.getAreaIds());
        }
        if (!CollectionUtils.isEmpty(param.getDeviceTags()) || Objects.nonNull(param.getSceneType())) {
            List<Long> deviceIds = alarmBimStatisticsQueryConvert
                    .getAlarmBimStatisticsQueryParamDeviceIds(param.getDeviceTags(), param.getSceneType(), param.getAreaId());
            if (CollectionUtils.isEmpty(deviceIds)) {
                return new Page<>();
            }
            param.setDeviceIds(deviceIds);
        }
        param.setArea(area);
        return this.alarmRecordMapper.selectAlarmPage(PagingUtils.convert(param), param);
    }

    /**
     * 标准报警列表接口
     *
     * @param query
     *
     * @return
     */
    public List<AlarmRecordPageVO> getStandardAlarmRecords(AlarmRecordStandardQueryParam query) {
        return this.alarmRecordMapper.selectStandardAlarmRecords(query, SessionContextHolder.getRequester().getTenant());
    }

    public List<AlarmRecordExportVO> getExportList(AlarmRecordQueryParam param) {
        return this.alarmRecordMapper.selectExportList(param);
    }

    public Page<AlarmRecordPageVO> getUnHandlerList(AlarmRecordQueryParam param) {
        return this.alarmRecordMapper.selectUnHandlerList(param, PagingUtils.convert(param));
    }

    public long getUnHandlerPageNumber(AlarmRecordPageNumberQueryParam param) {
        Integer selectUnHandlerPageNumber = this.alarmRecordMapper.selectUnHandlerRowNumber(param);
        if (Objects.isNull(selectUnHandlerPageNumber)) {
            return 0;
        }
        long pages = selectUnHandlerPageNumber / param.getPageSize();
        if (selectUnHandlerPageNumber % param.getPageSize() != 0) {
            pages++;
        }
        return pages;
    }

    public Long getUserUnConfirmedCount() {
        return this.alarmRecordMapper.selectUserUnConfirmedCount(SecurityContextHolder.getPrincipal()
                                                                         .getId(), SessionContextHolder
                                                                         .getRequester().getTerminal());
    }

    public Page<AlarmRecordPO> listUserUnConfirmedAlarmRecords(AlarmRecordQueryParam param) {
        return this.alarmRecordMapper.selectUserUnConfirmedAlarmRecords(PagingUtils.convert(param), SecurityContextHolder
                .getPrincipal().getId(), param);
    }

    public UnConfirmedCountByStatusVO getUserUnConfirmedCountByStatus(AlarmRecordQueryParam param) {
        String area = "";
        if (!CollectionUtils.isEmpty(param.getAreaIds())) {
            area = String.join("-", param.getAreaIds());
        }
        param.setArea(area);
        List<UnConfirmedCountByStatusBO> data = this.alarmRecordMapper.getUserUnConfirmedCountByStatus(param);
        UnConfirmedCountByStatusVO statusData = new UnConfirmedCountByStatusVO();
        statusData.setAlarmUnConfirmed(0);
        statusData.setAlarmProgressConfirmed(0);
        statusData.setAlarmSuspend(0);
        statusData.setAlarmHandler(0);
        data.stream().forEach(ex -> {
            switch (ex.getCode()) {
                case "ALARM_UN_CONFIRMED":
                    statusData.setAlarmUnConfirmed(ex.getNum());
                    break;
                case "ALARM_PROGRESS_CONFIRMED":
                    statusData.setAlarmProgressConfirmed(ex.getNum());
                    break;
                case "ALARM_SUSPEND":
                    statusData.setAlarmSuspend(ex.getNum());
                    break;
                case "ALARM_HANDLER":
                    statusData.setAlarmHandler(ex.getNum());
                    break;
                default:
            }
        });
        return statusData;
    }

    public AlarmRecordVO getDetailed(AlarmRecordVO alarmRecordVO) {
        // 挂起、处理详情
        List<AlarmStateEventOperatePO> operatePOList = alarmStateEventOperateService.lambdaQuery()
                .eq(AlarmStateEventOperatePO::getAlarmRecordId, alarmRecordVO.getId())
                .and(ex -> ex
                        .eq(AlarmStateEventOperatePO::getAlarmStatus, AlarmStatusEnum.ALARM_SUSPEND.name())
                        .or()
                        .eq(AlarmStateEventOperatePO::getAlarmStatus, AlarmStatusEnum.ALARM_HANDLER.name()))
                .list();
        List<AlarmRecordFollowUpVO> alarmRecordFollowUpVOList = this.alarmRecordFollowUpQueryService.getAlarmRecordFollowUpByAlarmId(alarmRecordVO.getId());
        alarmRecordVO.setFollowUps(alarmRecordFollowUpVOList);
        AlarmRecordPushConfigPO alarmPushConfig = this.alarmRecordPushConfigService.getAlarmRecordPushConfigPO(alarmRecordVO.getId());
        if (Objects.nonNull(alarmPushConfig)) {
            alarmRecordVO.setTurnWarningSound(alarmPushConfig.getTurnWarningSound());
        }
        operatePOList.forEach(ex -> {
            UserDTO userDTO;
            if (Objects.nonNull(ex.getCreatorId()) && ActorTypeEnum.USER.name().equals(ex.getCreatorType())) {
                userDTO = this.userClient.getUser(Long.valueOf(ex.getCreatorId()));
            } else {
                userDTO = this.userClient.getUserByUserName(ex.getCreateBy());
            }
            String createUserName = "";
            if (Objects.nonNull(userDTO)) {
                createUserName = userDTO.getName();
            }
            if (AlarmStatusEnum.ALARM_HANDLER.equals(ex.getAlarmStatus())) {
                HandlerVO handlerVO = JsonUtils.deserialize(ex.getOperationParam(), HandlerVO.class);
                handlerVO.setUserId(alarmRecordVO.getAlarmHandlerBy());
                handlerVO.setAlarmHandlerResultName(Objects
                                                            .requireNonNull(AlarmHandlerResultEnum.getAlarmImpractical(handlerVO.getAlarmHandlerResult()))
                                                            .getDescription());
                handlerVO.setCreateTime(ex.getCreateTime());
                handlerVO.setCreateUserName(alarmRecordVO.getAlarmHandlerName());
                alarmRecordVO.setHandler(handlerVO);
            } else if (AlarmStatusEnum.ALARM_SUSPEND.equals(ex.getAlarmStatus())) {
                SuspendVO suspendVO = JsonUtils.deserialize(ex.getOperationParam(), SuspendVO.class);
                suspendVO.setUserId(userDTO.getId());
                suspendVO.setCreateTime(ex.getCreateTime());
                suspendVO.setCreateUserName(createUserName);
                alarmRecordVO.setSuspend(suspendVO);
            }
        });
        if (Objects.nonNull(alarmRecordVO.getHandler())) {
            Optional<AlarmRecordHandlerReasonVO> alarmRecordHandlerReason = this.alarmRecordHandlerReasonService
                    .getAlarmRecordHandlerReason(alarmRecordVO.getId());
            alarmRecordHandlerReason.ifPresent(reason ->
                                                       alarmRecordVO.getHandler().setRecordHandlerReason(reason)
                                              );
        }
        return alarmRecordVO;
    }
}