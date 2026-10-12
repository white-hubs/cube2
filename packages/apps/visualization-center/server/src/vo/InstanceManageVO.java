package com.guhecloud.cube.base.visualization.server.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InstanceManageVO {
    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 实例名称
     */
    private String instanceName;
    /**
     * 链接地址
     */
    private String path;
    /**
     * 状态
     */
    private String status;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
