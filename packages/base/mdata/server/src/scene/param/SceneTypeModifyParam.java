package com.guhecloud.cube.biz.mdata.server.scene.param;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 场景类型信息
 *
 * @author lanchun
 * @date 2021/12/2 17:42
 */
@Data
public class SceneTypeModifyParam {

    /**
     * 所属项目id
     */
    @NotNull(message = "所属项目不能为空")
    private Long projectId;

    /**
     * 场景类型名称
     */
    @NotEmpty(message = "名称不能为空")
    @Size(max = 100, message = "名称不能大于100")
    private String name;

    /**
     * 父类主键
     */
    private Long parentId;

    /**
     * 场景类型描述
     */
    @Size(max = 100, message = "描述不能大于100")
    private String remark;

    /**
     * 场景序号
     */
    @Min(value = 1, message = "序号不能小于1")
    private Integer sort;

    /**
     * 文件主键
     */
    @Min(value = 1, message = "文件主键不能小于1")
    private Long fileId;

    //场景类型
    private String scenarioType;
}
