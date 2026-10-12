package com.guhecloud.cube.onecard.server.consumption.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionTypeVo {
    private Object value;
    private String name;
}
