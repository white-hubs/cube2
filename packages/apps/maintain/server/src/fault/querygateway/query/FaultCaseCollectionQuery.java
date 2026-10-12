package com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.query;

import com.guhecloud.cube.srv.gen.maintain.server.fault.enumeration.FaultResultEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

/**
 * @author lzy
 * @date 2022/5/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FaultCaseCollectionQuery extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 故障类型主键
     */
    private Long faultTypeId;

    /**
     * 设备类型主键
     */
    private Long deviceTypeId;

    /**
     * 品类code
     */
    private String productCategoryCode;

    /**
     * 故障结果枚举
     */
    private FaultResultEnum result;

    /**
     * 时间
     */
    private Period period;
}
