package com.guhecloud.cube.biz.vmonitor.server.param;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 多路视频保存参数
 *
 * @author lanchun
 * @date 2021/12/6 19:50
 */
@Data
public class MultipleVideoCollectModifyParam {

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    @Size(max = 100, message = "名称不能大于100")
    private String name;

    /**
     * 行数
     */
    @NotNull(message = "行数不能为空")
    @Min(value = 1, message = "行数不能小于1")
    private Integer row;

    /**
     * 列数
     */
    @NotNull(message = "列数不能为空")
    @Min(value = 1, message = "行数不能小于1")
    private Integer col;

    /**
     * 摄像机列表
     */
    @Valid
    @NotNull(message = "摄像机列表不能为空")
    @Size(min = 1, message = "摄像机列表不能小于1")
    private List<MultipleVideoCollectCameraModifyParam> cameraParams;
}