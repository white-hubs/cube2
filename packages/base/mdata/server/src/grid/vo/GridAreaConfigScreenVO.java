package com.guhecloud.cube.biz.mdata.server.grid.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 网格区域配置 VO（网格管理屏专用）
 * gridMembers 使用 GridManagerVO 返回完整成员信息（含头像、在岗状态）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GridAreaConfigScreenVO extends GridAreaConfigVO {

    /**
     * 网格成员列表（含头像、在岗状态、手机）
     */
    private List<GridManagerVO> gridMemberInfo;
}
