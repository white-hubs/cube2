package com.guhecloud.cube.biz.vmonitor.server.vo;

import com.guhecloud.cube.biz.vmonitor.server.enumeration.CameraPatrolAuthTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 视频巡逻人员配置返回参数
 *
 * @author lanchun
 * @date 2022/8/4 14:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CameraPatrolPersonVO {

    private Long id;

    /**
     * 巡逻路线主键
     */
    private Long cameraPatrolLineId;

    /**
     * 权限类型
     */
    private CameraPatrolAuthTypeEnum authType;

    /**
     * 权限配置
     */
    private Object authConfig;
}
