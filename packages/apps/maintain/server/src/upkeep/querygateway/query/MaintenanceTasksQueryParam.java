package com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lrj
 * @Description 维保任务
 * @date 2022-4-29 15:12:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MaintenanceTasksQueryParam extends Paging {

    /**
     * 对象名称
     */
    private String objectName;
    /**
     * 任务状态
     */
    private String maintenanceTaskStatus;

    /**
     * 维保发布开始时间
     */
    private String releaseStartTime;
    /**
     * 维保发布结束时间
     */
    private String releaseEndTime;

    /**
     * 维保完成开始时间
     */
    private String finishStartTime;

    /**
     * 维保完成开始时间
     */
    private String finishEndTime;

    /**
     * 品类编码
     */
    private String categoryCode;

    /**
     * 时间
     */
    private String period;

    private String userId;

    private String organization;

    private String position;
    //扫码主键匹配任务
    private String keyId;

}