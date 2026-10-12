package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/17 17:33
 * @Description: 工程巡查记录
 */

@Data
public class SafetyErrorInspectItemVO {

    /**
     * 市场租户号
     */
    private Long recordId;
    /**
     * 市场租户名称
     */
    private String itemName;

}
