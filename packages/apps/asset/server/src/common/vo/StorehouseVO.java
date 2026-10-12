package com.guhecloud.cube.srv.gen.property.management.server.common.vo;

import lombok.Data;

/**
 * @author Appet
 * @Description 仓库管理
 * @date 2023-03-16 10:08:36
 */
@Data
public class StorehouseVO {

    //
    private Long id;
    //仓库名称
    private String name;
    //仓库编号
    private String code;
    //仓库地址
    private String address;
    //仓库管理员
    private String storeman;
    //联系电话
    private String phone;
    //备注
    private String remark;

}