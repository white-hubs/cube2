package com.guhecloud.cube.biz.mdata.server.grid.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 网格成员 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GridMemberVO {

    /**
     * 成员ID
     */
    private Long id;

    /**
     * 成员名称
     */
    private String name;

    /**
     * 成员电话
     */
    private String mobile;

}

