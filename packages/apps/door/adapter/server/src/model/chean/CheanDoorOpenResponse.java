package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.chean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheanDoorOpenResponse {
    //开门是否成功标志 1  成功，0  失败
    private String flag;
    //备注
    private String memo;
}
