package com.guhecloud.cube.sn.meattrade.plugin.dbc.api.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 大白菜账号配置
 *
 * @author lanchun
 * @date 2024/9/29 15:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DbcPorkArrangePushConfig {

    /**
     * 业务接口地址
     */
    private String serverApiAddr;

    /**
     * bi接口地址
     */
    private String serverBiApiAddr;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DbcPorkArrangePushConfig that = (DbcPorkArrangePushConfig) o;
        return Objects.equals(getUsername(), that.getUsername()) && Objects.equals(getPassword(), that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword());
    }
}
