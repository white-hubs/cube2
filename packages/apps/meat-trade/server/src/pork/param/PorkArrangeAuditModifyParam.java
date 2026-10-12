package com.guhecloud.cube.biz.meattrade.server.pork.param;

import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lzy
 * @date 2021/12/6
 */
@Data
public class PorkArrangeAuditModifyParam {

    /**
     * 来货报备主键
     */
    private Long porkReportId;

    /**
     * 检疫票登记主键
     */
    private Long pratiqueRegisterId;

    /**
     * 食安检验主键
     */
    private Long spotCheckId;

    /**
     * 理货数据主键
     */
    private Long porkArrangeId;

    /**
     * 审核员主键
     */
    private Long auditorId;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 审核状态
     */
    private PorkArrangeAuditEnum auditStatus;

    /**
     * 备注
     */
    private String remark;

}
