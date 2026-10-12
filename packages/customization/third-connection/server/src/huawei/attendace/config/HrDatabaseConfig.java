package com.guhecloud.cube.srv.gen.third.connection.server.huawei.attendace.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * HR数据库连接配置
 * 通过构造函数传入配置参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrDatabaseConfig {
    /**
     * 数据库主机地址
     */
    private String host;
    
    /**
     * 数据库端口
     */
    private String port;
    
    /**
     * 数据库名称
     */
    private String database;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 数据库驱动类名
     */
    private String driverClass;
    
    /**
     * 数据库连接URL模板（从字典获取，支持占位符 %s）
     * 例如: jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=false;trustServerCertificate=true;sslProtocol=TLSv1.2
     * 占位符顺序: host, port, database
     */
    private String connectionUrlTemplate;
    
    /**
     * 获取数据库连接URL
     * 优先使用从字典获取的 connectionUrlTemplate（使用占位符），如果未配置则根据驱动类型动态生成
     */
    public String getConnectionUrl() {
        // 如果从字典配置了 connectionUrlTemplate，使用模板填充占位符
        if (connectionUrlTemplate != null && !connectionUrlTemplate.trim().isEmpty()) {
            return String.format(connectionUrlTemplate, host, port, database);
        }
        
        // 否则根据驱动类型动态生成（向后兼容）
        if (driverClass == null) {
            throw new IllegalArgumentException("数据库驱动类名不能为空");
        }
        
        // 判断是否为 MySQL 驱动
        if (driverClass.contains("mysql")) {
            // MySQL 连接 URL 格式: jdbc:mysql://host:port/database?参数
            return String.format("jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8", 
                    host, port, database);
        }
        
        // 判断是否为 SQL Server 驱动
        if (driverClass.contains("sqlserver")) {
            // SQL Server 连接 URL 格式: jdbc:sqlserver://host:port;databaseName=database;参数
            // encrypt=false: 禁用加密
            // trustServerCertificate=true: 信任服务器证书
            // sslProtocol=TLSv1.2: 指定使用 TLS 1.2 协议（兼容 TLS 1.0 服务器）
            return String.format("jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=false;trustServerCertificate=true;sslProtocol=TLSv1.2", 
                    host, port, database);
        }
        
        // 默认使用 SQL Server 格式（向后兼容）
        return String.format("jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=false;trustServerCertificate=true;sslProtocol=TLSv1.2", 
                host, port, database);
    }
}
