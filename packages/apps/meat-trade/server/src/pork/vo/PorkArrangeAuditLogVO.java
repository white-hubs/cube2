package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 理货审核记录
 *
 * @author lzy
 * @date 2021/9/11
 */
@Data
public class PorkArrangeAuditLogVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 理货审核员主键
     */
    private Long auditorId;

    /**
     * 理货审核员姓名
     */
    private String auditorName;

    /**
     * 理货审核主键
     */
    private Long porkArrangeAuditId;

    /**
     * 审核状态
     */
    private PorkArrangeAuditEnum status;

    /**
     * 审核时间
     */
    private LocalDateTime time;

    /**
     * 备注
     */
    private String remark;
}
