package com.guhecloud.cube.base.bracing.dto;

import com.guhecloud.cube.base.bracing.dto.request.AssemblyNoticeObjDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * @author wql
 * @describe 场景配置
 * @date 2023/9/4 10:35
 */
@Data
public class SceneConfigDTO {
    @NotBlank(message = "label不能为空")
    private String label;
    /**
     * 消息场景编号
     */
    @NotBlank(message = "场景code为空")
    private String sceneCode;
    /**
     * 通知时间
     */
    @NotBlank(message = "通知时间不能为空")
    private Integer value;

    /**
     * 配置参数
     */
    private String config;
    /**
     * 通知方式
     */
    List<AssemblyNoticeWayDTO> noticeWays;
    /**
     * 通知对象
     */
    List<AssemblyNoticeObjDTO> noticeObjs;
}
