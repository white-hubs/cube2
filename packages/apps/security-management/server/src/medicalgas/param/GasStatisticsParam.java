package com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.Size;

import java.util.List;

@Data
public class GasStatisticsParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    private String type;

    /**
     * 场景类型
     */
    private Long sceneType;

    /**
     * 区域主键
     */
    private Long areaId;

    /**
     * 时间范围
     */
    private Period period;

    /**
     * 设备主键集合
     */
    private List<Long> deviceIds;

    private List<String> thirdDeviceIds;

    private String timeFormat;

}
