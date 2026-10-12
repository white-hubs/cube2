package com.guhecloud.cube.sn.meattrade.plugin.common.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.sn.meattrade.plugin.common.enumeration.PorkArrangePushDataStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 理货推送数据
 *
 * @author lanchun
 * @date 2024/9/29 11:33
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("pork_arrange_push_data")
public class PorkArrangePushDataPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 客户端唯一 guid
     */
    private String guid;

    /**
     * 理货记录id
     */
    private Long porkArrangeId;

    /**
     * 理货设备编号
     */
    private String deviceId;

    /**
     * 批次号
     */
    private String tallyId;

    /**
     * 理货状态
     */
    private Integer eventType;

    /**
     * 理货开始时间
     */
    private LocalDateTime startTime;

    /**
     * 理货结束时间
     */
    private LocalDateTime endTime;

    /**
     * 理货详细信息(数据参照entity包下的porkArrangeDetail类)
     */
    private String arrangeDetails;

    /**
     * 推送次数
     */
    private Integer pushTimes;

    /**
     * 下次重试时间
     */
    private LocalDateTime nextRetryTime;

    /**
     * 推送状态
     */
    private PorkArrangePushDataStatusEnum status;
}
