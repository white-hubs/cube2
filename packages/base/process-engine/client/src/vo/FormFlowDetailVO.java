package vo;

import lombok.Data;

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
     * 公开连接
     */
    private String qrCodeUrl;

    /**
     * 内外部调用类型
     *
     * @param type external:外部、internal:内部
     */
    private String callType;
}
