package com.guhecloud.cube.biz.adapter.broadcast.server.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * 文本播放参数
 *
 * @author lanchun
 * @date 2022/12/8 20:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DevicePlayTextParam {

    /**
     * 播放内容
     */
    @NotBlank(message = "播放内容不能为空")
    private String playContent;

    /**
     * 设备主键集合
     */
    @NotNull(message = "设备不能为空")
    @Size(min = 1, message = "设备不能为空")
    private Set<String> deviceIds;

    /**
     * 播放次数
     */
    @NotNull(message = "播放次数不能为空")
    @Min(value = 1, message = "播放次数不能小于1")
    private Integer playTime;

    /**
     * 播放间隔
     */
    private Integer playInterval;

    /**
     * 播放优先级
     */
    @NotNull(message = "播放优先级不能为空")
    @Min(value = 1, message = "播放优先级不能小于1")
    private Integer playOrder;
}
