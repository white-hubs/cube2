package com.guhecloud.cube.srv.gen.disinfection.server.member.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberGroupResult {
    /**
     * 主键
     */
    private Long id;
    /**
     * 管理员id
     */
    private Long memberUserId;
    /**
     * 当前登陆人是否是管理员
     */
    private Boolean memberUserBool;
    /**
     * 管理者名
     */
    private String memberUserName;
    /**
     * 分组名
     */
    private String groupName;
    /**
     * 用户数
     */
    private Integer userCount;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
