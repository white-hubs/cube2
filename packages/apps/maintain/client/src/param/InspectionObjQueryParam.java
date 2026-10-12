package com.guhecloud.cube.srv.gen.maintain.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/21 16:46
 * @Description:
 */
@Data
public class InspectionObjQueryParam extends Paging {

    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 查询时间
     */
    private Period period;
    /**
     * 对象类型
     */
    private String objType;
    /**
     * 区域
     */
    private String area;
    /**
     * 对象名称
     */
    private String objName;
}