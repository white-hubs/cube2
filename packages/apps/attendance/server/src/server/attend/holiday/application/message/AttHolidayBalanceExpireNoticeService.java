package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.message;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.bracing.api.AssemblyNoticeClient;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendParam;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendSceneQueryPram;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayBalanceDeadlineMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayBalanceDeadlinePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayRulePO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 假期余额到期提醒
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttHolidayBalanceExpireNoticeService {

    private static final String BUSINESS_TYPE = "ATTENDANCE_MANAGEMENT";
    private static final String SCENE_CODE = "HOLIDAY_BALANCE_EXPIRE";

    private final AssemblyNoticeClient assemblyNoticeClient;
    private final AttHolidayBalanceDeadlineMapper attHolidayBalanceDeadlineMapper;
    private final AttHolidayRuleMapper attHolidayRuleMapper;

    public void sendExpireNotice(LocalDateTime nowTime) {
        LocalDateTime noticeTime = nowTime == null ? LocalDateTime.now() : nowTime;
        List<MessageSendConfig> configs = this.getNoticeConfigs();
        log.warn("{}余额到期通知configs：{}", SessionContextHolder.getRequester().getTenant(), JsonUtils.serialize(configs));
        if (CollectionUtils.isEmpty(configs)) {
            return;
        }
        Set<String> sendKeySet = new HashSet<>();
        for (MessageSendConfig config : configs) {
            this.sendExpireNoticeByConfig(noticeTime, config, sendKeySet);
        }
    }

    private List<MessageSendConfig> getNoticeConfigs() {
        MessageSendSceneQueryPram queryPram = new MessageSendSceneQueryPram();
        queryPram.setBusinessType(BUSINESS_TYPE);
        List<MessageSendConfig> configs = assemblyNoticeClient.getSendMsgConfig(queryPram);
        if (CollectionUtils.isEmpty(configs)) {
            return Collections.emptyList();
        }
        return configs.stream()
                .filter(config -> SCENE_CODE.equals(config.getSceneCode()))
                .filter(config -> config.getNotifyId() != null)
                .filter(config -> config.getValue() != null && config.getValue() >= AttConstants.zeroInt)
                .toList();
    }

    private void sendExpireNoticeByConfig(LocalDateTime nowTime, MessageSendConfig config, Set<String> sendKeySet) {
        LocalDate targetDate = nowTime.toLocalDate().plusDays(config.getValue());
        LocalDateTime startTime = targetDate.atStartOfDay();
        LocalDateTime endTime = targetDate.plusDays(1).atStartOfDay();
        List<AttHolidayBalanceDeadlinePO> deadlineList = attHolidayBalanceDeadlineMapper.selectList(
                Wrappers.<AttHolidayBalanceDeadlinePO>lambdaQuery()
                        .ge(AttHolidayBalanceDeadlinePO::getDeadline, startTime)
                        .lt(AttHolidayBalanceDeadlinePO::getDeadline, endTime)
                        .lt(AttHolidayBalanceDeadlinePO::getDeadline, AttConstants.PERMANENT_DATE)
                        .gt(AttHolidayBalanceDeadlinePO::getBalance, AttConstants.zeroLong)
                        .eq(AttHolidayBalanceDeadlinePO::getNormal, true)
                                                                                                   );
        if (CollectionUtils.isEmpty(deadlineList)) {
            return;
        }
        log.warn("{}余额到期通知deadlinePO：{}", SessionContextHolder.getRequester().getTenant(), JsonUtils.serialize(deadlineList));
        Map<Long, AttHolidayRulePO> holidayRuleMap = this.getHolidayRuleMap(deadlineList);
        for (AttHolidayBalanceDeadlinePO deadlinePO : deadlineList) {
            this.sendExpireNotice(deadlinePO, config, holidayRuleMap, sendKeySet);
        }
    }

    private Map<Long, AttHolidayRulePO> getHolidayRuleMap(List<AttHolidayBalanceDeadlinePO> deadlineList) {
        Set<Long> holidayRuleIds = deadlineList.stream()
                .map(AttHolidayBalanceDeadlinePO::getHolidayRuleId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(holidayRuleIds)) {
            return Collections.emptyMap();
        }
        List<AttHolidayRulePO> holidayRuleList = attHolidayRuleMapper.selectBatchIds(holidayRuleIds);
        if (CollectionUtils.isEmpty(holidayRuleList)) {
            return Collections.emptyMap();
        }
        return holidayRuleList.stream()
                .collect(Collectors.toMap(AttHolidayRulePO::getId, Function.identity(), (oldValue, newValue) -> oldValue));
    }

    private void sendExpireNotice(AttHolidayBalanceDeadlinePO deadlinePO, MessageSendConfig config,
                                  Map<Long, AttHolidayRulePO> holidayRuleMap, Set<String> sendKeySet
                                 ) {
        AttHolidayRulePO holidayRulePO = holidayRuleMap.get(deadlinePO.getHolidayRuleId());
        if (holidayRulePO == null || StringUtils.isBlank(holidayRulePO.getName()) || deadlinePO.getStaffId() == null ||
            deadlinePO.getDeadline() == null) {
            return;
        }
        String sendKey = config.getNotifyId() + "_" + deadlinePO.getStaffId() + "_" + deadlinePO.getHolidayRuleId() + "_" +
                         DateUtils.formatDate(deadlinePO.getDeadline());
        if (!sendKeySet.add(sendKey)) {
            return;
        }
        Map<String, String> tempParams = new HashMap<>();
        tempParams.put("holidayName", holidayRulePO.getName());
        tempParams.put("deadline", DateUtils.formatDate(deadlinePO.getDeadline()));
        MessageSendParam sendParam = MessageSendParam.builder()
                .notifyId(config.getNotifyId())
                .tempParams(tempParams)
                .notifyUsers(Collections.singleton(deadlinePO.getStaffId()))
                .build();
        log.warn("{}余额到期通知sendParam：{}", SessionContextHolder.getRequester().getTenant(), JsonUtils.serialize(sendParam));
        try {
            MessageSendResponse response = assemblyNoticeClient.sendMsg(sendParam);
            log.warn("{}余额到期通知response：{}", SessionContextHolder.getRequester().getTenant(), JsonUtils.serialize(response));
        } catch (Exception e) {
            log.error("假期余额到期提醒发送失败，deadlineId：{}", deadlinePO.getId(), e);
        }
    }
}
