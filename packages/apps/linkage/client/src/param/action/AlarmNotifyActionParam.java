package com.guhecloud.cube.biz.linkage.client.param.action;

import com.alibaba.fastjson2.annotation.JSONType;
import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;
import com.guhecloud.cube.biz.linkage.client.param.action.param.MessageTargetNotifyParam;
import com.guhecloud.cube.biz.linkage.client.param.action.validator.AlarmNotifyActionSequenceProvider;
import com.guhecloud.cube.biz.linkage.client.param.action.validator.AlarmNotifyConfigSequenceProvider;
import com.guhecloud.cube.biz.linkage.client.validator.provider.WhenEnabledTrueGroup;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.group.GroupSequenceProvider;

import java.util.List;

@Data
@JSONType(typeName = "ALARM_NOTIFY_ACTION")
@NoArgsConstructor
@GroupSequenceProvider(AlarmNotifyActionSequenceProvider.class)
public class AlarmNotifyActionParam extends ActionParam {

    /**
     * 报警配置
     */
    @NotNull(message = "报警配置不能为空", groups = WhenEnabledTrueGroup.class)
    @Valid
    private AlarmNotifyConfigParam actionConfig;

    @Builder(toBuilder = true)
    public AlarmNotifyActionParam(LinkageActionTypeEnum actionType, Boolean enabled, AlarmNotifyConfigParam actionConfig) {
        super(actionType, enabled);
        this.actionConfig = actionConfig;
    }

    @Data
    @GroupSequenceProvider(AlarmNotifyConfigSequenceProvider.class)
    public static class AlarmNotifyConfigParam {

        /**
         * 是否仅指派给当班人员
         */
        @NotNull(message = "是否仅指派给当班人员不能为空", groups = WhenEnabledTrueGroup.class)
        private Boolean dutyUser;

        /**
         * 通知配置
         */
        @NotEmpty(message = "请选择通知人员", groups = WhenEnabledTrueGroup.class)
        @Valid
        private List<MessageTargetNotifyParam> targetModifyParams;

        /**
         * 通知通道配置
         */
        private List<String> channelTypes;
    }
}
