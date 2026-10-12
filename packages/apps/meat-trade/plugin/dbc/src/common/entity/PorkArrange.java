package com.guhecloud.cube.sn.meattrade.plugin.common.entity;

import com.guhecloud.cube.sn.meattrade.plugin.common.enumeration.PorkArrangePushDataStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 理货推送数据
 *
 * @author lanchun
 * @date 2024/9/29 11:33
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PorkArrange {

    private Long id;

    /**
     * 记录id(每次存储到数据库时的唯一id)
     */
    private Long recordId;

    /**
     * 客户端唯一 guid
     */
    private String guid;

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
     * 推送次数
     */
    private Integer pushTimes;

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 下次重试时间
     */
    private LocalDateTime nextRetryTime;

    /**
     * 理货详细信息
     */
    private List<PorkArrangeDetail> arrangeDetails;

    /**
     * 推送状态
     */
    private PorkArrangePushDataStatusEnum status;
}
