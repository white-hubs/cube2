package com.guhecloud.kaihe.scada.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("GHVideo_Cam")
public class VideoCam {
    @TableField("camToken")
    private String camToken;
    @TableField("serverId")
    private Integer serverId;
    @TableField("areaId")
    private Integer areaId;
    @TableField("strName")
    private String strName;
    @TableField("strUrl")
    private String strUrl;
    @TableField("strUser")
    private String strUser;

}
