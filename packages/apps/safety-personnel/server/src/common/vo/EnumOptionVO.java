package com.guhecloud.cube.srv.gen.safety.personnel.server.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 枚举选项。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnumOptionVO {

    /**
     * 枚举编码，与接口入参/出参字符串一致。
     */
    private String code;

    /**
     * 中文说明。
     */
    private String description;
}
