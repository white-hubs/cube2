package com.guhecloud.cube.srv.gen.loose.works.vo;

import lombok.Data;

@Data
public class AbnormalProjectReportVO {

    private Long index;

    private Long id;
    /**
     * 小散工程名称
     */
    private String projectName;
    /**
     * 乙方
     */
    private String partyB;

    /**
     * 监管负责人名称
     */
    private String superviseUserName;
    /**
     * 监管部门
     */
    private Long superviseDep;

    /**
     * 监管部门
     */
    private String superviseDepName;
}
