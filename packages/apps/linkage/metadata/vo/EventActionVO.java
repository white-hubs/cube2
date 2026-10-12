package com.guhecloud.apps.linkage.metadata.vo;

import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteStatusEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 事件动作数据模型
 *
 * @author lanchun
 * @date 2022/6/21 13:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventActionVO {

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
     * 执行状态
     */
    private ExecuteStatusEnum executeStatus;

    /**
     * 动作json
     */
    private String actionConfig;
}
