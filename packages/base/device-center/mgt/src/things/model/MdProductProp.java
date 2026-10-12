package com.guhecloud.cube.iot.mgt.things.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

/**
 * 产品属性表
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "iot_md_product_prop")
@Data
public class MdProductProp extends BasicEnhancedTenantEntity<Long> {
    public static final String COL_ID = "id";
    public static final String COL_PROD_ID = "prod_id";
    public static final String COL_NAME = "name";
    public static final String COL_PROP_CODE = "prop_code";
    public static final String COL_DATA_TYPE_ID = "data_type_id";
    public static final String COL_PRECISION = "precision";
    public static final String COL_UNIT_ID = "unit_id";
    public static final String COL_DATA_WRITE = "data_write";
    public static final String COL_DATA_CONFIG = "data_config";
    public static final String COL_REMARK = "remark";

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    /**
     * 产品ID
     */
    @TableField(value = "prod_id")
    private Long prodId;
    /**
     * 属性名称
     */
    @TableField(value = "`name`")
    private String name;
    /**
     * 属性Code,产品下唯一
     */
    @TableField(value = "prop_code")
    private String propCode;
    /**
     * 数据类型
     */
    @TableField(value = "data_type")
    private String dataType;
    /**
     * 数据精度
     */
    @TableField(value = "`precision`")
    private Integer precision;
    /**
     * 数据单位id
     */
    @TableField(value = "unit_id")
    private Long unitId;
    /**
     * 数据单位code
     */
    @TableField(value = "unit_code")
    private String unitCode;
    /**
     * 读写类型:0只读:1读写
     */
    @TableField(value = "data_write")
    private int dataWrite;
    /**
     * json数组[0,100] 或 [{"value":"0","label":"开"},{"value":"1","label":"关"}]
     */
    @TableField(value = "data_config")
    private String dataConfig;
    /**
     * 属性描述
     * -- SETTER --
     * 设置属性描述
     *
     * @param remark 属性描述
     */
    @Setter
    @TableField(value = "remark")
    private String remark;
    /**
     * 默认值，产品可配置一个默认值<br/>
     * 请考虑以下三种场景:<P>
     * 当属性未未采集到数据时，使用默认值<br/>
     * 当北向调用重置设备属性时，使用默认值重置<br/>
     * 如果一个事件是由属性模拟生成的，事件重置时，也会使用默认值重置
     * </P>
     */
    @TableField(value = "default_value")
    private String defaultValue;
}