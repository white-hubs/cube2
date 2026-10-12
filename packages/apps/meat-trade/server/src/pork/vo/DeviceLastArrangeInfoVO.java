package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeEnum;
import lombok.Data;

/**
 * 设备最后一次理货信息
 *
 * @author lanchun
 * @date 2023/8/3 14:16
 */
@Data
public class DeviceLastArrangeInfoVO {

    /**
     * 商户信息
     */
    private SupplierInfoVO supplierInfo;

    /**
     * 理货详细数据
     */
    private PorkArrangeDetailVO porkArrangeDetail;

    /**
     * 理货信息
     */
    private PorkArrangeVO porkArrange;

    /**
     * 报备信息
     */
    private PorkReportVO porkReport;

    /**
     * 理货状态
     */
    private PorkArrangeEnum porkArrangeStatus;

    /**
     * 理货审核状态
     */
    private PorkArrangeAuditEnum porkArrangeAuditStatus;

    /**
     * 报备情况
     */
    private PorkReportStatAuditVO porkReportStatAudit;

    /**
     * 理货情况
     */
    private PorkStatArrangeAuditVO porkStatArrangeAudit;
}
