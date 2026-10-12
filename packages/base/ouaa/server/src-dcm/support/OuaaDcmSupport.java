package com.guhecloud.cube.biz.ouaa.dcm.support;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class OuaaDcmSupport {
    public static final String PROVIDER_OUAA = "OUAA";
    public static final String VERSION = "1.0";
    public static final String CONFIG_TYPE_APP_GROUP = "AppGroup";
    public static final String CONFIG_TYPE_APP = "App";

    public static final String CONFIG_TYPE_TERMINAL = "Terminal";
    public static final String CONFIG_TYPE_PRODUCT = "Product";

    public static final String CONFIG_TYPE_MENU = "Menu";

    public static final String CONFIG_TYPE_FUNCTION = "Function";
    public static final String CONFIG_TYPE_MENU_FUNCTION = "MenuFunction";
    public static final String CONFIG_TYPE_CARD = "Card";
    public static final String CONFIG_TYPE_APP_CARD = "AppCard";
    public static final String CONFIG_TYPE_PROD_APP = "ProdApp";
    public static final String CONFIG_TYPE_TERMINAL_APP = "TerminalApp";

    public static final String CONFIG_TYPE_PERMISSION = "Permission";

    public static final String CONFIG_TYPE_ROLE = "Role";

    public static final String CONFIG_TYPE_POSITION = "Position";
    public static final String CONFIG_TYPE_ROLE_TERMINAL = "RoleTerminal";
    public static final String CONFIG_TYPE_ROLE_APP = "RoleApp";
    public static final String CONFIG_TYPE_ROLE_MENU = "RoleMenu";
    public static final String CONFIG_TYPE_ROLE_MENU_FUNCTION = "RoleMenuFunction";
    public static final String CONFIG_TYPE_ROLE_MENU_DATA_PERMISSION = "RoleMenuDataPermission";

    public static final String CONFIG_TYPE_SUBJECT_PERMISSION = "SubjectPermission";
    public static final String CONFIG_TYPE_TENANT_PROD = "TenantProduct";
    public static final String CONFIG_TYPE_TENANT_APP = "TenantApp";

    /**
     * 以下为集中定义的Rci kw args key, 以消除无意引入的差异.
     */
    public static final String KW_KEY_PROD_CODE = "prodCode";
    public static final String KW_KEY_ROLE_NAME = "roleName";
    public static final String KW_KEY_TERMINAL_CODE = "terminalCode";
    public static final String KW_KEY_APP_CODE = "appCode";
    public static final String KW_KEY_MENU_CODE = "menuCode";
    public static final String KW_KEY_PARENT = "parent";

    // 用于树型结点的分隔符，注意目前的实现并没有对被分隔内容进行转义处理，
    // 存在一定风险，但暂时先可以不进行考虑。
    public static final String NODE_PATH_SEPARATOR = "::";

    // 依据依赖顺序确定的偏序集
    public static final List<String> CONFIG_TYPES_ORDERED = Arrays.asList(
            CONFIG_TYPE_APP_GROUP,
            CONFIG_TYPE_APP,
            CONFIG_TYPE_PRODUCT,
            CONFIG_TYPE_PROD_APP,
            CONFIG_TYPE_TERMINAL,
            CONFIG_TYPE_TERMINAL_APP,
            CONFIG_TYPE_CARD,
            CONFIG_TYPE_APP_CARD,
            CONFIG_TYPE_MENU,
            CONFIG_TYPE_PERMISSION,
            CONFIG_TYPE_TENANT_PROD,
            CONFIG_TYPE_TENANT_APP,
            CONFIG_TYPE_POSITION,
            CONFIG_TYPE_ROLE,
            CONFIG_TYPE_ROLE_TERMINAL,
            CONFIG_TYPE_ROLE_APP,
            CONFIG_TYPE_ROLE_MENU,
            CONFIG_TYPE_SUBJECT_PERMISSION
                                                                         );

    private OuaaDcmSupport() {
        // disable instantiation
    }
}
