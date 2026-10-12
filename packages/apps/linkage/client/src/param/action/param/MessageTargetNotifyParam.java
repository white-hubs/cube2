package com.guhecloud.cube.biz.linkage.client.param.action.param;

import com.guhecloud.apps.linkage.metadata.enumeration.MessageTargetNotifyEnum;
import com.guhecloud.cube.biz.linkage.client.param.action.validator.MessageTargetNotifyConfigSequenceProvider;
import com.guhecloud.cube.biz.linkage.client.validator.provider.WhenEnabledTrueGroup;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.group.GroupSequenceProvider;

/**
 * 通知配置
 *
 * @author lanchun
 * @date 2022/6/21 17:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@GroupSequenceProvider(MessageTargetNotifyConfigSequenceProvider.class)
public class MessageTargetNotifyParam {

    /**
     * 消息触达对象类型
     */
    @NotNull(message = "通知人员类型不能为空", groups = WhenEnabledTrueGroup.class)
    private MessageTargetNotifyEnum messageTargetNotify;

    /**
     * 触发对象配置
     */
    @NotNull(message = "通知人员配置不能为空", groups = WhenEnabledTrueGroup.class)
    private Object targetConfig;
}
