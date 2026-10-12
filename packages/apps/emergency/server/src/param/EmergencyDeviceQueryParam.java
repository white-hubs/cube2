package com.guhecloud.cube.biz.emergency.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * 预案设备查询参数
 *
 * @author lanchun
 * @date 2022/12/1 15:04
 */
@Data
public class EmergencyDeviceQueryParam extends Paging {

    /**
     * 设备型号主键集合
     */
    private List<Long> modelIds;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 区域id(多级数组）
     */
    private List<String> areaIds;

    /**
     * 应用编号
     */
    @NotEmpty(message = "应用编号不能为空")
    private String applicationCode;
}
