package com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GroupBaseInfoVo {
    private Long id;
    //名称
    private String groupControlName;
    //密码
    private String password;
    //回路数
    private Long num;
    //修改时间
    private LocalDateTime updateTime;
    //锁标识
    private Boolean isLock = false;
    //回路详情
    private List<GroupBaseDetailsVo> deviceDetails;

}
