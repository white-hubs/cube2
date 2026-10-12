package com.guhecloud.cube.biz.vmonitor.server.param;

import com.guhecloud.cube.biz.vmonitor.server.param.auth.CameraPatrolAuthParam;
import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 视频巡逻路线保存参数
 *
 * @author lanchun
 * @date 2021/12/6 19:48
 */
@Data
public class CameraPatrolLineModifyParam {

    /**
     * 名称
     */
    @NotEmpty(message = "名称不能为空")
    @Size(max = 100, message = "名称不能大于100个字符")
    private String name;

    /**
     * 巡逻建议时间
     */
    @NotEmpty(message = "巡逻建议时间不能为空")
    @Size(max = 50, message = "巡逻建议时间不能大于50个字符")
    private String patrolProposalTime;

    /**
     * 是否启用
     */
    private Boolean enabled = true;

    /**
     * 巡逻人员配置
     */
    @Valid
    @NotNull(message = "巡逻人员配置不能为空")
    @Size(min = 1, message = "巡逻人员配置不能小于1")
    private List<CameraPatrolAuthParam> personModifyParams;

    /**
     * 视频巡逻点配置
     */
    @Valid
    @NotNull(message = "视频巡逻点不能为空")
    @Size(min = 1, message = "视频巡逻点不能小于1")
    private List<CameraPatrolDeviceModifyParam> deviceModifyParams;
}