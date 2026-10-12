package com.guhecloud.cube.biz.mdata.server.grid.param;

import com.guhecloud.cube.biz.mdata.server.grid.enumeration.GridRelationTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 设置区域网格配置参数
 */
@Data
public class GridAreaConfigParam {

    /**
     * 网格区域id
     */
    @NotNull(message = "网格区域id不能为空")
    private Long gridAreaId;

    /**
     * 网格管理部门id
     */
    private Long gridDepartmentId;

    /**
     * 网格管理长id列表（可多人）
     */
    private List<Long> gridManagerIds;

    /**
     * 工程负责人id
     */
    private Long engineeringManager;

    /**
     * 安全负责人id
     */
    private Long safetyManager;

    /**
     * 保洁负责人id
     */
    private Long cleaningManager;

    /**
     * 计量管理负责人id
     */
    private Long meteringManager;

    /**
     * 计量管理人员id
     */
    private Long meteringStaff;

    /**
     * 网格成员配置列表
     */
    private List<GridMemberConfig> gridMembers;

    /**
     * 网格成员配置
     */
    @Data
    public static class GridMemberConfig {
        /**
         * 关联类型
         */
        @NotNull(message = "关联类型不能为空")
        private GridRelationTypeEnum relationType;

        /**
         * 关联id列表（可多选）
         */
        @NotNull(message = "关联id列表不能为空")
        private List<Long> relationIds;
    }

}

