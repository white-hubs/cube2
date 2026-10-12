package com.guhecloud.cube.biz.meattrade.server.base.vo;

import com.guhecloud.cube.biz.meattrade.server.base.enumeration.InspectorEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 抽检员信息
 */
@Data
public class InspectorVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 抽检员名称
     */
    private String name;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 抽检员类型
     */
    private InspectorEnum type;

    /**
     * 添加时间
     */
    private LocalDateTime addTime;

    /**
     * 备注
     */
    private String remark;

}
