package com.guhecloud.cube.base.flow.flowable.common.resovler;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author pengjian
 * @date 2022/6/13 13:54
 * @description 用户与通知渠道的映射
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserNotificationChannelMappingDTO {
    /**
     * 用户表达式
     */
    private Set<String> userIds;
    /**
     * 通知渠道
     */
    private Set<ChannelEnum> channel;
}
