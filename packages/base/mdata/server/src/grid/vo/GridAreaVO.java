package com.guhecloud.cube.biz.mdata.server.grid.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GridAreaVO {
    /**
     * 区域id
     */
    private Long areaId;
    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 子网格
     */
    private List<GridAreaVO> children;

    /**
     * 是否可编辑网格员（网格长及其子网格为 true，仅网格员为 false）
     */
    private Boolean canEdit;

    /**
     * 网格员信息
     */
    private List<GridMemberVO> gridMembers;
}
