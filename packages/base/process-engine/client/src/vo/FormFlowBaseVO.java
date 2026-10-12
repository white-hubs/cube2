package vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/13 16:36
 * @Description: 流程表单基本信息
 */
@Data
public class FormFlowBaseVO {

    /**
     * 应用
     */
    private String application;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 表单定义ID
     */
    private Long formDefinitionId;

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
     * 业务类型名称
     */
    private String businessTypeName;
    /**
     * 表单业务类型
     */
    private String businessType;

    /**
     * 业务与表单映射关系
     */
    private List<FormBizFieldMapVO> formBizFieldMapList;
}
