package com.guhecloud.cube.biz.vmonitor.server.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class CameraPatrolRecordDeviceRemarkQueryParam extends Paging {

    /**
     * 巡逻记录主键
     */
    private Long cameraPatrolRecordId;

    /**
     * 时间条件
     */
    private Period period;
}
