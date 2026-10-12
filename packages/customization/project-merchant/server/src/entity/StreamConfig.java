package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 流媒体服务配置
 * </p>
 *
 * @author zwb
 * @since 2020-09-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("stream_config")
public class StreamConfig implements Serializable {

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
     * 名称
     */
    private String name;

    /**
     * 主机
     */
    private String host;

    /**
     * IP
     */
    private String ip;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 公网rtmp端口
     */
    private Integer rtmpPort;

    /**
     * 内网IP
     */
    private String lanIp;

    /**
     * 内网端口
     */
    private Integer lanPort;

    /**
     * 系统用户名
     */
    private String username;

    /**
     * 系统密码
     */
    private String password;

    /**
     * 备注
     */
    private String remark;


}
