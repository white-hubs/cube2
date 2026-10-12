package com.guhecloud.cube.biz.dispatch.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author Appet
 * @Description 调度台配置
 * @date 2022-07-26 10:08:36
 */
@Data
public class DispatchConfigQueryParam extends Paging {

    //账号
    private String account;
    //密码
    private String password;
    //平台地址
    private String platformAddr;
    //扩展配置
    private String extendConfig;

}