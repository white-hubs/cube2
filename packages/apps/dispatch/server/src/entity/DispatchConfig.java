package com.guhecloud.cube.biz.dispatch.server.entity;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Appet
 * @Description 调度台配置
 * @date 2022-07-26 10:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DispatchConfig extends BasicTenantEntity<Long> {

    //
    private Long id;
    //账号
    private String account;
    //密码
    private String password;
    //平台地址
    private String platformAddr;
    //扩展配置
    private String extendConfig;

}