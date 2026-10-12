package com.guhecloud.cube.srv.gen.maintain.server.electron.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("patrol_nfc_details")
public class NfcDetails extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * NFC芯片编号
     */
    private String nfcCode;
    /**
     * 经度
     */
    private String nfcLongitude;
    /**
     * 纬度
     */
    private String nfcDimension;
    /**
     * 说明
     */
    private String remarks;
    private String status;
    /**
     * 蓝牙编号
     */
    private String bluetoothCode;
    /**
     * 设备编号
     */
    private String deviceCode;
}
