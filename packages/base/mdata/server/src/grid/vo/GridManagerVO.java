package com.guhecloud.cube.biz.mdata.server.grid.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GridManagerVO {
    /**
     * 网格管理长id
     */
    private Long gridManagerId;
    /**
     * 头像
     */
    private FileVO avatar;
    /**
     * 网格管理长名称
     */
    private String gridManagerName;
    /**
     * 状态
     */
    private String workStatus;
    /**
     * 手机
     */
    private String mobile;
}
