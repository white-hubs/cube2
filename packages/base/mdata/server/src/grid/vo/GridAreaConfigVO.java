package com.guhecloud.cube.biz.mdata.server.grid.vo;

import com.guhecloud.cube.biz.mdata.server.grid.enumeration.GridHandoverStatusEnum;
import lombok.Data;

import java.util.List;

/**
 * 区域网格配置 VO
 */
@Data
public class GridAreaConfigVO {

    /**
     * 网格区域id
     */
    private Long gridAreaId;
    /**
     * 网格名称
     */
    private String gridName;

    /**
     * 网格下属区域名称
     */
    private String childAreaName;

    /**
     * 网格管理部门id
     */
    private Long gridDepartmentId;

    /**
     * 网格管理部门名称
     */
    private String gridDepartmentName;

    /**
     * 网格管理长列表
     */
    private List<GridManagerVO> gridManagers;

    /**
     * 工程负责人
     */
    private GridManagerVO engineeringManager;

    /**
     * 安全负责人
     */
    private GridManagerVO safetyManager;

    /**
     * 保洁负责人
     */
    private GridManagerVO cleaningManager;

    /**
     * 计量管理负责人
     */
    private GridManagerVO meteringManager;

    /**
     * 计量管理人员
     */
    private GridManagerVO meteringStaff;

    /**
     * 网格成员名称列表
     */
    private List<GridMemberVO> gridMembers;

    /**
     * 当前用户在该网格的交接状态
     */
    private GridHandoverStatusEnum handoverStatus;

    /**
     * 当前用户待接班记录ID
     */
    private Long handoverRecordId;

    /**
     * 当前用户在该网格的交接状态描述
     */
    private String handoverStatusDesc;
}

