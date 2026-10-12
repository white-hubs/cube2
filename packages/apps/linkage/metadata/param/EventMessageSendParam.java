package com.guhecloud.apps.linkage.metadata.param;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class EventMessageSendParam {

    /**
     * 事件id
     */
    @NotNull(message = "事件id不能为空")
    private Long eventId;

    /**
     * 业务实体
     */
    private Object businessPrincipal;

    @Valid
    @NotNull(message = "通知配置不能为空")
    @Size(min = 1, message = "通知配置不能小于1")
    private List<MessageNotifyParam> messageNotifies;
}
