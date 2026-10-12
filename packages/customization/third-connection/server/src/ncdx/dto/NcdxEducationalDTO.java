package com.guhecloud.cube.srv.gen.third.connection.server.ncdx.dto;

import lombok.Data;

import java.util.List;

/**
 * 南充党校教务三方对象
 */
@Data
public class NcdxEducationalDTO {
    private Integer bj_num; // 班次数量
    private Integer jzg_num; // 教职工人数
    private Integer swdx_num; // 行政学院班数量
    private Integer shzyxy_xy_num; // 社会主义学院培训人数
    private Integer shzyxy_num; // 社会主义学院班数量
    private Integer year_xy_num; // 当前年度培训人员
    private Integer swdx_xy_num; // 行政学院培训人数
    private Integer zx_xy_num; // 在校人员
    private Integer wtbb_xy_num; // 委托办班人数
    private Integer jg_num; // 组织机构数量
    private Integer wtbb_num; // 委托办班数量
    private Integer lj_xy_num; // 累计培训人员
    private Integer lx; // 立项数量
    private Integer wlx; // 未立项数量
    private Integer jx; // 结项数量
    private Integer wjx; // 未结项数量
    private List<ClassInfoDTO> classInfo;
}
