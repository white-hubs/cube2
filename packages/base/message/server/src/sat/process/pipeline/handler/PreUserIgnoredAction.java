package com.guhecloud.cube.base.message2.server.sat.process.pipeline.handler;

import com.guhecloud.cube.base.message2.server.base.service.MsgRecordService;
import com.guhecloud.cube.base.message2.server.sat.constants.SatMsgConstant;
import com.guhecloud.cube.base.message2.server.sat.dto.SatUserMsgIgnoredConfigDTO;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.BusinessProcess;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.ProcessContext;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageRecordInfo;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageRecordTargetInfo;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.handler.context.MsgSendContext;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.guhecloud.cube.base.message2.server.handler.exeception.MessageExceptionConstant.USER_RECEIVER_IGNORED;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PreUserIgnoredAction implements BusinessProcess<MsgSendContext> {

    private final RedisTemplate<String, Object> redisTemplate;

    private final MsgRecordService msgRecordService;

    @Override
    public void process(ProcessContext<MsgSendContext> context) {
        LocalDateTime sendTime = DateUtils.now();
        Map<Long, MessageInfo> messageInfoMap = context.getProcessModel().getMessageInfos().stream()
                .collect(Collectors.toMap(MessageInfo::getId, s -> s, (k, v) -> v));
        for (MessageRecordInfo messageRecordInfo : context.getProcessModel().getMessageRecordInfos()) {
            Set<String> ignoredReceivers = new HashSet<>();
            for (MessageRecordTargetInfo targetInfo : messageRecordInfo.getMessageRecordTargetInfos()) {
                //本次发送中配置了忽略配置的用户
                Object o = redisTemplate.opsForHash().get(SatMsgConstant.getTenantRedisKey(
                        SatMsgConstant.USER_MSG_IGNORED_REDIS_KEY, messageRecordInfo.getTenant()), targetInfo.getUserId().toString());
                if (Objects.isNull(o)) {
                    continue;
                }
                SatUserMsgIgnoredConfigDTO satUserMsgIgnoredConfig = (SatUserMsgIgnoredConfigDTO) o;
                //是否忽略本次消息发送
                if (validUserIgnoredTime(sendTime, satUserMsgIgnoredConfig) &&
                    validUserIgnoredChannels(messageRecordInfo.getChannelCode().name(), satUserMsgIgnoredConfig)) {
                    ignoredReceivers.add(targetInfo.getUserId().toString());
                    ignoredReceivers.add(targetInfo.getUserPhone());
                    ignoredReceivers.add(targetInfo.getUserEmail());
                    this.msgRecordService
                            .updateMsgRecordTargetStatus(targetInfo.getId(), Boolean.FALSE, sendTime, USER_RECEIVER_IGNORED);
                    log.warn("msg_receiver ignored,config {}", JsonUtils.serialize(satUserMsgIgnoredConfig));
                }
            }
            if (!CollectionUtils.isEmpty(ignoredReceivers)) {
                MessageInfo messageInfo = messageInfoMap.get(messageRecordInfo.getId());
                messageInfo.getReceiver().removeAll(ignoredReceivers);
            }
        }
        context.setNeedBreak(false);
    }

    /**
     * 验证发送时间是否在用户忽略配置时间内
     *
     * @param sendTime
     * @param satUserMsgIgnoredConfig
     *
     * @return
     */
    private Boolean validUserIgnoredTime(LocalDateTime sendTime, SatUserMsgIgnoredConfigDTO satUserMsgIgnoredConfig) {
        LocalDate startDatePart = sendTime.toLocalDate();
        LocalDateTime ignoredStartTime = LocalDateTime.of(startDatePart, Objects.requireNonNull(JsonUtils.deserialize(satUserMsgIgnoredConfig.getStartTime(), LocalTime.class)));
        LocalDateTime ignoredEndTime = LocalDateTime.of(startDatePart, Objects.requireNonNull(JsonUtils.deserialize(satUserMsgIgnoredConfig.getEndTime(), LocalTime.class)));
        //跨天的情况
        if (Objects.equals(Boolean.TRUE, satUserMsgIgnoredConfig.getTomorrows())) {
            //如果发送时间小于结束时间
            if (!sendTime.isAfter(ignoredEndTime)) {
                ignoredStartTime = ignoredEndTime.plusDays(-1L);
            } else {
                ignoredEndTime = ignoredEndTime.plusDays(1L);
            }
        }
        return !sendTime.isBefore(ignoredStartTime) &&
               !sendTime.isAfter(ignoredEndTime);
    }

    /**
     * 验证发送通道是否在用户忽略通道内
     *
     * @param sendChannel
     * @param satUserMsgIgnoredConfig
     *
     * @return
     */
    private Boolean validUserIgnoredChannels(String sendChannel, SatUserMsgIgnoredConfigDTO satUserMsgIgnoredConfig) {
        if (StringUtils.isBlank(satUserMsgIgnoredConfig.getChannels())) {
            return Boolean.FALSE;
        }
        Set<String> ignoredChannels = Arrays.stream(satUserMsgIgnoredConfig.getChannels().split(",")).collect(Collectors.toSet());
        return ignoredChannels.contains(sendChannel);
    }
}
