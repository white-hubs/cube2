package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class InspectRepairQueryParam extends Paging {
    /**
     * 记录id
     */
    private Long recordId;
    /**
     * 修复状态
     */
    private String repairStatus;
    /**
     * 对象名
     */
    private String targetName;
    /**
     * 临时巡检(true临时巡检。false任务巡检。空全部)
     */
    private Boolean tempBool;
    /**
     * 记录id列表
     */
    private List<Long> recordIdList;
}
