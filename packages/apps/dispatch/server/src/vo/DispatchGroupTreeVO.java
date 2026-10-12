package com.guhecloud.cube.biz.dispatch.server.vo;

import com.guhecloud.platform.base.model.Tree;
import lombok.Data;

/**
 * @author Appet
 * @Description 调度设备-分组
 * @date 2022-07-26 10:08:36
 */
@Data
public class DispatchGroupTreeVO extends Tree.Simple<String, DispatchGroupTreeVO> {

    //
    private String id;
    //父级节点
    private String parentId;
    //编号
    private String code;
    //名称
    private String name;
    //类型
    private String type;
    //类型名称
    private String typeName;
    //节点类型
    private String category;
    //节点状态 组和设备可能表述的意思不同；通过category区分设备还是组
    private String status;

    private long count;

}