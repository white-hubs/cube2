package com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.interceptor;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;

import java.util.Objects;

/**
 * 客户端数据隔离拦截器
 * 用于二维码云化服务，根据OAuth2客户端认证自动添加grantClientCode过滤条件
 * <p>
 * 规则：
 * - 内部服务客户端（client_id="inner_server"或"inner_terminal"）：查看所有数据，不添加过滤条件
 * - 外部系统客户端：只查看自己client_id对应的数据，自动添加 WHERE grant_client_id = client_id
 *
 * @author system
 */
public class ClientIsolateHandler implements TenantLineHandler {

    /**
     * 客户端隔离字段名称
     */
    public static final String CLIENT_COLUMN_NAME = "grant_client_id";

    /**
     * 内部服务客户端标识列表
     */
    private static final String[] INNER_CLIENT_IDS = { "inner_server", "inner_terminal" };

    private static final Integer[] INNER_TENANT_IDS = { 0 };

    public ClientIsolateHandler() {
    }

    @Override
    public Expression getTenantId() {
        // 获取当前客户端的client_id
        String clientId = SecurityContextHolder.getPrincipalProperty(Principal::getAccount);
        return new StringValue(clientId != null ? clientId : "");
    }

    @Override
    public String getTenantIdColumn() {
        return CLIENT_COLUMN_NAME;
    }

    @Override
    public boolean ignoreTable(String tableName) {
        // 判断是否为cloud_qr_code相关表
        if (tableName == null) {
            return true;
        }

        String lowerTableName = tableName.toLowerCase();
        // 只对二维码相关表进行过滤
        if (!lowerTableName.equals("cloud_qr_code") &&
            !lowerTableName.equals("cloud_qr_code_type")) {
            return true;
        }
        if (Objects.equals(tableName, "cloud_qr_route")) {
            return true; // 二维码类型表，跳过过滤
        }
        // 判断是否为内部服务客户端
        if (SecurityContextHolder.isInnerServer()) {
            return true; // 内部服务客户端，跳过过滤
        }

        // 判断是否为特殊的内部服务client_id
        for (String innerClientId : INNER_CLIENT_IDS) {
            if (SecurityContextHolder.isClient(innerClientId)) {
                return true; // 内部服务客户端，跳过过滤
            }
        }

        for (Integer innerTenantId : INNER_TENANT_IDS) {
            if (SessionContextHolder.getRequester().getTenant().equals(innerTenantId)) {
                return true; // 内部服务客户端，跳过过滤
            }
        }
        return false;
    }
}
