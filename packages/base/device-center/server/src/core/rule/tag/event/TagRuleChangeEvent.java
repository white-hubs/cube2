package com.guhecloud.cube.base.device.center.core.rule.tag.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 标签规则变更事件
 *
 * @author lanchun
 * @date 2025/4/9 14:03
 */
@Getter
public class TagRuleChangeEvent extends ApplicationEvent {

    private final String tagCode;

    public TagRuleChangeEvent(Object source, String tagCode) {
        super(source);
        this.tagCode = tagCode;
    }
}
