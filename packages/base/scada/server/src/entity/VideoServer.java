package com.guhecloud.kaihe.scada.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("GHVideo_Server")
public class VideoServer {

    @TableId
    private Integer id;

    @TableField("serverName")
    private String serverName;

    @TableField("port")
    private String port;

    @TableField("ip")
    private String ip;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("session")
    private String session;

    @TableField("projectId")
    private Integer projectId;

    @TableField("deviceType")
    private Integer deviceType;

}
