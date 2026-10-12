package com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.property.management.server.property.filter.PropertyCurrentUserDeptFieldFilter;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 资产明细;
 *
 * @author : lijie
 * @date : 2023-3-9
 */
@Data
@TableName("property_detailed")
@DataPermission({
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "maintenance_dep_id",
                             fieldFilter = PropertyCurrentUserDeptFieldFilter.class
        ),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "maintenance_dep_id")
}
)
public class PropertyDetailed extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 资产编码
     */
    private String serialNumber;
    /**
     * 资产名称
     */
    private String name;
    /**
     * 资产类别id
     */
    private Long typeId;
    /**
     * 资产类别名称
     */
    private String typeName;
    /**
     * 购入时间
     */
    private LocalDateTime purchaseDate;
    /**
     * 维保期限（月）
     */
    private Integer maintenanceTime;
    /**
     * 维保到期时间
     */
    private LocalDateTime maintenanceDate;
    /**
     * 使用期限（月）
     */
    private Integer usageTime;
    /**
     * 逾龄时间
     */
    private LocalDateTime usageDate;
    /**
     * 规格型号
     */
    private String norms;
    /**
     * 计量单位
     */
    private String unit;
    /**
     * 管理员id
     */
    private Long adminId;
    /**
     * 区域id
     */
    private String areaId;
    /**
     * 入库单号
     */
    private String depositoryNumbers;
    /**
     * 数量
     */
    private Integer number;
    /**
     * 原值
     */
    private BigDecimal price;
    /**
     * 存放地点
     */
    private String storageLocation;
    /**
     * 供应商
     */
    private String supplier;
    /**
     * 供应商联系方式
     */
    private String supplierPhone;
    /**
     * 供应商联系人
     */
    private String supplierUser;
    /**
     * 维保说明
     */
    private String maintenanceRemarks;
    /**
     * 状态
     */
    private String status;
    /**
     * 资产图片
     */
    private String imgs;
    /**
     * 资产附件
     */
    private String file;
    /**
     * 设备编码
     */
    private String deviceNumber;
    /**
     * 数据过滤条件，现用于盘点时新增资产时，其他模块不需要展示
     */
    private String authority;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 来源
     */
    private Long sourceId;
    /**
     * 入库时间
     */
    private LocalDateTime startTime;
    /**
     * 使用部门
     */
    private Long receiveDepId;
    private String receiveDepName;
    /**
     * 使用人
     */
    private Long receiveUserId;
    private Integer level;
    /**
     * 使用人电话
     */
    private String receiveUserPhone;
    /**
     * 财务编码
     */
    private String financeNumber;
    /**
     * 维护部门
     */
    private Long maintenanceDepId;
    private String maintenanceDepName;
    /**
     * 经纬度
     */
    private String latAndLon;
    /**
     * 经纬度地点
     */
    private String location;
}
