package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionProjectStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectPageVO {
    /**
     * 主键ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 项目编号
     */

    private String projectCode;

    /**
     * 项目名称
     */

    private String projectName;

    /**
     * 项目类型ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long projectTypeId;

    /**
     * 项目类型名称
     */

    private String projectTypeName;

    /**
     * 业主单位
     */
    private String company;

    /**
     * 项目开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 项目结束时间
     */
    private LocalDateTime endTime;

    /**
     * 项目负责人ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long leaderId;

    /**
     * 项目负责人名称
     */
    private String leaderName;
    /**
     * 项目面积
     */
    private String disinfectionArea;
    /**
     * 项目所在地名称
     */
    private String disinfectionLocationName;
    /**
     * 项目所在地经纬度
     */
    private String disinfectionLocation;

    /**
     * 项目消杀范围经纬度，多个
     */
    private String disinfectionScope;

    /**
     * 项目说明
     */
    private String description;

    /**
     * 项目状态
     */
    private DisinfectionProjectStatusEnum status;

    /**
     * 项目图片
     */
    private FileVO projectImage;
}
