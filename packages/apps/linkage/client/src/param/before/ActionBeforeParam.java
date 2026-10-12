package com.guhecloud.cube.biz.linkage.client.param.before;

import com.alibaba.fastjson2.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.guhecloud.apps.linkage.metadata.enumeration.ActionBeforeTypeEnum;
import com.guhecloud.cube.biz.linkage.client.validator.annotation.GroupId;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JSONType(seeAlso = { AlarmJudgmentModifyParam.class })
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "actionBeforeType",
        visible = true
)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = AlarmJudgmentModifyParam.class, name = "ALARM_JUDGMENT")
        }
)
@NoArgsConstructor
@AllArgsConstructor
public class ActionBeforeParam {

    /**
     * 前置条件类型
     */
    @NotNull(message = "动作前置条件")
    protected ActionBeforeTypeEnum actionBeforeType;

    /**
     * 启用状态
     */
    @NotNull(message = "前置条件启用状态不能为空")
    @GroupId
    protected Boolean enabled;
}
