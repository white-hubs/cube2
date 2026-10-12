package com.guhecloud.cube.biz.fusion.communicate.server.third.account.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThirdFusionAccountAllocationVO {

    /**
     * 第三方账号信息
     */
    private ThirdFusionAccountVO thirdFusionAccount;

    /**
     * 系统用户主键
     */
    private Long userId;
}
