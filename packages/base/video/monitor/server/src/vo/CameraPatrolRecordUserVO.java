package com.guhecloud.cube.biz.vmonitor.server.vo;

import lombok.Data;

import java.util.List;

/**
 * 巡逻记录人返回参数
 *
 * @author lanchun
 * @date 2022/8/5 10:19
 */
@Data
public class CameraPatrolRecordUserVO {

    /**
     * 线路主键
     */
    private Long cameraPatrolLineId;

    /**
     * 用户名称
     */
    private List<String> userNames;
}
