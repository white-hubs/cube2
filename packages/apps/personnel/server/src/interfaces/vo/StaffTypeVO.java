package com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/5 20:20
 * @Description:
 */

@Data
public class StaffTypeVO {
    /**
     * id
     */
    private Long id;

    /**
     * 类型名称
     */
    private String name;
    /**
     * 类型编码
     */
    private String code;

    /**
     * 备注
     */
    private String remark;
}
