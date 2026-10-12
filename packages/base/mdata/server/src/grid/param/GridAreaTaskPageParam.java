package com.guhecloud.cube.biz.mdata.server.grid.param;

import com.guhecloud.platform.base.model.Paging;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class GridAreaTaskPageParam extends Paging {
    /**
     * 网格id(就是区域id)
     */
    private Long gridAreaId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    private String businessTypes;

    /**
     * 业务状态
     */
    private String status;

    /**
     * 任务状态
     */
    private String taskStatus;

    private String period;

    private List<Long> userIds;
}
