package com.guhecloud.cube.biz.dispatch.server.entity;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Appet
 * @Description 调度设备
 * @date 2022-07-26 10:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DispatchDevice extends BasicTenantEntity<String> {

    //
    private String id;
    //名称，可以是人员名称，也可以是设备名称
    private String name;
    //编号
    private String code;
    //密码
    private String password;
    //类型：参见对接文档
    private String type;
    //预留字段
    private Long userId;

}