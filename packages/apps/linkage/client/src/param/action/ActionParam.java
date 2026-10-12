package com.guhecloud.cube.biz.linkage.client.param.action;

import com.alibaba.fastjson2.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;
import com.guhecloud.cube.biz.linkage.client.validator.annotation.GroupId;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JSONType(seeAlso = { AlarmNotifyActionParam.class, DeviceCtlActionParam.class, MessageNotifyActionParam.class, VideoActionParam.class })
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "actionType",
        visible = true
)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = AlarmNotifyActionParam.class, name = "ALARM_NOTIFY_ACTION"),
                @JsonSubTypes.Type(value = DeviceCtlActionParam.class, name = "IOT_DEVICE_ACTION"),
                @JsonSubTypes.Type(value = MessageNotifyActionParam.class, name = "MESSAGE_NOTIFY_ACTION"),
                @JsonSubTypes.Type(value = VideoActionParam.class, name = "VIDEO_DEVICE_ACTION")
        }
)
@NoArgsConstructor
@AllArgsConstructor
public abstract class ActionParam {

    /**
     * 动作类型
     */
    @NotNull(message = "动作类型不能为空")
    protected LinkageActionTypeEnum actionType;

    /**
     * 启用状态
     */
    @NotNull(message = "动作启用状态不能为空")
    @GroupId
    protected Boolean enabled;
}
