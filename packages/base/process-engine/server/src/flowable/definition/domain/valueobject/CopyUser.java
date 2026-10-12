package com.guhecloud.cube.base.flow.flowable.definition.domain.valueobject;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author pengjian
 * @date 2022/9/1 21:12
 * @description
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CopyUser {
    private Long userId;
    private Set<ChannelEnum> channelTypes;
}
