package com.guhecloud.cube.biz.vmonitor.server.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 视频巡逻记录查询参数
 *
 * @author lanchun
 * @date 2021/12/6 19:49
 */
@Data
public class CameraPatrolRecordQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 时间搜索
     */
    private Period period;

    /**
     * 巡逻线路主键
     */
    private Long cameraPatrolLineId;
}