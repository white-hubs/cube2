package com.guhecloud.cube.srv.gen.energymanagement.server.param;

import lombok.Data;

import java.util.List;

/**
 * 读数记录模板参数
 */
@Data
public class MeterReadLogTemplateParam {
    private List<Long> deviceIdList;
}
