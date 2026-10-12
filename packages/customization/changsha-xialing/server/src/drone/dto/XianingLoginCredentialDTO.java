package com.guhecloud.cube.customization.xialing.server.drone.dto;

import lombok.Data;

/**
 * 霞凝无人机登录凭证数据传输对象
 * <p>
 * 用于封装从子系统适配配置中解析的霞凝无人机平台登录信息，
 * 包括基础URL、业务账号、SSO账号、租户ID和客户端ID等认证所需关键参数。
 * </p>
 *
 * @author system
 */
@Data
public class XianingLoginCredentialDTO {
    /**
     * 霞凝无人机平台的基础URL地址
     * <p>
     * 例如: https://drone.example.com
     * </p>
     */
    private String baseUrl;

    /**
     * 业务调用账号
     * <p>
     * 用于无人机业务接口调用与定时任务同步的OAuth2密码模式认证账号
     * </p>
     */
    private String bizUsername;

    /**
     * 业务调用密码
     * <p>
     * 用于无人机业务接口调用与定时任务同步的OAuth2密码模式认证密码
     * </p>
     */
    private String bizPassword;

    /**
     * 可视化SSO账号
     * <p>
     * 用于生成可视化免登链接时的OAuth2密码模式认证账号
     * </p>
     */
    private String ssoUsername;

    /**
     * 可视化SSO密码
     * <p>
     * 用于生成可视化免登链接时的OAuth2密码模式认证密码
     * </p>
     */
    private String ssoPassword;

    /**
     * 指挥调度入口SSO账号
     * <p>
     * 用于我们系统指挥调度入口跳转无人机平台时的OAuth2密码模式认证账号
     * </p>
     */
    private String commandDispatchSsoUsername;

    /**
     * 指挥调度入口SSO密码
     * <p>
     * 用于我们系统指挥调度入口跳转无人机平台时的OAuth2密码模式认证密码
     * </p>
     */
    private String commandDispatchSsoPassword;

    /**
     * 租户ID
     * <p>
     * 霞凝平台的租户标识，如果未配置则使用当前系统租户ID
     * </p>
     */
    private String tenantId;

    /**
     * 客户端ID
     * <p>
     * OAuth2认证的客户端标识，默认为 "nest"
     * </p>
     */
    private String clientId;

    /**
     * MQTT broker 连接地址
     * <p>
     * 例如: tcp://127.0.0.1:1883
     * </p>
     */
    private String mqttHost;

    /**
     * MQTT 客户端用户名
     */
    private String mqttUsername;

    /**
     * MQTT 客户端密码
     */
    private String mqttPassword;

    /**
     * MQTT 客户端ID前缀
     */
    private String mqttClientIdPrefix;
}
