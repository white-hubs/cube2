package com.guhecloud.cube.srv.gen.visitor.management.server.vo.result;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/24 17:01
 * @Description: 邀约记录列表
 */

@Data
public class VisitorInviteRecordResult {
    /**
     * 主键
     */
    private Long id;

    /**
     * 访客姓名
     */
    private String visitorName;

    /**
     * 访客身份证号
     */
    private String visitorIdCard;

    /**
     * 访客电话
     */
    private String visitorPhone;

    /**
     * 访问区域
     */
    private String areaId;

    /**
     * 访问区域
     */
    private String areaName;

    /**
     * 访问日期
     */
    private String accessDate;

    /**
     * 访问时段
     */
    private String timeStr;

    /**
     * 状态
     */
    private String status;

    /**
     * 流程实例id
     */
    private String processInsId;

    /**
     * 预约二维码
     */
    private String barCode;
}
