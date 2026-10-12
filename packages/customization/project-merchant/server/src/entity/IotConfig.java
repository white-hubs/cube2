package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 物联网配置
 * </p>
 *
 * @author zwb
 * @since 2020-09-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("iot_config")
public class IotConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 平台地址Api访问
     */
    private String platformSite;

    /**
     * 平台前端跳转地址
     */
    private String platformSiteWeb;

    /**
     * Iot平台项目ID
     */
    private String iotProjectId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 备注
     */
    private String remark;


}
