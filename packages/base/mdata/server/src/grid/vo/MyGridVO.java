package com.guhecloud.cube.biz.mdata.server.grid.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import lombok.Data;

import java.util.List;

/**
 * 我负责的网格 VO
 */
@Data
public class MyGridVO {

    /**
     * 用户信息（包含在岗信息、头像、角色等）
     */
    private UserDTO user;

    private FileVO headFile;

    /**
     * 负责的网格列表名称
     */
    private List<GridAreaVO> gridAreas;

    /**
     * 允许交班的网格
     */
    private List<GridAreaVO> handoverGridAreas;
}
