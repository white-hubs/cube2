package com.guhecloud.cube.base.flow.context.interfaces.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.base.flow.context.enumeration.FormFlowStatusEnum;
import com.guhecloud.cube.base.flow.context.enumeration.FormFlowTypeEnum;
import com.guhecloud.cube.base.flow.context.enumeration.PermissionType;
import com.guhecloud.cube.base.flow.webhooks.vo.WebhookConfigVO;
import lombok.Data;
import vo.FormBizFieldMapVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author bq
 * @date 2022/09/08
 * @description 表单定义详情
 */
@Data
public class FormFlowDetailVO {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    private String typeName;

    /**
     * 应用
     */
    private String application;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 状态
     */
    private String status;

    private String statusName;

    /**
     * 启停
     */
    private Boolean enable;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否公共表单
     */
    private Integer publicForm;

    /**
     * 公开表单二维码图片
     */
    private Long qrImage;

    /**
     * 流程key
     */
    private String flowKey;

    /**
     * 表单定义key
     */
    private String formKey;

    /**
     * 表单定义版本
     */
    private Integer formDefinitionVersion;

    /**
     * 表单定义ID
     */
    private Long formDefinitionId;
    /**
     * 表单分组
     */
    private Long formGroupId;

    /**
     * 表单分组名称
     */
    private String formGroupName;

    /**
     * 简称
     */
    private String shortName;

    /**
     * 权限类型
     */
    private PermissionType permissionType;

    /**
     * 自定义表单编码
     */
    private String customCode;

    /**
     * 权限集合
     */
    private List<FormFieldVO> permissionList;
    /**
     * 表单定义Json
     */
    private String formJson;

    private List<FormFieldVO> formFieldList;

    private List<FormBizFieldMapVO> formBizFieldMapList;

    /**
     * 流程定义json
     */
    private String flowJson;

    private LocalDateTime updateTime;

    private String businessTypeName;
    /**
     * 表单业务类型
     */
    private String businessType;
    /**
     * 是否自动去重
     */
    private Boolean autoRemoveDuplication;
    /**
     * 自动审批打印展示
     */
    private Boolean autoApprovalPrinting;
    /**
     * 回调下游系统配置
     */
    private List<WebhookConfigVO> webhookConfigVOList;

    /**
     * 公开连接
     */
    private String qrCodeUrl;

    /**
     * 公开表单二维码
     *
     * @param type
     */
    public FileVO fileVO;

    /**
     * 内外部调用类型
     *
     * @param type external:外部、internal:内部
     */
    private String callType;

    public void setType(String type) {
        this.type = type;
        this.typeName = FormFlowTypeEnum.valueOf(type).getValue();
    }

    public void setStatus(String status) {
        this.status = status;
        this.statusName = FormFlowStatusEnum.valueOf(status).getValue();
    }

}
