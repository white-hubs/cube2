package com.guhecloud.cube.base.flow.context.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author bq
 * @date 2022/09/08
 * @description 表单与流程基本信息管理总表
 */
@Data
@TableName("form_flow")
@EqualsAndHashCode(callSuper = false)
public class FormFlowPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 应用
     */
    private String application;

    /**
     * 状态
     */
    private String status;

    /**
     * 启停
     */
    private Boolean enable;

    /**
     * 备注
     */
    private String remark;

    /**
     * 流程key
     */
    private String flowKey;

    /**
     * 表单定义key
     */
    private String formKey;

    /**
     * 管理员ID
     */
    private Long administratorId;

    /**
     * 管理员姓名
     */
    private String administrator;
    /**
     * 是否自动去重
     */
    private Boolean autoRemoveDuplication;
    /**
     * 自动审批打印展示
     */
    private Boolean autoApprovalPrinting;

    /**
     * 是否公共表单
     */
    private Integer publicForm;

    /**
     * 公开表单url
     */

    private String qrCodeUrl;

    /**
     * 公开表单二维码图片
     */

    private Long qrImage;

    /**
     * 表单分组
     */
    private Long formGroupId;

    /**
     * 简称
     */
    private String shortName;
}