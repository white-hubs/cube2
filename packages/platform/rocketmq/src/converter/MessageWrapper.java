package com.guhecloud.platform.rocketmq.converter;

import com.guhecloud.platform.base.model.Requester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MessageWrapper {

    /**
     * 目标对象
     */
    private Object payload;

    /**
     * 请求对象
     */
    private Requester requester;

}
