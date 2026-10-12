package base.constant;

import com.guhecloud.platform.base.util.StringUtils;

/**
 * @author pengjian
 * @date 2022/10/25 19:51
 * @description 变量常量
 */
public abstract class VariableConstant {

    /**
     * 表示用户变量,可直接返给用户
     */
    public static final String USER_PREFIX = "user_";

    /**
     * 表示内部变量,不能以变量的形式暴露给用户
     */
    public static final String INTERNAL_PREFIX = "internal_";

    /**
     * 结果
     */
    public static final String RESULT = INTERNAL_PREFIX + "result";

    /**
     * 数据
     */
    public static final String DATA = INTERNAL_PREFIX + "data";

    /**
     * 审批意见
     */
    public static final String APPROVAL_OPINION = INTERNAL_PREFIX + "approval_opinion";

    /**
     * 表单实例id
     */
    public static final String FORM_INSTANCE_Id = INTERNAL_PREFIX + "formInstanceId";

    public static final String PRE_SUBMIT_RESPONSE = INTERNAL_PREFIX + "preSubmitResponse";

    /**
     * 表单单号
     */
    public static final String FORM_NO = "formNo";
    /**
     * 发起用户
     */
    public static final String START_USER_INFO = "startUserInfo";

    public static final String REMARK = "remark";
    /**
     * 外部用户
     */
    public static final String OutSider = "OutSider";

    /**
     * 来源工单
     */
    public static final String SOURCE_PROCESS_ID = "sourceProcessId";

    /**
     * 任务处理结果
     */
    public static final String TASK_RESULT = "taskResult";

    /**
     * 内部变量，历史原因，为了兼容
     */
    public static final String ASSIGNEE_List = "assigneeList";

    /**
     * 内部变量，历史原因，为了兼容
     */
    public static final String TEMPORARY = "temporary";

    public static final String ROOT = "root";

    public static final String BACK_NODE = "backNode";

    public static String getVariableName(String name) {
        if (StringUtils.isEmpty(name)) {
            return "";
        }
        if (name.startsWith(INTERNAL_PREFIX)) {
            return name.substring(name.indexOf(INTERNAL_PREFIX) + INTERNAL_PREFIX.length());
        }

        if (name.startsWith(USER_PREFIX)) {
            return name.substring(name.indexOf(USER_PREFIX) + INTERNAL_PREFIX.length());
        }

        return name;
    }

}
