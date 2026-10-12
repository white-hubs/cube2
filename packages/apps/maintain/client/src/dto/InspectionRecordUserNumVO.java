package com.guhecloud.cube.srv.gen.maintain.dto;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/21 14:51
 * @Description:
 */
@Data
public class InspectionRecordUserNumVO {

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 用户名
     */
    private String name;

    /**
     * 数量
     */
    private Integer num;

}
