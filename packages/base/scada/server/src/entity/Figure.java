package com.guhecloud.kaihe.scada.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "GHD_Figures")
public class Figure {

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer ID;

    @TableField("FigureKindID")
    private Integer FigureKindID;

    @TableField("FigureName")
    private String FigureName;

    @TableField("FigureFunctionID")
    private Integer FigureFunctionID;

    @TableField("FileName")
    private String FileName;

    @TableField("ImageName")
    private String ImageName;

    @TableField("Sort")
    private Integer Sort;

    @TableField("CreateTime")
    private Date CreateTime;

    @TableField("projectId")
    private Integer projectId;

    @TableField("Public")
    private Boolean Public;


}
