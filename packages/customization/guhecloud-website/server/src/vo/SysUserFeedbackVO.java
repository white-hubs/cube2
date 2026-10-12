package com.guhecloud.cube.customized.guhecloudwebsite.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户留言VO
 */
@Data
public class SysUserFeedbackVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 联系方式
     */
    private String contactWay;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 应用场景
     */
    private String applicationScenario;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
