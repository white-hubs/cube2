package instance.command;

import com.guhecloud.cube.base.flow.client.dto.StartUserInfo;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bq
 * @date 2022/11/10
 * @description 业务提交初始表单数据--发起审批
 */
@Data
public class SubmitInitFormCommand {

    /**
     * 表单ID
     */
    private Long id;

    /**
     * 表单单号
     */
    private String formNo;

    /**
     * 表单单号前缀
     */
    private String formNoPrefix;

    /**
     * 名称
     */
    private String name;
    /**
     * 电话
     */
    private String phone;

    /**
     * 是否外部发起
     */
    private boolean publicForm;
    /**
     * 自定义统一显示待办名称
     */
    private String backLogTitle;

    /**
     * 类型
     */
    private String type;

    /**
     * 应用
     */
    private String application;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务类型
     */
    private String businessTypeName;

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
     * 表单定义id
     */
    private Long formDefinitionId;

    /**
     * 表单数据
     */
    private String data;
    /**
     * 流程变量
     */
    private Object variable;
    /**
     * 自定义变量
     */
    private Map<String, Object> variables = new HashMap<>();
    /**
     * 指定审批人
     */
    private Map<String, String> approveUsers = new HashMap<>();

    /**
     * 发起人信息
     */
    private StartUserInfo startUserInfo;

}
