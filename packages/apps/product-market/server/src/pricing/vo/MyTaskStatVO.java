package com.guhecloud.cube.srv.gen.product.market.server.pricing.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyTaskStatVO {
    private int waitNum;
    private int execNum;
    private int overNum;
    private int total;

    public int getTotal() {
        return waitNum + execNum + overNum;
    }
}
