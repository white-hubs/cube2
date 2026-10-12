package com.guhecloud.cube.srv.gen.adapter.patrol.server.model;

import lombok.Data;

@Data
public class DBConfig {
    //数据库链接
    private String dburl;
    //用户名
    private String username;
    //密码
    private String password;
    //驱动类名 全路径
    private String driverClass;
}
