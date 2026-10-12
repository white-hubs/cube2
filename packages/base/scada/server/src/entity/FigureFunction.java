package com.guhecloud.kaihe.scada.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("GHD_FigureFunction")
public class FigureFunction {

    @TableId("ID")
    private String ID;

    @TableField("FunctionName")
    private String FunctionName;

    @TableField("FunctionDesc")
    private String FunctionDesc;

}
