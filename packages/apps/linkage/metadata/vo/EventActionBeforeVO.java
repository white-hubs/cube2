package com.guhecloud.apps.linkage.metadata.vo;

import com.guhecloud.apps.linkage.metadata.enumeration.ActionBeforeTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventActionBeforeVO {

    private Long id;

    /**
     * 事件主键
     */
    private Long eventId;

    /**
     * 联动预案主键
     */
    private Long linkageId;

    /**
     * 事件关联预案主键
     */
    private Long eventLinkageId;

    /**
     * 类型
     */
    private ActionBeforeTypeEnum actionBeforeType;

    /**
     * json配置
     */
    private String actionBeforeConfig;
}
