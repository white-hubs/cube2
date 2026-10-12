package com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message;

import com.guhecloud.cube.base.message.sdk.SiteMessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.request.SiteBroadcastSendRequest;
import com.guhecloud.cube.base.message.sdk.api.request.SiteSendRequest;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message.dto.CloseConferenceRoomMessage;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message.dto.InviteUserMessage;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message.dto.JoinConferenceRoomMessage;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message.dto.LeaveConferenceRoomMessage;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message.dto.ProposeConferenceRoomMessage;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message.dto.RefuseJoinConferenceRoomMessage;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message.dto.RemoveConferenceRoomUserMessage;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.message.dto.UserOtherTerminalJoinRoomMessage;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
public class RoomEventMessageSender {

    private final SiteMessageSendClient siteMessageSendClient;

    private static final String CONFERENCE_ROOM_MSG_TYPE = "COMMUNICATE_MESSAGE";

    /**
     * 用户加入会议房间
     */
    private static final String USER_JOIN_CONFERENCE_ROOM = "USER_JOIN_CONFERENCE_ROOM";

    /**
     * 邀请用户加入会议房间
     */
    private static final String INVITE_USER_JOIN_CONFERENCE_ROOM = "INVITE_USER_JOIN_CONFERENCE_ROOM";

    /**
     * 用户拒绝加入房间邀请
     */
    private static final String USER_REFUSE_JOIN_CONFERENCE_ROOM = "USER_REFUSE_JOIN_CONFERENCE_ROOM";

    /**
     * 用户离开会议房间
     */
    private static final String USER_LEAVE_CONFERENCE_ROOM = "USER_LEAVE_CONFERENCE_ROOM";

    /**
     * 踢出房间用户消息
     */
    private static final String REMOVE_CONFERENCE_ROOM_USER = "REMOVE_CONFERENCE_ROOM_USER";

    /**
     * 关闭会议房间消息
     */
    private static final String CLOSE_CONFERENCE_ROOM = "CLOSE_CONFERENCE_ROOM";

    /**
     * 发起会议消息
     */
    private static final String PROPOSE_CONFERENCE_ROOM = "PROPOSE_CONFERENCE_ROOM";

    /**
     * 用户其它端已加入消息
     */
    private static final String USER_OTHER_TERMINAL_JOIN = "USER_OTHER_TERMINAL_JOIN";

    public RoomEventMessageSender(SiteMessageSendClient siteMessageSendClient) {
        this.siteMessageSendClient = siteMessageSendClient;
    }

    /**
     * 加入会议房间消息发送
     *
     * @param message   消息内容
     * @param userIds   发送用户
     * @param terminals 发送终端
     */
    public void sendJoinConferenceRoomMsg(JoinConferenceRoomMessage message,
                                          Set<Long> userIds,
                                          Set<String> terminals
                                         ) {
        this.send(message, USER_JOIN_CONFERENCE_ROOM, userIds, terminals);
    }

    /**
     * 邀请用户加入房间消息
     *
     * @param message   消息内容
     * @param userIds   发送用户
     * @param terminals 发送终端
     */
    public void sendInviteUserMsg(InviteUserMessage message,
                                  Set<Long> userIds,
                                  Set<String> terminals
                                 ) {
        this.send(message, INVITE_USER_JOIN_CONFERENCE_ROOM, userIds, terminals);
    }

    /**
     * 离开会议房间消息发送
     *
     * @param message   消息内容
     * @param userIds   发送用户
     * @param terminals 发送终端
     */
    public void sendLeaveConferenceRoomMessage(LeaveConferenceRoomMessage message,
                                               Set<Long> userIds,
                                               Set<String> terminals
                                              ) {
        this.send(message, USER_LEAVE_CONFERENCE_ROOM, userIds, terminals);
    }

    /**
     * 踢出会议房间用户消息
     *
     * @param message   消息内容
     * @param userIds   发送用户
     * @param terminals 发送终端
     */
    public void sendRemoveConferenceRoomUserMessage(RemoveConferenceRoomUserMessage message,
                                                    Set<Long> userIds,
                                                    Set<String> terminals
                                                   ) {
        this.send(message, REMOVE_CONFERENCE_ROOM_USER, userIds, terminals);
    }

    /**
     * 发起会议房间消息发送
     *
     * @param message   消息内容
     * @param userIds   发送用户
     * @param terminals 发送终端
     */
    public void sendProposeConferenceRoomMsg(ProposeConferenceRoomMessage message,
                                             Set<Long> userIds,
                                             Set<String> terminals
                                            ) {
        this.send(message, PROPOSE_CONFERENCE_ROOM, userIds, terminals);
    }

    /**
     * 关闭会议房间消息发送
     *
     * @param message   消息内容
     * @param userIds   发送用户
     * @param terminals 发送终端
     */
    public void sendCloseConferenceRoomMsg(CloseConferenceRoomMessage message,
                                           Set<Long> userIds,
                                           Set<String> terminals
                                          ) {
        this.send(message, CLOSE_CONFERENCE_ROOM, userIds, terminals);
    }

    /**
     * 用户拒绝加入房间邀请
     *
     * @param message   消息内容
     * @param userIds   发送用户
     * @param terminals 发送终端
     */
    public void sendRefuseJoinConferenceRoomMsg(RefuseJoinConferenceRoomMessage message,
                                                Set<Long> userIds,
                                                Set<String> terminals
                                               ) {
        this.send(message, USER_REFUSE_JOIN_CONFERENCE_ROOM, userIds, terminals);
    }

    /**
     * 用户其它端已加入房间消息
     *
     * @param message   消息内容
     * @param userIds   发送用户
     * @param terminals 发送终端
     */
    public void sendUserOtherTerminalJoinMsg(UserOtherTerminalJoinRoomMessage message,
                                             Set<Long> userIds,
                                             Set<String> terminals
                                            ) {
        this.send(message, USER_OTHER_TERMINAL_JOIN, userIds, terminals);
    }

    private void send(Object msgContent,
                      String eventType,
                      Set<Long> userIds,
                      Set<String> terminals
                     ) {
        if (CollectionUtils.isEmpty(userIds) ||
            CollectionUtils.isEmpty(terminals)) {
            return;
        }
        SiteSendRequest request = SiteSendRequest.builder()
                .msgType(eventType)
                .content(JsonUtils.serialize(msgContent))
                .eventType(CONFERENCE_ROOM_MSG_TYPE)
                .tenant(SessionContextHolder.getRequester().getTenant())
                .userIds(userIds)
                .terminals(terminals)
                .build();
        this.siteMessageSendClient.send(request);
        SiteBroadcastSendRequest broadcastRequest = SiteBroadcastSendRequest.builder()
                .content(JsonUtils.serialize(msgContent))
                .msgType(CONFERENCE_ROOM_MSG_TYPE)
                .eventType(eventType)
                .tenant(SessionContextHolder.getRequester().getTenant())
                .userIds(userIds)
                .terminals(terminals)
                .build();
        this.siteMessageSendClient.customerBroadcastSend(broadcastRequest);
    }
}
