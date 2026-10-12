package com.guhecloud.cube.customized.guhecloudwebsite.param;

import lombok.Data;

/**
 * 侧边图标查询参数
 */
@Data
public class SysSideIconQueryParam {

    /**
     * 标题
     */
    private String title;

    /**
     * 状态：1显示，0隐藏
     */
    private Integer status;

    /**
     * 是否开启：1开启，0关闭
     */
    private Integer isOpen;

}
