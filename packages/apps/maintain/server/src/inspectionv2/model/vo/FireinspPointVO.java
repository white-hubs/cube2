package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-15  16:15
 * @Description:
 * @Version: 1.0
 */
@Data
public class FireinspPointVO {
    private String id;
    private String projectId;
    private String type;
    private String code;
    private String name;
    private String areaId1;
    private String areaId2;
    private String areaId3;
    private String address;
    private String deptId;
    private String deptName;
    private String gisLongitude;
    private String gisLatitude;
    private String remark;
    private String enabled;

}
