package com.guhecloud.cube.base.flow.context.commandgateway.command;

import com.guhecloud.cube.base.flow.context.enumeration.PermissionType;
import com.guhecloud.cube.base.flow.webhooks.vo.WebhookConfigVO;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * @author bq
 * @date 2022/10/18
 * @description 流程表单命令
 */
@Data
public class FormFlowCommand {

    /**
     * 表单ID
     */
    private Long id;

    /**
     * 名称
     */
    @Length(max = 20, message = "名称限制在20字内")
    @NotEmpty(message = "名称不能为空")
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
    @Length(max = 200)
    private String remark;

    /**
     * 流程定义json
     */
    private String flowJson;

    /**
     * 表单定义Json
     */
    private String formJson;

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
     * 公开连接
     */
    private String qrCodeUrl;

    /**
     * 表单分组
     */
    private Long formGroupId;

    /**
     * 简称
     */
    private String shortName;

    /**
     * 自定义表单业务编码
     */
    private String businessType;

    /**
     * 权限类型
     */
    private PermissionType permissionType;

    /**
     * 权限id集合（角色ID、用户ID、部门ID）
     */
    private List<Long> permissionIds;
    /**
     * 回调下游系统配置
     */
    private List<WebhookConfigVO> webhookConfigVOList;
}
