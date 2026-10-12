package com.guhecloud.cube.base.alarm.server.flow.interfaces.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnConfirmedCountByStatusBO {
    private String code;
    private Integer num;
}
