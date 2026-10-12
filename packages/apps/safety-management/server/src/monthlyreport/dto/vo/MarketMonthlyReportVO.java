package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-07  16:36
 * @Description:
 * @Version: 1.0
 */
@Data
public class MarketMonthlyReportVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 总部月报主键
     */
    private Long monthlyReportId;

    /**
     * 标题
     */
    private String title;

    /**
     * 月报月份
     */
    private String monthly;
    /**
     * 月报时间
     */
    private LocalDateTime monthDate;
    /**
     * 填报状态
     */
    private String status;

    /**
     * 填报状态名
     */
    private String statusName;

    /**
     * 填报数据
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String reportData;

    /**
     * 上报人
     */
    private Long reportUserId;

    /**
     * 上报人姓名
     */
    private String reportUserName;

    /**
     * 上报时间
     */
    private LocalDateTime reportTime;

    /**
     * 文件地址
     */
    private String reportUrl;

    /**
     * 文件id
     */
    private Long reportId;

    /**
     * 文件名称
     */
    private String reportName;

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 市场租户
     */
    private Integer marketTenant;

    /**
     * 填报是否到期
     */
    private Boolean reportExpired;

    public String getReportUserName() {
        UserDTO user = null;
        if (Objects.nonNull(reportUserId)) {
            UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
            user = userClient.getUser(reportUserId);
        }
        if (Objects.nonNull(user)) {
            return user.getName();
        }
        return StringUtils.EMPTY_STRING;
    }

    public Boolean getReportExpired() {
        var monthDate = this.monthDate;
        var offsetMonth = DateUtils.offsetDay(DateUtils.offsetMonth(monthDate, 1), 10);
        var endOffsetMonth = DateUtils.endOfDay(offsetMonth);
        return endOffsetMonth.isBefore(LocalDateTime.now());
    }
}
