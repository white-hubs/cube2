package com.guhecloud.cube.base.visualization.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 项目布局;
 *
 * @author :lijie
 * @date : 2023-8-22
 */
@Data
@TableName("projcet_canvas")
public class ProjcetCanvas {
    @TableId
    private Long id;
    private String canvasJson;
    /**
     * 项目id
     */
    private Long projectId;
}
