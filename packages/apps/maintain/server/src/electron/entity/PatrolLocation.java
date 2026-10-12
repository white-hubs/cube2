package com.guhecloud.cube.srv.gen.maintain.server.electron.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 巡更点信息;
 *
 * @author :lijie
 * @date : 2023-5-25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("patrol_location")
public class PatrolLocation extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 巡更设备id
     */
    private Long deviceId;
    /**
     * 巡更点名称
     */
    private String patrolLocationName;
    /**
     * nfc芯片编码
     */

    private String nfcCode;
    /**
     * 模型编码
     */
    private String bimCode;
    /**
     * 说明
     */

    private String remarks;
    /**
     * 经度
     */

    private String patrolLongitude;
    /**
     * 纬度
     */

    private String patrolDimension;
    /**
     * 区域id
     */
    private String areaId;
    /**
     * {@link com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.DeleteStatusEnum}
     */
    private String status;
    /**
     * 蓝牙编号
     */
    private String bluetoothCode;
}
