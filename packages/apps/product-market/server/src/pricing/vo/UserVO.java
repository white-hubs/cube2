package com.guhecloud.cube.srv.gen.product.market.server.pricing.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
    private Long userId;
    private String userName;
    private String deptName;

    public UserVO(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
}
