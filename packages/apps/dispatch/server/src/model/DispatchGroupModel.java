package com.guhecloud.cube.biz.dispatch.server.model;

import lombok.Data;

/**
 * @author Appet
 * @Description 调度设备-分组
 * @date 2022-07-26 10:08:36
 */
@Data
public class DispatchGroupModel {

    //父级节点
    private String parentCode;
    //组编号
    private String code;
    //组名称
    private String name;
    //组类型
    private Integer type;
    //有效期
    private Integer validTime;
    //排序号
    private Integer sort;
}