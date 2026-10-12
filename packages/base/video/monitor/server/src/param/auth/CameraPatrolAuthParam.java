package com.guhecloud.cube.biz.vmonitor.server.param.auth;

import com.guhecloud.cube.biz.vmonitor.server.enumeration.CameraPatrolAuthTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 巡逻线路权限配置
 *
 * @author lanchun
 * @date 2023/3/22 10:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CameraPatrolAuthParam {

    /**
     * 权限类型
     */
    private CameraPatrolAuthTypeEnum authType;

    /**
     * 权限配置
     */
    private Object authConfig;
}
