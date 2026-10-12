package com.guhecloud.cube.biz.linkage.client.param.before;

import com.alibaba.fastjson2.annotation.JSONType;
import com.guhecloud.apps.linkage.metadata.enumeration.ActionBeforeTypeEnum;
import com.guhecloud.cube.biz.linkage.client.param.before.validator.AlarmJudgmentConfigSequenceProvider;
import com.guhecloud.cube.biz.linkage.client.param.before.validator.AlarmJudgmentSequenceProvider;
import com.guhecloud.cube.biz.linkage.client.validator.provider.WhenEnabledTrueGroup;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.group.GroupSequenceProvider;

@EqualsAndHashCode(callSuper = true)
@Data
@JSONType(typeName = "ALARM_JUDGMENT")
@NoArgsConstructor
@GroupSequenceProvider(AlarmJudgmentSequenceProvider.class)
public class AlarmJudgmentModifyParam extends ActionBeforeParam {

    @NotNull(message = "报警研判配置不能为空", groups = WhenEnabledTrueGroup.class)
    @Valid
    private JudgmentConfig actionBeforeConfig;

    @Builder(toBuilder = true)
    public AlarmJudgmentModifyParam(ActionBeforeTypeEnum actionBeforeType, Boolean enabled,
                                    JudgmentConfig actionBeforeConfig
                                   ) {
        super(actionBeforeType, enabled);
        this.actionBeforeConfig = actionBeforeConfig;
    }

    @Data
    @GroupSequenceProvider(AlarmJudgmentConfigSequenceProvider.class)
    public static class JudgmentConfig {

        /**
         * 研判结果：CONFIRM_ALARM-确认报警，SUSPECTED_ALARM-疑似报警
         */
        private String confirmedAlarm;

        /**
         * 是否系统处理 仅当confirmedAlarm = SUSPECTED_ALARM-疑似报警 时生效
         */
        private Boolean systemProcess;

        /**
         * 处理状态  仅当confirmedAlarm = SUSPECTED_ALARM-疑似报警 时生效
         */
        private String handlerStatus;

        /**
         * 研判时限（秒），仅当 enabled=true 时生效
         */
        private Integer judgmentTimeoutSeconds;
    }
}
