package com.guhecloud.cube.srv.gen.adapter.information.server.hik.dto;

import lombok.Data;

/**
 * 节目列表
 */
@Data
public class HikProgramResponse {

    /**
     * 节目ID
     */
    private Integer programId;
    /**
     * 节目名称
     */
    private String programName;
    /**
     * 节目别名
     */
    private String programAlias;
    /**
     * 节目类型编码：标准:normal,触摸:touch
     */
    private String programType;
    /**
     * 节目类型描述
     */
    private String programTypeName;
    /**
     * 节目分辨率
     */
    private String resolution;
    /**
     * 节目时长，播放时长
     */
    private String programLength;
    /**
     * 所属组织编码
     */
    private String orgIndexCode;
    /**
     * 所属组织名称
     */
    private String orgName;
    /**
     * 创建时间 "2019-04-09T19:13:56.691+0800"
     */
    private String createTime;
    /**
     * 更新时间 "2019-04-09T20:33:21.751+0800"
     */
    private String updateTime;
}
