package com.guhecloud.cube.customized.youxianqu.park.management.server.rent.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 租控记录数据模型实体
 *
 * @author lanchun
 * @date 2024/8/28 15:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode
@TableName(value = "V_ZHYQ_YQWY_ROOM_RENT_VIEW")
public class RentRecordPO {

    /**
     * 用户ID
     */
    @TableField(value = "CUSTID")
    private Long custId;

    /**
     * 公司编码
     */
    @TableField(value = "UNITCODE")
    private String unitCode;

    /**
     * 房间ID
     */
    @TableField(value = "ROOMID")
    private Long roomId;

    /**
     * 出售面积
     */
    @TableField(value = "ROOM_AREA_CALCULATE")
    private String roomAreaCalculate;

    /**
     * 房间层级ID
     */
    @TableField(value = "PATH_IDS")
    private String pathIds;

    /**
     * 房间单位名
     */
    @TableField(value = "ROOM_UNIT_NAME")
    private String roomUnitName;

    /**
     * 用户名
     */
    @TableField(value = "CUSTNAME")
    private String custName;

    /**
     * 房间编号
     */
    @TableField(value = "ROOM_UNIT_NO")
    private String roomUnitNo;

    /**
     * 合同ID
     */
    @TableField(value = "CONTRACTID")
    private String contractId;

    /**
     * 房间状态（RentRoomStatusEnum）
     * 0闲置、1已租、2预定、16已售
     */
    @TableField(value = "STATUS")
    private String status;

    /**
     * 预定状态
     * 0、未预定 1、已预定
     */
    @TableField(value = "RESERVE_STATUS")
    private String reserveStatus;

    /**
     * 锁定状态
     * 0、未锁定 1、已锁定
     */
    @TableField(value = "LOCKED_STATUS")
    private String lockedStatus;

    /**
     * 房间缴费状态
     * 0正常缴费、1欠费
     */
    @TableField(value = "PAYSTATUS")
    private String payStatus;

    /**
     * 房间层级名
     */
    @TableField(value = "PATH_NAMES")
    private String pathNames;

    /**
     * 合同类型
     */
    @TableField(value = "CONTRACT_TYPE")
    private String contractType;
}
