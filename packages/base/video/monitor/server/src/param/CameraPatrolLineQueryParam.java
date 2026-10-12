package com.guhecloud.cube.biz.vmonitor.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 视频巡逻路线查询参数
 *
 * @author lanchun
 * @date 2021/12/6 19:48
 */
@Data
public class CameraPatrolLineQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 权限主键集合
     */
    private List<String> authIds;

    /**
     * 启用状态
     */
    private Boolean enabled;
}