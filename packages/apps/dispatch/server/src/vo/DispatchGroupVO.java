package com.guhecloud.cube.biz.dispatch.server.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 调度设备-分组
 * @date 2022-07-26 10:08:36
 */
@Data
public class DispatchGroupVO {

    //
    private String id;
    //父级节点
    private String parentId;
    //组编号
    private String code;
    //组名称
    private String name;
    //组类型
    private String type;
    //
    private String typeName;
    //有效期
    private Integer validTime;
    //创建时间
    private LocalDateTime createTime;

}