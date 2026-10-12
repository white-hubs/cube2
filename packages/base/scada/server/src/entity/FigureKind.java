package com.guhecloud.kaihe.scada.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("GHD_FigureKind")
public class FigureKind {

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer ID;

    @TableField("KindName")
    private String KindName;

    @TableField("FolderName")
    private String FolderName;

    @TableField("ParentID")
    private Integer ParentID;

    @TableField("Sort")
    private Integer Sort;

    @TableField("CreateTime")
    private Date CreateTime;

    @TableField("projectId")
    private Integer projectId;

    @TableField("Public")
    private Boolean Public;

}
