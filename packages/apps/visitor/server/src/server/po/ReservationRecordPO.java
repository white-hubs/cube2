package com.guhecloud.cube.srv.gen.visitor.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 预约记录表
 */
@Data
@TableName("reservation_record")
@EqualsAndHashCode(callSuper = true)
public class ReservationRecordPO extends BasicEnhancedTenantEntity<Long> {
    private static final long serialVersionUID = 8622557980484748248L;
    private Long id; // 预约id（通行记录的通行人id）
    private String name;
    private String phone;
    private String dw; // 单位
    private String address; // 住址
    private Integer personSum; // 来访人数
    private String reason; // 来访理由（非必填）
    private String faceFile; // 人脸图片（非必填） base64，须加前缀data:image/jpeg;base64,图片大小5k~4M,500*500,五官清晰
    private Integer certificateType; // 证件类型
    private String certificateNumber; // 证件号

    private Long areaId; // 预约区域id
    private String areaCode;
    private String areaName;
    private LocalDateTime startDateTime; // 预约开始时间
    private LocalDateTime endDateTime; // 预约结束时间
    private String dayStr; // 预约日期字符串
    /**
     * 预约时段字符串(这里有次日的中文，不能用作日期)
     */
    private String timeStr;
    /**
     * // 跨天标识
     */
    private Boolean nextDayBool;

    private Integer status; // 状态 0：待审核 1：预约 2：在访 3：离访 4:预约取消

    private LocalDateTime rvStartTime; // 实际来访时间
    private LocalDateTime rvEndTime; // 实际离访时间
    private String vBarCode;//预约码
    private String processId;//流程id
}
