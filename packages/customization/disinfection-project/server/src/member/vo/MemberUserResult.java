package com.guhecloud.cube.srv.gen.disinfection.server.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 小程序、app、H5用户
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberUserResult {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 注册地址
     */
    private String registerAddress;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 所属分组
     */
    private String groups;

}
