package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeterReadLogErrorVO {
    private Integer successCount;
    private List<RedLogErrorVO> errorList;
}
