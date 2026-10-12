package com.guhecloud.cube.biz.adapter.park.server.application.reformer.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReformerParkingResult {
    /**
     * 返回值，0 为成功，其他为失败
     */
    private Integer resCode;
    /**
     * 返回说明
     */
    private String resMsg;
}
