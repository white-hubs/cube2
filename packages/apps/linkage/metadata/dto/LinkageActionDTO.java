package com.guhecloud.apps.linkage.metadata.dto;

import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;
import lombok.Data;

/**
 * 联动动作返回参数
 *
 * @author lanchun
 * @date 2022/6/21 20:02
 */
@Data
public class LinkageActionDTO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 动作类型
     */
    private LinkageActionTypeEnum actionType;

    /**
     * 启用状态
     */
    private Boolean enabled;

    /**
     * 动作json
     */
    private Object actionConfig;
}
