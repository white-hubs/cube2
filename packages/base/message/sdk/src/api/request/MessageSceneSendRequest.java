package com.guhecloud.cube.base.message.sdk.api.request;

import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.common.annotation.Validation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MessageSceneSendRequest extends BasicRequest {

    /**
     * 场景编号
     */
    @Validation(required = true, maxLength = 50, minLength = 1)
    private String msgSceneCode;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 业务主键
     */
    @Size(max = 50, message = "业务主键不能大于50")
    private String businessId;

    /**
     * 推送的业务主体
     */
    private Object businessPrincipal;

    /**
     * 模板参数
     */
    @Validation(required = true)
    private Map<String, String> tmpParams;

    /**
     * 发送对象信息参数
     */
    private List<MessageNotifyTarget> messageNotifyTargets;

    /**
     * 租户编号
     */
    @Validation(required = true, maxLength = 10, minLength = 1)
    private Integer tenant;
    /**
     * 来源租户
     */
    private Integer sourceTenant;
}
