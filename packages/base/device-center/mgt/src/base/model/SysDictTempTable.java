package com.guhecloud.cube.iot.mgt.base.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "iot_sys_dict_temp_table")
public class SysDictTempTable {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @TableField(value = "item_level")
    private String itemLevel;

    @TableField(value = "item_label")
    private String itemLabel;

    @TableField(value = "item_value")
    private String itemValue;

    @TableField(value = "item_level_concat")
    private String itemLevelConcat;

    @TableField(value = "item_label_concat")
    private String itemLabelConcat;

    @TableField(value = "item_id_concat")
    private String itemIdConcat;
}
