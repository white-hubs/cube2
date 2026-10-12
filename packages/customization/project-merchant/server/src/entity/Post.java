package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "post")
public class Post {

    private String id;

    private String projectId;
    
    private String code;

    private String name;

    private String pinyin;

    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted;

}
