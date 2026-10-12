package com.guhecloud.cube.srv.gen.maintain.server.common.util;

import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author wql
 * @describe 消息2.0发送处理工具
 * @date 2023/9/5 14:29
 */
public class MessageSendUtil {

    public static List<MessageNotifyTarget> getNotifyUserByIds(Set<Long> userIds) {
        List<MessageNotifyTarget> notifyTargets = new ArrayList<>();
        MessageNotifyTarget notifyTarget = new MessageNotifyTarget();
        notifyTarget.setNotifyTargetType(MessageNotifyTargetEnum.USER);
        notifyTarget.setIds(userIds);
        notifyTargets.add(notifyTarget);
        return notifyTargets;
    }
}

