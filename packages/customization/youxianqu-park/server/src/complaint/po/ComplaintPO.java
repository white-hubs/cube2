package com.guhecloud.cube.customized.youxianqu.park.management.server.complaint.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 投诉数据实体模型
 *
 * @author lanchun
 * @date 2024/8/28 16:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode
@TableName(value = "ZHYQ_COMPLAINT")
public class ComplaintPO {

    /**
     * 顺序号
     */
    @TableField(value = "ID")
    private Long id;

    /**
     * 用户类型
     */
    @TableField(value = "USER_TYPE")
    private String userType;

    /**
     * 用户名称
     */
    @TableField(value = "USERNAME")
    private String username;

    /**
     * 内容
     */
    @TableField(value = "CONTENT")
    private String content;

    /**
     * 投诉建议
     */
    @TableField(value = "SORT_TYPE")
    private String sortType;

    /**
     * 提交时间
     */
    @TableField(value = "CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 回复内容
     */
    @TableField(value = "CONTENT_REPLY")
    private String contentReply;

    /**
     * 回复时间
     */
    @TableField(value = "REPLY_TIME")
    private LocalDateTime replyTime;

    /**
     * 图片地址
     */
    @TableField(value = "IMGURL")
    private String imgUrl;

    /**
     * 状态 未处理，处理中，处理完成(ComplaintStatusEnum)
     */
    @TableField(value = "STATUS")
    private String status;

    /**
     * 联系人
     */
    @TableField(value = "CONTACTS")
    private String contacts;

    /**
     * 联系电话
     */
    @TableField(value = "PHONE")
    private String phone;

    /**
     * 公有云id
     */
    @TableField(value = "PUBLICID")
    private String publicId;

    /**
     * 公司编码
     */
    @TableField(value = "UNITCODE")
    private String unitCode;

    /**
     * 送审标志
     */
    @TableField(value = "SPFLAG")
    private String spflag;

    /**
     * 企业ID
     */
    @TableField(value = "ENTERPRISE_ID")
    private String enterpriseId;

}
