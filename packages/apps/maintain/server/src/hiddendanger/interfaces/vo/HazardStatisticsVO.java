package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-19  16:44
 * @Description:
 * @Version: 1.0
 */
@Data
public class HazardStatisticsVO {
    private Integer total;
    private Integer afoot;
    private Integer end;
    private List<HazardStatisticsTopVO> data;

}
