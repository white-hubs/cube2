package com.guhecloud.cube.srv.gen.stall.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * 档位信息
 */
@Data
@TableName(value = "gen_stall")
public class GenStall extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 档位编号
     */
    private String code;

    /**
     * 档位名称
     */
    private String name;

    /**
     * 档位类型
     */
    private String type;

    /**
     * 档位区域
     */
    private String area;

    /**
     * 具体位置
     */
    private String location;

    /**
     * 建筑面积
     */
    private String builtUpArea;

    /**
     * 实用面积
     */
    private String practicalArea;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 出租类型
     */
    private Boolean leaseType;

    /**
     * 是否出租
     */
    private Boolean leased;

    /**
     * 模型绑定
     */
    private Boolean bimBind = false;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

}
