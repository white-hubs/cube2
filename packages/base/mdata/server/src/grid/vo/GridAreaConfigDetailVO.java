package com.guhecloud.cube.biz.mdata.server.grid.vo;

import com.guhecloud.cube.biz.mdata.server.grid.enumeration.GridRelationTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * 区域网格配置详情 VO
 */
@Data
public class GridAreaConfigDetailVO {

    /**
     * 网格区域id
     */
    private Long gridAreaId;

    /**
     * 网格管理部门id
     */
    private Long gridDepartmentId;

    /**
     * 网格管理长 id列表（可多人）
     */
    private List<Long> gridManagerIds;

    /**
     * 工程负责人
     */
    private Long engineeringManager;

    /**
     * 安全负责人
     */
    private Long safetyManager;

    /**
     * 保洁负责人
     */
    private Long cleaningManager;

    /**
     * 计量管理负责人
     */
    private Long meteringManager;

    /**
     * 计量管理人员
     */
    private Long meteringStaff;

    /**
     * 网格成员配置列表
     */
    private List<GridMemberConfigVO> gridMembers;

    /**
     * 网格成员配置
     */
    @Data
    public static class GridMemberConfigVO {
        /**
         * 关联类型
         */
        private GridRelationTypeEnum relationType;

        /**
         * 关联id列表（可多选）
         */
        private List<Long> relationIds;
    }
}

