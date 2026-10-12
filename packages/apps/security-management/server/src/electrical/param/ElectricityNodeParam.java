package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
public class ElectricityNodeParam {
    private Long id;
    private String code;
    /**
     * 节点名称
     */
    private String name;
    /**
     * 节点类型
     */
    private String type;
    /**
     * 节点关联设备ids
     */
    @Length(max = 500, message = "节点关联设备不能超过20个")
    private String deviceIds;
    /**
     * 节点关联子节点ids
     */
    private String childrenIds;
    /**
     * 所在位置
     */
    private String location;
    private String description;
    /**
     * 网页样式(前端定位用)
     */
    private String webStyle;
    private String createBy;
    private LocalDateTime createTime;
}
