package com.guhecloud.cube.biz.mdata.server.scene.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 区域漫游配置保存参数
 *
 * @author lanchun
 * @date 2022/9/19 15:51
 */
@Data
public class AreaRoamingConfigModifyParam {

    /**
     * 漫游名称
     */
    @NotEmpty(message = "名称不能为空")
    @Size(max = 50, message = "名称不能大于50")
    private String name;

    /**
     * 场景类型
     */
    private Long sceneType;

    /**
     * 所属区域集合
     */
    @NotNull(message = "所属区域不能为空")
    @Size(min = 1, message = "所属区域不能为空")
    private List<String> areaIds;

    /**
     * 漫游配置
     */
    private String roamingConfig;
}
