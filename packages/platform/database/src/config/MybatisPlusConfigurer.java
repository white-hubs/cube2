package com.guhecloud.platform.database.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.interceptor.CustomTenantLineInnerInterceptor;
import com.guhecloud.platform.database.interceptor.DeletedExcludeHandler;
import com.guhecloud.platform.database.interceptor.TenantIsolateHandler;
import com.guhecloud.platform.database.interceptor.special.DefaultSpecialCharacterHandler;
import com.guhecloud.platform.database.interceptor.special.DmSpecialCharacterHandler;
import com.guhecloud.platform.database.interceptor.special.SpecialCharacterHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus配置
 *
 * @author Woody
 * @date 2021/6/25
 */
@Configuration
@MapperScan(basePackages = { "com.guhecloud.platform.database.mapper", "com.guhecloud.**.dao", "com.guhecloud.**.mapper" })
public class MybatisPlusConfigurer {
    /**
     * 瀚高数据库
     */
    private static final String HG = "hg";
    /**
     * 达梦数据库
     */
    private static final String DM = "dm";
    @Value(value = "${mybatis-plus.global-config.db-config.db-type:mysql}")
    private String dbType;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 设置租户隔离拦截器
        interceptor.addInnerInterceptor(new CustomTenantLineInnerInterceptor() {
            {
                this.setTenantLineHandler(new TenantIsolateHandler());
            }
        });

        // 设置删除数据过滤拦截器
        interceptor.addInnerInterceptor(new CustomTenantLineInnerInterceptor() {
            {
                this.setTenantLineHandler(new DeletedExcludeHandler());
            }
        });
        // 设置分页拦截器，数据过滤条件与拦截器设置顺序有关，分页拦截器最后设置才能在分页时同时使用数据隔离过滤条件
        //        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(this.getDbType());
        interceptor.addInnerInterceptor(paginationInnerInterceptor);

        return interceptor;
    }

    @Bean
    public SpecialCharacterHandler specialCharacterHandler() {
        if (DbType.DM == getDbType()) {
            return new DmSpecialCharacterHandler();
        }
        return new DefaultSpecialCharacterHandler();
    }

    private DbType getDbType() {
        DbType type = DbType.MYSQL;
        if (ObjectUtils.isNotEmpty(dbType)) {
            if (dbType.contains(HG)) {
                type = DbType.POSTGRE_SQL;
            } else if (dbType.contains(DM)) {
                type = DbType.DM;
            }
        }
        return type;
    }
}