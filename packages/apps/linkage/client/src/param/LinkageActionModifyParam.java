package com.guhecloud.cube.biz.linkage.client.param;

import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 联动动作新增参数
 *
 * @author lanchun
 * @date 2022/6/21 20:02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkageActionModifyParam {

    /**
     * 动作类型
     */
    @NotNull(message = "动作类型不能为空")
    private LinkageActionTypeEnum actionType;

    /**
     * 动作json
     */
    private Object actionConfig;

    /**
     * 启用状态
     */
    private Boolean enabled;
}
