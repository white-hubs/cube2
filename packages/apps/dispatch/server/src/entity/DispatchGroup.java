package com.guhecloud.cube.biz.dispatch.server.entity;

import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Appet
 * @Description 调度设备-分组
 * @date 2022-07-26 10:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DispatchGroup extends BasicTenantEntity<String> {

    //
    private String id;
    //父级节点
    private String parentId;
    //组编号
    @Unique(composites = "deleted", message = "组编号已存在")
    private String code;
    //组名称
    private String name;
    //组类型
    private String type;
    //有效期
    private Integer validTime;
    //排序号
    private Integer sort;

}