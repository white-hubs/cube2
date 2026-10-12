package com.guhecloud.cube.base.device.center.metadata.std.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerTagVO {
    private Long id;
    /**
     * 标签名称
     */
    private String tagName;
    /**
     * 标签code
     */
    private String tagCode;
    /**
     * 标签描述
     */
    private String description;
    /**
     * 编辑时间
     */
    private LocalDateTime updateTime;

    /**
     * 状态
     */
    private Boolean enableStatus;

    /**
     * 是否绑定设备
     */
    private Boolean whetherBindDevice;

}
