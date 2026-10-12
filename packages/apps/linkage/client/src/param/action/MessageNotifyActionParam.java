package com.guhecloud.cube.biz.linkage.client.param.action;

import com.alibaba.fastjson2.annotation.JSONType;
import com.guhecloud.apps.linkage.metadata.enumeration.DateUnitEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.MessageNotifyTypeEnum;
import com.guhecloud.cube.biz.linkage.client.param.action.param.MessageTargetNotifyParam;
import com.guhecloud.cube.biz.linkage.client.param.action.validator.MessageNotifyActionSequenceProvider;
import com.guhecloud.cube.biz.linkage.client.param.action.validator.MessageNotifyConfigSequenceProvider;
import com.guhecloud.cube.biz.linkage.client.validator.provider.WhenEnabledTrueGroup;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.group.GroupSequenceProvider;

import java.util.List;

@Data
@JSONType(typeName = "MESSAGE_NOTIFY_ACTION")
@NoArgsConstructor
@GroupSequenceProvider(MessageNotifyActionSequenceProvider.class)
public class MessageNotifyActionParam extends ActionParam {

    /**
     * 消息通知动作配置
     */
    @Valid
    @NotEmpty(message = "消息通知动作配置不能为空", groups = WhenEnabledTrueGroup.class)
    private List<LinkageMessageParam> actionConfig;

    public MessageNotifyActionParam(LinkageActionTypeEnum actionType, Boolean enabled, List<LinkageMessageParam> actionConfig) {
        super(actionType, enabled);
        this.actionConfig = actionConfig;
    }

    @Data
    @GroupSequenceProvider(MessageNotifyConfigSequenceProvider.class)
    public static class LinkageMessageParam {
        /**
         * 通知时间类型
         */
        @NotNull(message = "通知时间点不能为空", groups = WhenEnabledTrueGroup.class)
        private MessageNotifyTypeEnum notifyType;

        /**
         * 延迟时间
         */
        private Integer duration;

        /**
         * 延迟时间单位
         */
        private DateUnitEnum delayTimeUnit;

        /**
         * 通知通道配置
         */
        @NotNull(message = "通知方式不能为空", groups = WhenEnabledTrueGroup.class)
        @Size(min = 1, message = "通知方式不能为空", groups = WhenEnabledTrueGroup.class)
        private List<String> channelTypes;

        /**
         * 通知对象配置
         */
        @NotNull(message = "通知对象不能为空", groups = WhenEnabledTrueGroup.class)
        @Size(min = 1, message = "通知对象不能小于1", groups = WhenEnabledTrueGroup.class)
        @Valid
        private List<MessageTargetNotifyParam> targetModifyParams;
    }
}
