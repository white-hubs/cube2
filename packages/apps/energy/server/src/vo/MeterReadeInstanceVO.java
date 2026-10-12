package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @author: lw
 * @date: 2022/7/8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeterReadeInstanceVO {

    private String genreName;
    private List<MeterDeviceVo> meterDeviceVos;

}
