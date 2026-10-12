package com.guhecloud.cube.biz.mdata.server.scene.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 区域场景配置保存参数
 *
 * @author lanchun
 * @date 2021/12/2 14:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AreaSceneConfigModifyParam {

    /**
     * 区域主键
     */
    @NotNull(message = "区域主键不能为空")
    @Min(value = 1, message = "区域主键不能小于1")
    private Long areaId;

    /**
     * 所属项目id
     */
    private Long projectId;

    /**
     * 场景类型
     */
    @NotNull(message = "场景类型不能为空")
    @Min(value = 1, message = "场景类型不能小于1")
    private Long sceneType;

    /**
     * 场景类型序列号
     */
    @NotEmpty(message = "场景类型序列号不能为空")
    @Size(max = 200, message = "场景类型序列号不能大于200")
    private String sceneTypeSequence;

    /**
     * 模型视角绑定主键
     */
    private List<String> bimViewBinds;
}
