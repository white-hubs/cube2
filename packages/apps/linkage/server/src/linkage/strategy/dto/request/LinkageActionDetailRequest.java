package com.guhecloud.cube.biz.linkage.server.linkage.strategy.dto.request;

import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkageActionDetailRequest {

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
     * 动作类型
     */
    private LinkageActionTypeEnum actionType;

    /**
     * 动作json
     */
    private Object actionConfig;
}
