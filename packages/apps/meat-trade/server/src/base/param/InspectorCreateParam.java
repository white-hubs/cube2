package com.guhecloud.cube.biz.meattrade.server.base.param;

import com.guhecloud.cube.biz.meattrade.server.base.enumeration.InspectorEnum;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 抽检员新增信息
 */
@Data
public class InspectorCreateParam {

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 抽检员名称
     */
    @NotEmpty(message = "抽检员名称不能为空")
    @Size(max = 100, message = "抽检员名称不能大于100")
    private String name;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 抽检员类型
     */
    @NotNull(message = "抽检员类型不能为空")
    private InspectorEnum type;

    /**
     * 备注
     */
    @Size(max = 200, message = "备注不能大于200")
    private String remark;

}
