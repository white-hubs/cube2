package com.guhecloud.kaihe.scada.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "GHD_Category")
public class Category {

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer ID;

    @TableField(value = "Name")
    private String Name;

    @TableField(value = "ParentID")
    private Integer ParentID;

    @TableField(value = "projectId")
    private Integer projectId;

}
