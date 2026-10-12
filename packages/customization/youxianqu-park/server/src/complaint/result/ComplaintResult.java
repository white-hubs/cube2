package com.guhecloud.cube.customized.youxianqu.park.management.server.complaint.result;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 投诉返回对象
 */
@Data
public class ComplaintResult {
    private Long id;
    /**
     * 用户类型
     */
    private String userType;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 内容
     */
    private String content;

    /**
     * 投诉建议
     */
    private String sortType;

    /**
     * 提交时间
     */
    private LocalDateTime createTime;

    /**
     * 回复内容
     */
    private String contentReply;

    /**
     * 回复时间
     */
    private LocalDateTime replyTime;

    /**
     * 图片地址
     */
    private String imgUrl;

    /**
     * 状态 未处理，处理中，处理完成(ComplaintStatusEnum)
     */
    private String status;
}
