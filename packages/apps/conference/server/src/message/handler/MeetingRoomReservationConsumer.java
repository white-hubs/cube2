package com.guhecloud.cube.srv.gen.conferencemanage.server.message.handler;

import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.BookRoom;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.BookRoomService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/7 17:05
 * @Description: 流程审批完成消费
 */

@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = ProcessCompletedMessage.TOPIC + "_CONFERENCE_GROUP",
        topic = ProcessCompletedMessage.TOPIC, selectorExpression = "MEETING_ROOM_RESERVATION"
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MeetingRoomReservationConsumer implements RocketMQListener<ProcessCompletedMessage> {
    private final BookRoomService bookRoomService;

    @Override
    public void onMessage(ProcessCompletedMessage message) {

        try {
            SessionContextHolder.requestTenant(message.getTenant());
            if (FlowResult.PASS.equals(message.getResult())) {
                bookRoomService.updateBookRoom(message.getProcessInstanceId());
            } else {
                BookRoom bookRoom = bookRoomService.lambdaQuery()
                        .eq(BookRoom::getProcessInsId, message.getProcessInstanceId())
                        .last("limit 1").one();
                if (Objects.isNull(bookRoom)) {
                    return;
                }
                bookRoomService.cancelBookRoom(bookRoom.getId(), true, this.getCancelReason(message.getResult()));
            }
        } catch (Exception e) {
            log.error("会议室预约审批事件失败，失败原因：", e);
        }
    }

    private String getCancelReason(FlowResult result) {
        return switch (result) {
            case REJECT -> "审批不通过";
            case UNDO -> "流程被撤销";
            default -> null;
        };
    }
}
