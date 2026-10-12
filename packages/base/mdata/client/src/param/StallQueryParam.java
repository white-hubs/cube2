package com.guhecloud.cube.biz.mdata.client.param;

import com.guhecloud.platform.base.model.Paging;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * 档位查询信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StallQueryParam extends Paging {
    private Set<Long> ids;
    /**
     * 关键字（档位编号/档位别名）
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 区域主键
     */
    @Min(value = 1, message = "区域主键不能小于1")
    private Long areaId;

    /**
     * 档位类型
     */
    @Size(max = 100, message = "档位类型不能大于100")
    private String type;

    /**
     * 出租类型
     */
    private Boolean leaseType;

    /**
     * 档位状态
     */
    private Boolean enabled;

    /**
     * 部门id
     */
    private Long departmentId;

}
