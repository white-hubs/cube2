package com.guhecloud.apps.linkage.metadata.dto;

import com.guhecloud.apps.linkage.metadata.enumeration.ActionBeforeTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkageActionBeforeDTO {

    private Long id;

    /**
     * 联动预案主键
     */
    private Long linkageId;

    /**
     * 动作类型
     */
    private ActionBeforeTypeEnum actionBeforeType;

    /**
     * json配置
     */
    private Object actionBeforeConfig;

    /**
     * 启用状态
     */
    private Boolean enabled;
}
