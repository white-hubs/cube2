package com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountAllocationUserVO {

    /**
     * 第三方账号信息
     */
    private AccountVO account;

    /**
     * 系统用户主键
     */
    private Long userId;
}
