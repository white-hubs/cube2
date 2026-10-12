package com.guhecloud.cube.biz.meattrade.server.pork.rocketmq.message;

import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 理货开始消息
 *
 * @author lanchun
 * @date 2023/4/13 10:39
 */
@Data
public class PorkArrangeMessage {

    public static final String TOPIC = "PORK_ARRANGE";

    private Long id;

    /**
     * 每次请求唯一
     */
    private String guid;

    /**
     * 报备主键
     */
    private Long porkReportId;

    /**
     * 报备号
     */
    private String porkReportCode;

    /**
     * 理货片数
     */
    private Integer slices;

    /**
     * 理货重量
     */
    private BigDecimal weight;

    /**
     * 理货地点
     */
    private String place;

    /**
     * 理货设备台号
     */
    private String terminalNumber;

    /**
     * 理货状态
     */
    private PorkArrangeEnum status;

    /**
     * 理货机理货状态
     */
    private Integer tallyMachineEventType;

    /**
     * 理货开始时间
     */
    private LocalDateTime startTime;

    /**
     * 理货结束时间
     */
    private LocalDateTime endTime;

    /**
     * 理货完成时间
     */
    private LocalDateTime porkArrangeTime;

    /**
     * 市场方租户编号
     */
    private Integer tenant;

    /**
     * 理货详细信息
     */
    private List<PorkArrangeDetailMessage> porkArrangeDetailMessages;
}
