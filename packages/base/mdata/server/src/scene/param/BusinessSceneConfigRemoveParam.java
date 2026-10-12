package com.guhecloud.cube.biz.mdata.server.scene.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * 设施场景绑定保存参数
 *
 * @author lanchun
 * @date 2021/12/2 14:21
 */
@Data
public class BusinessSceneConfigRemoveParam {

    /**
     * 所属项目id
     */
    @NotNull(message = "所属项目不能为空")
    private Long projectId;

    /**
     * 业务主键
     */
    @NotNull(message = "档位主键不能为空")
    @Size(min = 1, message = "档位主键集合长度不能小于1")
    private Set<Long> businessIds;
}
