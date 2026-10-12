package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2023-12-26  11:39
 * @Description:
 * @Version: 1.0
 */
@Data
public class BasicScenePointVO {
    private Long id;
    private String sceneName;    //	现场点名称
    private String sceneCode;    //	现场点编码
    private String sceneTypeId;    //	现场点类型
    private String manageDeptId;    //	现场点管理部门
    private String areaId;    //	现场点区域
    private String addrDetail;    //	详细地址
    private String sceneX;    //	现场点坐标X
    private String sceneY;    //	现场点坐标Y
    private String remark;    //	备注
}
